package com.song.archives.dao;

import com.song.archives.model.AuditInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * Created by souyouyou on 2018/11/17.
 */
public interface AuditInfoRepository extends CrudRepository<AuditInfo, Long> {

    AuditInfo findByFileIdAndApplicant(Long fileId,String userName);

    AuditInfo findByFileIdAndApplicantAndType(Long fileId,String userName,String type);

    AuditInfo findByFileIdAndType(Long fileId,String type);

    List<AuditInfo> findAll(Specification specification, Pageable pageable);

}
