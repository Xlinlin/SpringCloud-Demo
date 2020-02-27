package com.xiao.springcloud.demo.common.sign.request;

import com.xiao.springcloud.demo.common.sign.SignConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;

/**
 * [简要描述]: 包装reqeust 复制下request流，防止在controller层获取不到reqeust body情况
 * [详细描述]:
 *
 * @author xiaolinlin
 * @version 1.0, 2020/2/21 16:38
 * @since JDK 1.8
 */
@Slf4j
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper
{
    private final byte[] body;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @exception IllegalArgumentException if the request is null
     */
    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request)
    {
        super(request);
        String sessionStream = getBodyString(request);
        // 使用utf-8编码格式
        body = sessionStream.getBytes(Charset.forName(SignConstants.UTF_8));
    }

    /**
     * 获取请求Body
     *
     * @param request
     * @return
     */
    public String getBodyString(final ServletRequest request)
    {
        String encode = request.getCharacterEncoding();
        if (StringUtils.isBlank(encode))
        {
            encode = SignConstants.UTF_8;
        }
        StringBuilder sb = new StringBuilder();
        try (InputStream inputStream = cloneInputStream(request.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName(encode))))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
        }
        catch (IOException e)
        {
            log.error("Request流包装发生错误，", e);
        }
        return sb.toString();
    }

    /**
     * Description: 复制输入流</br>
     *
     * @param inputStream
     */
    public InputStream cloneInputStream(ServletInputStream inputStream)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try
        {
            while ((len = inputStream.read(buffer)) > -1)
            {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.flush();
        }
        catch (IOException e)
        {
            log.error("Request流复制发生错误，", e);
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    @Override
    public BufferedReader getReader()
    {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream()
    {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream()
        {

            @Override
            public int read()
            {

                return bais.read();
            }

            @Override
            public boolean isFinished()
            {
                return false;
            }

            @Override
            public boolean isReady()
            {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener)
            {

            }
        };
    }
}
