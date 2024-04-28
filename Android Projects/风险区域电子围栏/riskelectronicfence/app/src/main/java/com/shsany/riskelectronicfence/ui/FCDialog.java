package com.shsany.riskelectronicfence.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;

import com.shsany.riskelectronicfence.MainActivity;
import com.shsany.riskelectronicfence.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;

public class FCDialog extends Dialog {
    private static final String TAG = "FCDialog";
    private EditText ipAddressEditText;
    private EditText portNumberEditText;
    private EditText messageEditText;
    private Button sendButton;
    private TextView receivedDataTextView;

    public FCDialog(@NonNull Context context) {
        super(context);
    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fc);
        setTitle("UDP Dialog");

        ipAddressEditText = findViewById(R.id.ip_address);
        portNumberEditText = findViewById(R.id.port_number);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);
        receivedDataTextView = findViewById(R.id.received_data_text_view);

        ipAddressEditText.setText("192.168.1.118");
        portNumberEditText.setText("18001");
        //messageEditText.setText("0000A10000000000000000A0A0A000000000000000000000000000000000000000000000000000000000000000");

        MainActivity.setSocket2(18001);
        setSendButton();
        receiveDataThread();
    }

    private void setSendButton() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipAddress = ipAddressEditText.getText().toString();
                int portNumber = Integer.parseInt(portNumberEditText.getText().toString());

                String inputString = messageEditText.getText().toString();
                int intValue = Integer.parseInt(inputString);
                String hexString = String.format("%02X", intValue);
                String finalHexString = "6869BB001A0001E240000000017FFFFFFF0102" + hexString;
                /*
                 * 先加密 后计算CR16
                 * */
                String jYHexString = String.format("%02X", calculateCRC16(encBeforeCac(finalHexString)));
                finalHexString = finalHexString + jYHexString + "EEEE";
                Log.e(TAG, "SendHexString:" + finalHexString);
                sendThread(ipAddress, portNumber, finalHexString);
            }
        });
    }

    public static int calculateCRC16(String data) {
        int crc = 0xFFFF;
        for (byte b : data.getBytes()) {
            crc = (crc >>> 8) ^ (crc ^ b);
            crc = crc >>> 8 ^ (crc & 0x00FF) ^ (crc & 0x00FF) << 8;
        }
        return crc & 0xFFFF;
    }

    public void cacMessage(String message) {

    }

    private void sendThread(String ipAddress, int portNumber, String hexMessage) {
        new Thread(new Runnable() {
            @OptIn(markerClass = UnstableApi.class)
            @Override
            public void run() {
                try {
                    InetAddress address = InetAddress.getByName(ipAddress);
                    //原始发送的数据
                    byte[] data = hexStringToByteArray(hexMessage);
                    DatagramPacket packet = new DatagramPacket(data, data.length, address, portNumber);
                    MainActivity.socket2.send(packet);
                    Log.e("UDP send", "Sent UDP data to " + address + portNumber + ":" + data);
                } catch (Exception e) {
                    Log.e("UDP error", "run: " + e.getMessage());
                }
            }
        }).start();
    }

    /*
     * 对数据进行加密
     * */
    public String encBeforeCac(String hexMessage) {
        byte[] data = hexStringToByteArray(hexMessage);
        //对数据进行加密
        byte[] encrypt = encryptWith0xA(data, 5, data.length - 1);

        return byteArrayToHexString(encrypt);
    }

    public static String byteArrayToHexString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : data) {
            stringBuilder.append(String.format("%02X", b));
        }
        return stringBuilder.toString();
    }

    public byte[] encryptWith0xA(byte[] data, int startIndex, int endIndex) {
        // 将原始数据中指定范围的部分复制到新数组中，并与 0xA 进行异或操作
        for (int i = startIndex; i < endIndex; i++) {
            data[i] = (byte) (data[i] ^ -96);
        }
        return data;
    }

    private void receiveDataThread() {
        new Thread(new Runnable() {
            @OptIn(markerClass = UnstableApi.class)
            @Override
            public void run() {
                try {
                    while (true) {
                        byte[] buffer = new byte[45];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        MainActivity.socket2.receive(packet);
                        byte[] hexData = packet.getData();
                        int[] decryptData = handleReceive(hexData);
                        if (decryptData != null) {
                            Message msg = new Message();
                            String data = "success";
                            msg.obj = data;
                            receivedDataTextView.post(new Runnable() {
                                @Override
                                public void run() {
                                    receivedDataTextView.setText("\n" + "收到数据" + "\n>>16米=" + decryptData[0] + "\n>>8米=" + +decryptData[1] + "" + "\n当前距离=" + decryptData[2] + "" + "\n");
                                }
                            });
                            Log.e("UDP receive", "Received data:" + handleReceive(hexData).toString());
                        } else {
                            Message msg = new Message();
                            msg.obj = "数据解析错误，错误的数据为:" + hexData.toString();
                            receivedDataTextView.post(new Runnable() {
                                @Override
                                public void run() {
                                    receivedDataTextView.append(msg.obj.toString() + "\n");
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public int[] handleReceive(byte[] hexData) {
        byte typeByte = hexData[2];
        String hexString = String.format("0x%02X", typeByte & 0xFF);

        if (hexString.equals("0xA1")) {
            int startIndex = 11;
            int messageLength = hexData.length - startIndex - 4;
            if (messageLength % 30 == 0) {
                for (int i = 0; i < messageLength / 30; i++) {
                    int messageStartIndex = startIndex + i * 30;
                    int messageEndIndex = messageStartIndex + 30;
                    byte[] message = Arrays.copyOfRange(hexData, messageStartIndex, messageEndIndex);
                    //int keyA = 0xA;
                    int keyA = -96;
                    byte fifthByte = message[4];
                    byte sixthByte = message[5];
                    byte seventhByte = message[6];
                    byte decryptedFifthByte = (byte) (fifthByte ^ keyA);
                    byte decryptedSixthByte = (byte) (sixthByte ^ keyA);
                    byte decryptedSeventhByte = (byte) (seventhByte ^ keyA);
                    Log.e("Decrypted Fifth Byte", "解密后的第五字节数据: " + decryptedFifthByte);
                    Log.e("Decrypted Sixth Byte", "解密后的第六字节数据: " + decryptedSixthByte);
                    Log.e("Decrypted Seventh Byte", "解密后的第七字节数据: " + decryptedSeventhByte);
                    int[] decrypted = new int[3];
                    decrypted[0] = decryptedFifthByte & 0xFF;
                    decrypted[1] = decryptedSixthByte & 0xFF;
                    decrypted[2] = decryptedSeventhByte & 0xFF;
                    return decrypted;
                }
            } else {
                return null;
            }
        }
        return null;
    }

    public byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
