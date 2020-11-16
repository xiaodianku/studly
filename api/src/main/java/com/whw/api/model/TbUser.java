package com.whw.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 登录用户列表
 */
@Entity   //注意包名
@Table(name="tb_user")   //自动建表,与数据库对应
@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class TbUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="nickname")
    private String nickname;
    @Column(name="img")
    private String img;
    @Column(name="mobile")
    private String mobile;
    @Column(name="be_user")
    private Integer beUser;
    @Column(name="openid")
    private String openid;
    @Column(name="last_login_time")
    private Timestamp lastLoginTime;
    @Column(name="gmt_create")
    private Timestamp gmtCreate;
    @Column(name="gmt_modify")
    private Timestamp gmtModify;

    @Transient
    private String beUserName;

}
