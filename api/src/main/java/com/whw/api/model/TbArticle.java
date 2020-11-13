package com.whw.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Proxy;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 文章列表
 */
@Entity   //注意包名
@Table(name="tb_article")   //自动建表,与数据库对应
@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class TbArticle {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -3245478690496182643L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="title_img")
    private String titleImg;
    @Column(name="title")
    private String title;
    @Column(name="type")
    private Integer type;
    @Column(name="course_name")
    private String courseName;
    @Column(name="participant_numbers")
    private Integer participantNumbers;
    @Column(name="unlock_class_hours")
    private String unlockClassHours;
    @Column(name="unlock_rules")
    private String unlockRules;
    @Column(name="category_id")
    private Integer categoryId;
    @Column(name="money")
    private BigDecimal money;
    @Column(name="describes")
    private String describes;
    @Column(name="status")
    private Integer status;
    @Column(name="content")
    private String content;

    @Column(name="images")
    private String images;
    @Column(name="videos")
    private String videos;
    @Column(name="contact_info")
    private String contactInfo;

    @Column(name="gmt_create")
    private Timestamp gmtCreate;
    @Column(name="gmt_modify")
    private Timestamp gmtModify;

    @Transient
    private String categoryName;
    @Transient
    private List<String> imageList;
    @Transient
    private List<String> videoList;

}
