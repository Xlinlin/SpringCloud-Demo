/*
 * Winner 
 * 文件名  :MqttTestClient.java
 * 创建人  :llxiao
 * 创建时间:2018年4月16日
*/

package com.skywalking.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月16日
 * @since 项目名称 项目版本
 */
public class MqttTestClient
{
    public static final String HOST = "tcp://localhost:1883";

    public static final String TOPIC = "test";

    private String clientId;

    private MqttClient client;

    private MqttConnectOptions options;

    // private String userName = "admin";
    //
    // private String passWord = "admin123";

    public MqttTestClient(String clientId)
    {
        this.clientId = clientId;
    }

    private void start() throws MqttException
    {
        try
        {
            // host为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            client = new MqttClient(HOST, clientId, new MemoryPersistence());
            // MQTT的连接设置
            options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置连接的用户名
            // options.setUserName(userName);
            // 设置连接的密码
            // options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            // // 设置回调
            // MqttTopic topic = client.getTopic(TOPIC+"/test/");
            // // setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
            // options.setWill(topic, "close".getBytes(), 2, true);
            client.connect(options);
            client.setCallback(new ClientCallback(client, options));
            // 订阅消息
            int[] Qos =
            {
                    1
            };
            String[] topic1 =
            {
                    "PTP/test"
            };
            client.subscribe(topic1, Qos);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws MqttException
    {
        String clientId = "admin1";
        MqttTestClient client = new MqttTestClient(clientId);
        client.start();
    }
}
