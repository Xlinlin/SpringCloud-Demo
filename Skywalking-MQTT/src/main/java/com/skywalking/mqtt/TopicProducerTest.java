/*
 * Winner 
 * 文件名  :TopicProducerTest.java
 * 创建人  :llxiao
 * 创建时间:2018年4月16日
*/

package com.skywalking.mqtt;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang3.StringUtils;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月16日
 * @since 项目名称 项目版本
 */
public class TopicProducerTest
{
    public static void main(String[] args) throws JMSException
    {
        // 创建连接工厂
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        String PTP_CLIENTID = "PTP_CLIENTID";

        // 鉴权，如没有开启可省略
        // factory.setUserName("admin");
        // factory.setPassword("admin123");
        // 创建JMS连接实例，并启动连接
        Connection connection = factory.createConnection();

        connection.start();

        // 创建Session对象，不开启事务
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 创建主题
        Topic topic = session.createTopic("PTP.test");

        // 创建生成者
        MessageProducer producer = session.createProducer(topic);

        // 设置消息不需持久化。默认消息需要持久化
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        Scanner sc = new Scanner(System.in);
        boolean isStart = true;
        String userMsg = "";
        String msg = "";
        TextMessage message = null;
        String[] messages = null;
        String clientId = null;
        while (isStart)
        {
            userMsg = sc.nextLine();
            if (StringUtils.isBlank(userMsg) || "stop".equals(userMsg))
            {
                System.out.println("Stop producer message!");
                isStart = false;
            }
            messages = userMsg.split(":");
            msg = "Hello MQ,Client msg:" + messages[0];
            message = session.createTextMessage(msg);

            if (messages.length == 2)
            {
                clientId = messages[1];
            }

            // 发送指定消息，配合主题分发策略使用，以附带用户ID ，分发策略对特定的主题进行拦截解析分发
            if (StringUtils.isNotBlank(clientId))
            {
                message.setStringProperty(PTP_CLIENTID, clientId);
            }

            // 发送消息。non-persistent 默认异步发送；persistent 默认同步发送
            producer.send(message);
        }
        sc.close();
        // 关闭连接
        producer.close();
        session.close();
        connection.close();

    }
}
