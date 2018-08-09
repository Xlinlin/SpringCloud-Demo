/*
 * Winner 
 * 文件名  :ClientCallback.java
 * 创建人  :llxiao
 * 创建时间:2018年4月16日
*/

package com.skywalking.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月16日
 * @since 项目名称 项目版本
 */
public class ClientCallback implements MqttCallback
{
    private MqttClient client;
    private MqttConnectOptions options;

    public ClientCallback(MqttClient client, MqttConnectOptions options)
    {
        this.client = client;
        this.options = options;
    }

    @Override
    public void connectionLost(Throwable cause)
    {
        // 连接丢失后，一般在这里面进行重连
        System.out.println("连接断开，可以做重连");
        try
        {
            client.connect(options);
        }
        catch (MqttSecurityException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (MqttException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // while(!sampleClient.isConnected()){
        // try {
        // Thread.sleep(1000);
        // sampleClient.connect(connOpts);
        // //客户端每次上线都必须上传自己所有涉及的订阅关系，否则可能会导致消息接收延迟
        // sampleClient.subscribe(topicFilters,qos);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception
    {
        // subscribe后得到的消息会执行到这里面
        System.out.println("接收消息主题 : " + topic);
        System.out.println("接收消息Qos : " + message.getQos());
        String msg = new String(message.getPayload());
        System.out.println("接收消息到服务端内容 : " + msg);
        if (msg.contains("close"))
        {
            client.close();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token)
    {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }
}
