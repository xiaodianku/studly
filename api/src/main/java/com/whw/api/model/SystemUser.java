package com.whw.api.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity   //注意包名
@Table(name="sys_user")   //自动建表,与数据库对应
@Data
public class SystemUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="username")
    private String username;
    @Column(name="mobile")
    private String mobile;
    @Column(name="password")
    private String password;
    @Column(name="nickname")
    private String nickname;
    @Column(name="role_id")
    private Integer roleId;
    @Column(name="delete_status")
    private Integer deleteStatus; //0-未删除， 99-删除
    @Column(name="create_time")
    private Timestamp createTime;
    @Column(name="update_time")
    private Timestamp updateTime;
    @Column(name="create_id")
    private Integer createId;

    @Transient
    private String roleName;

}
