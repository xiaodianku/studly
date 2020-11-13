package com.whw.api.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 留言
 */
@Entity   //注意包名
@Table(name="tb_category")   //自动建表,与数据库对应
@Data
public class TbCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="sort")
    private Integer sort;
    @Column(name="name")
    private String name;
    @Column(name="gmt_create")
    private Timestamp gmtCreate;
    @Column(name="gmt_modify")
    private Timestamp gmtModify;


}
