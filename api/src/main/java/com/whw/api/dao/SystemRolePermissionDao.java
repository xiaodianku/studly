package com.whw.api.dao;

import com.whw.api.model.SystemRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface SystemRolePermissionDao extends JpaRepository<SystemRolePermission, Integer> {


}
