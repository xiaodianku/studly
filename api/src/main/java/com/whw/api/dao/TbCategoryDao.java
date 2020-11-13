package com.whw.api.dao;


import com.whw.api.model.TbCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TbCategoryDao extends JpaRepository<TbCategory, Integer> {

    List<TbCategory> findByOrderBySortDesc();

}
