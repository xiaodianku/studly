package com.whw.api.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity   //注意包名
@Table(name="sys_role")   //自动建表,与数据库对应
@Data
public class SystemRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="role_name")
    private String roleName;
    @Column(name="create_time")
    private Timestamp createTime;
    @Column(name="update_time")
    private Timestamp updateTime;
    @Column(name="create_id")
    private Integer createId;

    @Transient
    List<SystemUser> users;

    @Transient
    List<SystemPermission> menus;
}
