package com.whw.api.config.ResultJson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GirlException extends RuntimeException{    //注意：spring中，只有RuntimeException才会进行事务回滚，Exception不会进行事务回滚

    private Integer code;
    private String message;

    public GirlException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMsg();
    }

    public GirlException(String message) {
        super(message);
        this.code = ResultEnum.PRIMARY_SCHOOL.getCode();
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
