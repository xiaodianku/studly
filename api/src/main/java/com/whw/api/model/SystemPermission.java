package com.whw.api.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity   //注意包名
@Table(name="sys_permission")   //自动建表,与数据库对应
@Data
public class SystemPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="menu_code")
    private String menuCode;
    @Column(name="menu_name")
    private String menuName;
    @Column(name="permission_code")
    private String permissionCode;
    @Column(name="permission_name")
    private String permissionName;
    @Column(name="required_permission")
    private String requiredPermission;
    @Column(name="parent_id")
    private Integer parentId;
    @Column(name="sort")
    private Integer sort;
    @Column(name="status")
    private Integer status;

    @Transient
    List<SystemPermission> nextList;
}
