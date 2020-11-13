package com.whw.api.config.emnu;

public enum ArticleType {

    //1-购买课程，2-免费课程，3-打卡
    PAY_COURSE(1, "购买课程"),
    FREE_COURSE(2, "免费课程"),
    CLOCK_IN(3, "打卡")
    ;


    private Integer value;
    private String key;

    ArticleType(Integer value, String key) {
        this.key = key;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
