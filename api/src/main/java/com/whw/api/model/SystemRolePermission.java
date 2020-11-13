package com.whw.api.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity   //注意包名
@Table(name="sys_role_permission")   //自动建表,与数据库对应
@Data
public class SystemRolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="role_id")
    private Integer roleId;
    @Column(name="permission_id")
    private Integer permissionId;
    @Column(name="create_time")
    private Timestamp createTime;

}
