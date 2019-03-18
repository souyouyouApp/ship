package com.song.archives.dao;

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
public interface DataRepository extends CrudRepository<ZiliaoInfoEntity,Long> {


    Page<ZiliaoInfoEntity> findAll(Specification<ZiliaoInfoEntity> specification, Pageable pageable);

    List<ZiliaoInfoEntity> findAll(Specification<ZiliaoInfoEntity> specification);

    long count();

    @Query(value = "select id from ziliao_info where creator =:creator",nativeQuery = true)
    List<Long> findIdByCreator(@Param(value = "creator") String creator);

    @Query(value = "select zi.id from ziliao_info zi where id  not in (select t.id from ziliao_info t join module_file m on t.id = m.t_id JOIN file_info f where f.file_code = m.file_code and f.audit = 0 GROUP BY t.id HAVING count(f.audit) > 0)",nativeQuery = true)
    List<Long> findZiliaoAuditRes();

}
