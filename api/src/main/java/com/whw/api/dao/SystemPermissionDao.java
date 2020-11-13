package com.whw.api.dao;


import com.whw.api.model.SystemPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SystemPermissionDao extends JpaRepository<SystemPermission, Integer> {


}
