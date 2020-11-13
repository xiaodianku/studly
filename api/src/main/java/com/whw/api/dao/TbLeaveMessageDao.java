package com.whw.api.dao;


import com.whw.api.model.TbLeaveMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TbLeaveMessageDao extends JpaRepository<TbLeaveMessage, Integer> {


    @Query(value = "select a.*  from tb_leave_messages a  limit ?1,?2" ,nativeQuery = true)
    List<TbLeaveMessage> getList(int offset, int rows);

    List<TbLeaveMessage> findByArticleIdOrderByGmtCreateDesc(Integer articleId);

}
