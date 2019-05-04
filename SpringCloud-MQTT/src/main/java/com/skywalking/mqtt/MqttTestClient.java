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

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

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
        String host = "tcp://39.108.176.226:1883";

        try
        {
            // host为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            client = new MqttClient(host, clientId, new MemoryPersistence());
            // MQTT的连接设置
            options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置连接的用户名
            options.setUserName("admin");
            // 设置连接的密码
            options.setPassword("admin123".toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);

            //            options.setSocketFactory(setSSLSocketFactory());

            // // 设置回调
            // MqttTopic topic = client.getTopic(TOPIC+"/test/");
            // // setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
            // options.setWill(topic, "close".getBytes(), 2, true);
            client.connect(options);
            client.setCallback(new ClientCallback(client, options));
            // 订阅消息
            int[] Qos = { 1
            };
            String[] topic1 = { "PTP/test"
            };
            client.subscribe(topic1, Qos);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            client.close();
        }
    }

    private SocketFactory setSSLSocketFactory()
    {
        SSLSocketFactory factory = null;
        try
        {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManager[] trustAllCerts = getTrustManagers();
            sslContext.init(null, trustAllCerts, new SecureRandom());
            factory = sslContext.getSocketFactory();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return factory;
    }

    private TrustManager[] sslTrustManagers()
    {
        TrustManager[] trustManagers = null;
        try
        {
            // p12文件ssl
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            InputStream instream = MqttTestClient.class.getClassLoader().getResourceAsStream("client.p12");
            keyStore.load(instream, "admin123".toCharArray());

            //kbs文件ssl
            //KeyStore ts = KeyStore.getInstance("BKS");
            //bks文件，还有生成时密钥库口令
            //ts.load(context.getAssets().open("key.bks"), "123456".toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(keyStore);
            trustManagers = tmf.getTrustManagers();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return trustManagers;
    }

    private TrustManager[] getTrustManagers()
    {
        TrustManager[] trustAllCerts = new TrustManager[1];
        trustAllCerts[0] = new PurTrustManager();
        return trustAllCerts;
    }

    public static void main(String[] args) throws MqttException
    {
        String clientId = "javaClient:" + System.currentTimeMillis();
        MqttTestClient client = new MqttTestClient(clientId);
        client.start();
        System.out.println(clientId);
    }
}
