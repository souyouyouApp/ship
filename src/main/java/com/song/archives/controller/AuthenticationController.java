package com.song.archives.controller;

import com.song.archives.aspect.ArchivesLog;
import com.song.archives.dao.PermissionRepository;
import com.song.archives.dao.RoleRepository;
import com.song.archives.dao.UserRepository;
import com.song.archives.model.Permission;
import com.song.archives.model.Role;
import com.song.archives.model.User;
import com.song.archives.utils.LoggerUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by souyouyou on 2018/2/26.
 */

@Controller
@RequestMapping("/")
public class AuthenticationController {

    Logger logger = Logger.getLogger(AuthenticationController.class);

    public static String SUCCESS = "success";

    private String msg = "failed";

    private JSONObject result = new JSONObject();

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * 新建角色
     *
     * @param role
     * @return
     */
    @ArchivesLog(operationType = "saveRole", description = "新建角色")
    @RequestMapping(value = "saveRole")
    @ResponseBody
    String saveRole(Role role) {


        try {
            roleRepository.save(role);
            msg = SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage());
            msg = "Exception";
        }

        result.put("msg",msg);
        LoggerUtils.setLoggerReturn(request,msg);
        return result.toString();
    }

    /**
     * 删除角色
     *
     * @param rids
     * @return
     */
    @ArchivesLog(operationType = "deleteRoleByIds", description = "删除角色")
    @RequestMapping(value = "deleteRoleByIds")
    @ResponseBody
    String deleteRoleByIds(Long[] rids) {

        try {
            if (rids != null && rids.length > 0) {

                for (Long rid : rids) {
                    Role role = roleRepository.findOne(rid);
                    List<User> users = userRepository.findUsersByRoleId(rid);
                    for (User user : users) {
                        user.getRoles().remove(role);
                        userRepository.save(user);
                    }
                    roleRepository.delete(rid);
                }
            }

            msg = SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage());
            msg = "删除角色异常";
        }

        LoggerUtils.setLoggerReturn(request,msg);

        result.put("msg",msg);
        return result.toString();

    }

    /**
     * 新建权限
     *
     * @param permission
     * @return
     */
    @ArchivesLog(operationType = "savePermission", description = "新建权限")
    @RequestMapping(value = "savePermission")
    @ResponseBody
    String savePermission(Permission permission) {



        try{
            permissionRepository.save(permission);
            msg = SUCCESS;
        }catch (Exception e){
            msg = "新建权限异常";
        }

        result.put("msg",msg);
        LoggerUtils.setLoggerReturn(request,msg);
        return result.toString();
    }


    /**
     * 删除权限
     *
     * @param pids
     * @return
     */
    @ArchivesLog(operationType = "deletePermissionByIds", description = "删除权限")
    @RequestMapping(value = "deletePermissionByIds")
    @ResponseBody
    String deletePermissionByIds(Long[] pids) {

        try {
            if (pids != null && pids.length > 0) {

                for (Long pid : pids) {
                    Permission permission = permissionRepository.findOne(pid);

                    List<Role> roles = roleRepository.findRolesByPermissionId(pid);

                    for (Role role : roles) {
                        role.getPermissions().remove(permission);
                        roleRepository.save(role);
                    }
                    permissionRepository.delete(pid);
                }
            }
            msg = SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage());
            msg = "删除权限异常";
        }

        result.put("msg",msg);
        LoggerUtils.setLoggerReturn(request,msg);
        return result.toString();
    }


    /**
     * 权限列表
     *
     * @return
     */
    @ArchivesLog(operationType = "permissionList", description = "权限列表",writeFlag = false)
    @RequestMapping(value = "/permissionList")
    String permissionList() {
        return "permission/list";
    }

    /**
     * 查询权限信息
     *
     * @param page
     * @param size
     * @return
     */
    @ArchivesLog(operationType = "permissions", description = "查询权限列表",descFlag = true)
    @RequestMapping(value = "/permissions")
    @ResponseBody
    String permisssions(@RequestParam(value = "pageIndex", defaultValue = "1") Integer page,
                        @RequestParam(value = "pageSize", defaultValue = "10") Integer size) {


        page = page - 1;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        List<Permission> permissions = permissionRepository.findAll(pageable);

        result.put("msg", SUCCESS);
        LoggerUtils.setLoggerSuccess(request);
        result.put("result", JSONArray.fromObject(permissions).toString());
        return result.toString();
    }


    /**
     * 角色列表
     *
     * @return
     */
    @ArchivesLog(operationType = "roleList", description = "角色列表",writeFlag = false)
    @RequestMapping(value = "/roleList")
    String roleList() {
        return "role/list";
    }

    /**
     * 查询角色信息
     *
     * @param page
     * @param size
     * @return
     */
    @ArchivesLog(operationType = "roles", description = "查询角色列表",descFlag = true)
    @RequestMapping(value = "/roles")
    @ResponseBody
    @RequiresRoles("securitor")
    String roles(@RequestParam(value = "pageIndex", defaultValue = "0") Integer page,
                 @RequestParam(value = "pageSize", defaultValue = "10") Integer size) {

        page = page - 1;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        List<Role> roles = roleRepository.findAll(pageable);

        result.put("msg", SUCCESS);
        LoggerUtils.setLoggerSuccess(request);
        result.put("result", JSONArray.fromObject(roles).toString());
        return result.toString();
    }

    /**
     * 查询用户角色
     *
     * @param id
     * @return
     */
    @ArchivesLog(operationType = "findRoleByUserId", description = "查询用户角色")
    @RequestMapping(value = "/findRoleByUserId")
    @ResponseBody
    String findRoleByUserId(Long id) {

        User queryUser = userRepository.findOne(id);

        List<Object> roles = userRepository.findRoleByUserId(id);

        result.put("msg", SUCCESS);
        LoggerUtils.setLoggerSuccess(request);
        result.put("result", JSONArray.fromObject(roles).toString());
        return result.toString();

    }


    /**
     * 保存用户角色
     *
     * @param userId
     * @param rids
     * @return
     */
    @ArchivesLog(operationType = "saveRoles", description = "保存用户角色")
    @RequestMapping(value = "/saveRoles")
    @ResponseBody
    String saveRoles(Long userId, Long[] rids) {

        User queryUser = userRepository.findOne(userId);

        try {
            User user = userRepository.findOne(userId);

            Set<Role> roles = new HashSet<>();

            for (Long rid : rids) {
                Role role = roleRepository.findOne(rid);
                roles.add(role);
            }

            user.setRoles(roles);
            userRepository.save(user);
            msg = SUCCESS;
        } catch (Exception e) {
            msg = "保存用户角色异常";
        }


        result.put("msg", msg);
        LoggerUtils.setLoggerReturn(request,msg);
        return result.toString();

    }


    /**
     * 查询角色权限
     *
     * @param rid
     * @return
     */
    @ArchivesLog(operationType = "findPermissionByRoleId", description = "查询角色权限",descFlag = true)
    @RequestMapping(value = "/findPermissionByRoleId")
    @ResponseBody
    @RequiresRoles("securitor")
    String findPermissionByRoleId(String rid) {

        List<Object> permissions = permissionRepository.findPermissionByRoleId(rid);
        LoggerUtils.setLoggerSuccess(request);

        return JSONArray.fromObject(permissions).toString();

    }


    /**
     * 保存角色权限
     *
     * @param roleId
     * @param pids
     * @return
     */
    @ArchivesLog(operationType = "savePermissions", description = "保存角色权限",descFlag = true)
    @RequestMapping(value = "/savePermissions")
    @ResponseBody
    String savePermissions(Long roleId, Long[] pids) {


        try {
            Role role = roleRepository.findOne(roleId);


            Set<Permission> permissions = new HashSet<>();

            for (Long pid : pids) {
                Permission permission = permissionRepository.findOne(pid);
                permissions.add(permission);
            }

            role.setPermissions(permissions);
            roleRepository.save(role);

            msg = SUCCESS;

        } catch (Exception e) {
            msg = "保存角色权限异常";
        }

        LoggerUtils.setLoggerReturn(request,msg);

        result.put("msg", msg);
        return result.toString();

    }


    protected User getUser() {
        Subject subject = SecurityUtils.getSubject();
        return (User) subject.getPrincipal();
    }
}
