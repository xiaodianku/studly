package com.whw.api.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 浏览记录
 */
@Entity   //注意包名
@Table(name="tb_browsing_history")   //自动建表,与数据库对应
@Data
public class TbBrowsingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="article_id")
    private Integer articleId;
    @Column(name="user_id")
    private Integer userId;

    @Column(name="browsing_time")
    private Timestamp browsingTime;

    @Transient
    private String articleTitle;
    @Transient
    private String nickname;
    @Transient
    private String categoryName;
    @Transient
    private String browsingTimeStr;

}
