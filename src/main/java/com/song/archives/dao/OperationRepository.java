package com.song.archives.dao;

import com.song.archives.model.OperationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by souyouyou on 2018/2/2.
 */
public interface OperationRepository extends CrudRepository<OperationEntity,Long>,JpaSpecificationExecutor<OperationEntity> {

    Page<OperationEntity> findAll(Specification<OperationEntity> specification,Pageable pageable);
    Page<OperationEntity> findAll(Pageable pageable);

}
