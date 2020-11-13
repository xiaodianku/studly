package com.whw.api.dao;

import com.whw.api.model.TbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TbUserDao extends JpaRepository<TbUser, Integer> {

    @Query(value = "select a.*  from tb_user a order by a.create_time desc  limit ?1,?2" ,nativeQuery = true)
    List<TbUser> getList(int offset, int rows);

    TbUser findByOpenid(String openid);

    TbUser findByMobile(String mobile);

}
