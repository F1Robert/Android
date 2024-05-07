### Android Mqtt使用文档

#### 1.Mqtt原理

用tcp传输协议实现的局域网通信框架

#### 2.Mqtt使用步骤

​	1.创建Mqtt服务端 使用局域网内一个支持开启Mqtt服务的设备开启服务

​		   常用的Mqtt服务软件有EMQX，MQTT explore

​	2.使用局域网内其它设备进行mqtt连接

#### 3.Android Mqtt连接代码

主要参数说明

1.目标地址，格式 tcp://ip:端口

如 tcp://192.168.0.100:1883

2.如mqtt服务端已启用用户认证

需添加用户名和密码作为连接参数

未启用用户认证，则可匿名进行连接

```
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
        subscribeTopic(uwbTopic, 1);
    } catch (MqttException e) {
        e.printStackTrace();
    }
}
```

3.mqtt主题订阅 参数topic qos(0,1,-1)

```
public void subscribeTopic(String topic, int qos) {
    try {
        client.subscribe(topic, qos);
    } catch (MqttException e) {
        e.printStackTrace();
    }
}
```

4.向服务端的某个主题下发message

```
public void sendUwbMessage(String topic, String msg) {
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
```