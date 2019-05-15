package com.xiao.custom.config.web.auth.exception;

import com.xiao.custom.config.web.auth.util.ResultJson;
import lombok.Getter;

/**
 * @author Joetao
 * Created at 2018/8/24.
 */
@Getter
public class CustomException extends RuntimeException
{
    private ResultJson resultJson;

    public CustomException(ResultJson resultJson)
    {
        this.resultJson = resultJson;
    }
}
