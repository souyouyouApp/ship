package com.song.archives.dao;

import com.song.archives.model.Permission;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by souyouyou on 2018/2/9.
 */
public interface PermissionRepository extends CrudRepository<Permission,Long> {
    List<Permission> findAll(Pageable pageable);


//    @Query(value = "select up.id permissionId,up.name permissionName,ifnull(ur.id,0) marker,ifnull(ur.id,0) from permission up left join role_permission urp on urp.permission_id = up.id left join (select id from role where id =:roleId) ur on ur.id = urp.role_id where\n" +
//            "  up.identification not in ('user:delete','user:add')  group by up.id",nativeQuery = true)
    @Query(value = "select p.id permissionId,p.NAME permissionName,ifnull( rp.role_id, 0 ) marker,ifnull( rp.role_id, 0 ) roleId from permission p LEFT JOIN role_permission rp on p.id = rp.permission_id and rp.role_id =:roleId",nativeQuery = true)
    List<Object> findPermissionByRoleId(@Param(value = "roleId") String roleId);
}
