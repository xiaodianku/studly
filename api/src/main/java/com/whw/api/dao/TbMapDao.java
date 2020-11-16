package com.whw.api.dao;


import com.whw.api.model.TbMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TbMapDao extends JpaRepository<TbMap, Integer> {

    List<TbMap> findByKNameAndVName(String kName, String vName);

}
