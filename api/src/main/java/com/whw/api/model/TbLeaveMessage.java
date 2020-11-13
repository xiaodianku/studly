package com.whw.api.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 留言
 */
@Entity   //注意包名
@Table(name="tb_leave_messages")   //自动建表,与数据库对应
@Data
public class TbLeaveMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="article_id")
    private Integer articleId;
    @Column(name="user_id")
    private Integer userId;
    @Column(name="content")
    private String content;
    @Column(name="audio")
    private String audio;

    @Column(name="gmt_create")
    private Timestamp gmtCreate;
    @Column(name="gmt_modify")
    private Timestamp gmtModify;

    @Transient
    private String articleTitle;
    @Transient
    private String nickname;
    @Transient
    private String img;
    @Transient
    private String leaveMessageTime;

}
