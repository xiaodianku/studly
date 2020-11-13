package com.whw.api.dao;


import com.whw.api.model.SystemRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SystemRoleDao extends JpaRepository<SystemRole, Integer> {

}
