package com.song.archives.dao;

import com.song.archives.model.LowInfoEntity;
import com.song.archives.model.ZiliaoInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * Created by souyouyou on 2018/2/28.
 */
public interface LowRepository extends CrudRepository<LowInfoEntity,Long> {


    Page<LowInfoEntity> findAll(Specification<LowInfoEntity> specification, Pageable pageable);

    List<LowInfoEntity> findAll(Specification<LowInfoEntity> specification);

    Page<LowInfoEntity> findAll(Pageable pageable);

    long count();

}
