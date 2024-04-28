package com.shsany.riskelectronicfence.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;

import com.shsany.riskelectronicfence.MainActivity;
import com.shsany.riskelectronicfence.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class UDPDialog extends Dialog {
    private EditText ipAddressEditText;
    private EditText portNumberEditText;
    private EditText messageEditText;
    private Button sendButton;
    private TextView receivedDataTextView;

    public UDPDialog(@NonNull Context context) {
        super(context);
    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_udp);
        setTitle("UDP Dialog");

        ipAddressEditText = findViewById(R.id.ip_address);
        portNumberEditText = findViewById(R.id.port_number);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);
        receivedDataTextView = findViewById(R.id.received_data_text_view);

        ipAddressEditText.setText("192.168.1.118");
        portNumberEditText.setText("18000");
        messageEditText.setText("6869A10A270A0A0A1C0B0B0A0AC9C10A0B210AF5F83C0AF5F58AF6141C21091509590925096F0A0A0AD2BCEEEE");
        //messageEditText.setText("0000A10000000000000000000000000A0A0A000000000000000000000000000000000000000000000000000000");
        setSendButton();
    }

    @OptIn(markerClass = UnstableApi.class)
    public void initSocket1() {
        MainActivity.setSocket1(18000);
        receiveDataThread();
    }

    private void setSendButton() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipAddress = ipAddressEditText.getText().toString();
                int portNumber = Integer.parseInt(portNumberEditText.getText().toString());
                String message = messageEditText.getText().toString();
                sendThread(ipAddress, portNumber, message);
            }
        });
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
                    //对数据进行加密
                    byte[] encrypt = encryptWith0xA(data, 11, 41);
                    DatagramPacket packet = new DatagramPacket(encrypt, encrypt.length, address, portNumber);
                    MainActivity.socket1.send(packet);
                    Log.e("UDP send", "Sent UDP data to " + address + portNumber + ":" + data);
                } catch (Exception e) {
                    Log.e("UDP error", "run: " + e.getMessage());
                }
            }
        }).start();
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
                        MainActivity.socket1.receive(packet);
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
                            if (decryptData[2] < 8) {
                                Message nMsg = new Message();
                                nMsg.what = 100;
                                MainActivity.mHandler.sendMessage(nMsg);
                                dismiss();
                            } else if (decryptData[2] < 16) {
                                Message nMsg = new Message();
                                nMsg.what = 102;
                                dismiss();
                                MainActivity.mHandler.sendMessage(nMsg);
                            }
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
