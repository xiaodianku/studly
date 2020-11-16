package com.whw.api.dao;


import com.whw.api.model.TbArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TbArticleDao extends JpaRepository<TbArticle, Integer> {


    @Query(value = "select a.*  from tb_article a  limit ?1,?2" ,nativeQuery = true)
    List<TbArticle> getList(int offset, int rows);

    List<TbArticle> findByStatusOrderByGmtCreateDesc(Integer status);

    List<TbArticle> findByCategoryIdAndStatusOrderByGmtCreateDesc(Integer category, Integer status);

}
