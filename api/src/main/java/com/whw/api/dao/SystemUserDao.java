package com.whw.api.dao;


import com.whw.api.model.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SystemUserDao extends JpaRepository<SystemUser, Integer> {

	SystemUser findByMobileAndPassword(String moblie, String password);

	SystemUser findByMobile(String moblie);

	SystemUser findByMobileAndIdNot(String moblie, int id);

	@Query(value = "select a.*  from sys_user a where delete_status = 0 order by a.create_time desc limit ?1,?2" ,nativeQuery = true)
	List<SystemUser> getList(int offset, int rows);


}
