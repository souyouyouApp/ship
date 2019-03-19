package com.song.archives.dao;

import com.song.archives.model.StorageEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface StorageRepository extends CrudRepository<StorageEntity,Long> {


    @Query(value = "select sum(data_length) from information_schema.tables where table_schema='zscq'",nativeQuery = true)
    Double GetDataBaseSpace();

    @Query(value="select case when sum(file_size) is null then 0 else sum(file_size) end from file_info where file_classify=:filetype",nativeQuery = true)
    Double GetFileSpaceByType(@Param(value ="filetype") Integer filetype);
    @Query(value="select sum(DATA_LENGTH) from information_schema.tables where table_schema='zscq' AND table_name='operation_log'; ",nativeQuery = true)
    Double GetLogSpace();



}
