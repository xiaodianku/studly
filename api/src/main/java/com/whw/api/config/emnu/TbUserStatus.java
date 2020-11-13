package com.whw.api.config.emnu;

public enum TbUserStatus {

    ZERO(0, "正常"),
	HMD(1, "黑名单"),
    DELETE(99,"删除")
     ;


	TbUserStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * type code
     */
    private Integer code;
    private String name;
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
 
    
}
