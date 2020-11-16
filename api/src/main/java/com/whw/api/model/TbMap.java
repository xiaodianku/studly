package com.whw.api.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 字典
 */
@Entity   //注意包名
@Table(name="tb_map")   //自动建表,与数据库对应
@Data
public class TbMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="k_name")
    private String kName;
    @Column(name="v_name")
    private String vName;



}
