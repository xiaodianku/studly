package com.whw.api.dao;


import com.whw.api.model.TbBrowsingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TbBrowsingHistoryDao extends JpaRepository<TbBrowsingHistory, Integer> {


    @Query(value = "select a.*  from tb_browsing_history a  limit ?1,?2" ,nativeQuery = true)
    List<TbBrowsingHistory> getList(int offset, int rows);

    List<TbBrowsingHistory> findByUserIdOrderByBrowsingTimeDesc(Integer userId);

}
