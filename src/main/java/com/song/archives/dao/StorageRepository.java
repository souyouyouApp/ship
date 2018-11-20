package com.song.archives.dao;

import com.song.archives.model.StorageEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface StorageRepository extends CrudRepository<StorageEntity,Long> {


    @Query(value = "select sum(truncate(data_length/1024/1024/1024, 2)) from information_schema.tables where table_schema='zscq'",nativeQuery = true)
    Double GetDataBaseSpace();

    @Query(value="select case when round(sum(file_size/1024/1024/1024),2) is null then 0 else round(sum(file_size/1024/1024/1024),2) end from file_info where file_type=:filetype",nativeQuery = true)
    Double GetFileSpaceByType(@Param(value ="filetype") Integer filetype);





}
