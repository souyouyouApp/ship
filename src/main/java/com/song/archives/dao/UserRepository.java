package com.song.archives.dao;

import com.song.archives.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User,Long> {

    User findByUsername(String username);

    @Query(value = "select ur.id roleId,ur.identification roleName,ifnull(uu.id,0) maker,uu.id userId from role ur left join user_role uur on uur.role_id = ur.id left join (select id from user where id =:userId) uu on uu.id = uur.user_id group by ur.id",nativeQuery = true)
    List<Object> findRoleByUserId(@Param(value = "userId") Long userId);

    List<User> findAll(Specification specification,Pageable pageable);

    List<User> findAll(Specification specification);

    @Query(value = "select * from user where id in (select user_id from user_role where role_id =:roleId)",nativeQuery = true)
    List<User> findUsersByRoleId(@Param(value = "roleId") Long roleId);

    List<User> findByIdIsIn(Long[] userids);

    @Query(value = "select * from `user` u JOIN user_role ur ON  u.id = ur.user_id JOIN role_permission rp ON (ur.role_id = rp.role_id AND rp.permission_id = 32)",nativeQuery = true)
    List<User> findAuditUser();

    @Query(value = "select * from user where type=:typeId",nativeQuery = true)
    List<User> findUsersByTypeId(@Param(value = "typeId") Long roleId);
}
