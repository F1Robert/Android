package com.shsany.riskelectronicfence.mqtt;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/*
 * MQtt客户端
 * 与UWB端及AI平台端进行数据通信
 * */
public class MyMqtt {
    private String uwbHost = "tcp://192.168.0.105:1883";
    private String aiHost = "tcp://192.168.0.100:1883";
    private String userName = "admin";
    private String passWord = "123456";
    private MqttClient client;
    private String id;
    private static MyMqtt instance; // = new MyMqtt();
    private MqttTopic mqttTopic;
    private String uwbStTopic = "UWB/SET";
    private String uwbAlarmTopic = "UWB/ALARM";
    private String aiTopic = "UWB/ALARM";
    private MqttMessage message;
    public static Handler mqHandler;

    public MyMqtt(String id, MqttCallback callback, boolean cleanSession,String host) {
        try {
            //id应该保持唯一性
            client = new MqttClient(host, id, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(cleanSession);
            options.setUserName(userName);
            options.setPassword(passWord.toCharArray());
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);
            if (callback == null) {
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        Log.e("MQTT", "Connection Lost: " + cause.getMessage());
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) {
                        Log.d("MQTT", "Message Arrived on topic " + topic + ": " + message.toString());
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        Log.d("MQTT", "Delivery Complete: " + token.getMessageId());
                    }
                });
            } else {
                client.setCallback(callback);
            }
            connect(options);
            client.subscribe(uwbStTopic);
            client.subscribe(uwbAlarmTopic);
        } catch (MqttException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
    }

    public void connect(MqttConnectOptions options) throws MqttException {
        mqHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.what == 100) {
                    sendUwbMessage((String) msg.obj);
                }
                return false;
            }
        });
        client.connect();
    }

    public MyMqtt(String id, MqttCallback callback, boolean cleanSession, String userName, String password) {
        try {
            client = new MqttClient(uwbHost, id, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(cleanSession);
            if (userName != null && password != null) {
                options.setUserName(userName);
                options.setPassword(password.toCharArray());
            }
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);
            if (callback == null) {
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        Log.e("MQTT", "Connection Lost: " + cause.getMessage());
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) {
                        Log.d("MQTT", "Message Arrived on topic " + topic + ": " + message.toString());
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        Log.d("MQTT", "Delivery Complete: " + token.getMessageId());
                    }
                });
            } else {
                client.setCallback(callback);
            }
            connect(options);
            subscribeTopic(uwbStTopic, 1);
            subscribeTopic(uwbAlarmTopic, 1);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public MyMqtt(String id, String[] args, MqttCallback callback, boolean cleanSession) {
        try {
            //id应该保持唯一性
            client = new MqttClient(args[9], id, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(cleanSession);
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);
            if (callback == null) {
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        Log.e("MQTT", "Connection Lost: " + cause.getMessage());
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) {
                        Log.d("MQTT", "Message Arrived on topic " + topic + ": " + message.toString());
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        Log.d("MQTT", "Delivery Complete: " + token.getMessageId());
                    }
                });
            } else {
                client.setCallback(callback);
            }
            connect(options);
            subscribeTopic(uwbStTopic, 1);
            subscribeTopic(uwbAlarmTopic, 1);
        } catch (MqttException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
    }

    public void sendUwbMessage(String msg) {
        sendUwbMessage(uwbStTopic, msg);
    }

    public void sendAiMessage(String msg) {
        sendAiMessage(aiTopic, msg);
    }

    /*
     * 发送消息出去
     * */
    public void sendUwbMessage(final String topic, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    message = new MqttMessage();
                    message.setQos(1);
                    message.setRetained(true);
                    message.setPayload(msg.getBytes());
                    mqttTopic = client.getTopic(topic);
                    MqttDeliveryToken token = mqttTopic.publish(message);//发布主题
                    token.waitForCompletion();
                } catch (Exception e) {
                    Log.e("MainActivity_fence", "sendMessage: 发送失败");
                }
            }
        }).start();
    }


    public void sendAiMessage(String topic, String msg) {
        try {
            message = new MqttMessage();
            message.setQos(1);
            message.setRetained(true);
            message.setPayload(msg.getBytes());
            mqttTopic = client.getTopic(topic);
            MqttDeliveryToken token = mqttTopic.publish(message);//发布主题
            token.waitForCompletion();
        } catch (Exception e) {
            Log.e("MainActivity_fence", "sendMessage: 发送失败");
        }
    }

    public void subscribe(String[] topicFilters, int[] qos) {
        try {
            client.subscribe(topicFilters, qos);
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 订阅主题

    }

    public void subscribeTopic(String topic, int qos) {
        try {
            client.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}