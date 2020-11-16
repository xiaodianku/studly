package com.whw.api.config.ResultJson;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 捕获异常的类，返回信息给浏览器，可以自定义返回的code,msg等信息
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandle {


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e) {
        if (e instanceof GirlException) {   //判断异常是否是我们定义的异常
            log.error("【自定义异常】{}", e.getMessage());
            GirlException girlException = (GirlException) e;
            return ResultUtil.error(girlException.getCode(), girlException.getMessage());
        }else {
            e.printStackTrace();
            log.error("【系统异常】{}", e);
            return ResultUtil.error(-1, "未知错误");
        }
    }
}
