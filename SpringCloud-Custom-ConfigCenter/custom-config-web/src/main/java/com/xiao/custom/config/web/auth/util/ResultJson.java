package com.xiao.custom.config.web.auth.util;

import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;

/**
 * @author Joetao
 * RESTful API 返回类型
 * Created at 2018/3/8.
 */
@Data
public class ResultJson<T> implements Serializable
{

    private static final long serialVersionUID = 783015033603078674L;
    private int code;
    private String msg;
    private T data;

    public static ResultJson ok()
    {
        return ok("");
    }

    public static ResultJson ok(Object o)
    {
        return new ResultJson(ResultCode.SUCCESS, o);
    }

    public static ResultJson failure(ResultCode code)
    {
        return failure(code, "");
    }

    public static ResultJson failure(ResultCode code, Object o)
    {
        return new ResultJson(code, o);
    }

    public ResultJson(ResultCode resultCode)
    {
        setResultCode(resultCode);
    }

    public ResultJson(ResultCode resultCode, T data)
    {
        setResultCode(resultCode);
        this.data = data;
    }

    public void setResultCode(ResultCode resultCode)
    {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    @Override
    public String toString()
    {
        return "{" + "\"code\":" + code + ", \"msg\":\"" + msg + '\"' + ", \"data\":\"" + data + '\"' + '}';
    }

    public static void main(String[] args)
    {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("admin123"));
    }
}
