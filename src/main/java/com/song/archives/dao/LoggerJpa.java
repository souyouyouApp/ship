package com.song.archives.dao;

import com.song.archives.model.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggerJpa extends JpaRepository<OperationEntity,Integer> {

}
