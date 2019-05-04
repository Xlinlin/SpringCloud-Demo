package com.skywalking.mqtt;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/5/4 12:33
 * @since JDK 1.8
 */
public class PurTrustManager implements TrustManager, X509TrustManager
{
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException
    {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException
    {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers()
    {
        return null;
    }


}
