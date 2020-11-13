package com.whw.api.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity   //注意包名
@Table(name="tb_sms")   //自动建表,与数据库对应
@Data
public class TbSms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="mobile")
    private String mobile;
    @Column(name="code")
    private String code;
    @Column(name="message")
    private String message;
    @Column(name="title")
    private String title;
    @Column(name="create_time")
    private Timestamp createTime;
}
