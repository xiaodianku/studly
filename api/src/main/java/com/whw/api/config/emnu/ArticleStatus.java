package com.whw.api.config.emnu;

public enum ArticleStatus {

    //0-未启用，1-启用，99-删除
    NOT_OPEN(0, "未启用"),
    OPEN(1, "启用"),
    DELETE(99, "删除")
    ;


    private Integer value;
    private String key;

    ArticleStatus(Integer value, String key) {
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
