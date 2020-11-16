package com.whw.api.config.emnu;

public enum MapStaticEnum {

    //可以登录的电话号码
    LOGIN_PHONE("loginMobile")
    ;


    private String name;

    MapStaticEnum(String name ) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
