package com.song.archives.controller;

import com.song.archives.aspect.ArchivesLog;
import com.song.archives.dao.NotifyRepository;
import com.song.archives.dao.OperationRepository;
import com.song.archives.dao.StorageRepository;
import com.song.archives.dao.UserRepository;
import com.song.archives.model.NotifyEntity;
import com.song.archives.model.OperationLog;
import com.song.archives.model.StorageEntity;
import com.song.archives.model.User;
import com.song.archives.utils.DateUtil;
import com.song.archives.utils.MySQLDatabaseBackup;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by souyouyou on 2018/2/1.
 */

@Controller
@RequestMapping("/")
public class UserController {

    Logger logger = Logger.getLogger(UserController.class);

    public static String SUCCESS = "success";

    private String msg = "failed";

    private String operationLogInfo = "";

    private JSONObject result = new JSONObject();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private NotifyRepository notifyRepository;
    @Value("${backup.path}")
    private String savePath;
    @Value("${project.overdate}")
    private String overDateStr;
    @Value("${project.timeOut}")
    private Long timeOut;

    private Date overDate;

    private String backUpFileName = "";

    private static Object spaceAlertLock=new Object();

    /**
     * 视图跳转
     *
     * @param viewName
     * @return
     */
    @ArchivesLog(operationType = "viewName", operationName = "视图跳转")
    @RequestMapping(value = "/{viewName}")
    String viewJump(@PathVariable("viewName") String viewName) {
        return viewName;
    }

    @ArchivesLog(operationType = "login", operationName = "登录页面")
    @RequestMapping(value = "/")
    String login() {
        return "login";
    }

    /**
     * 日志页面
     *
     * @return
     */
    @ArchivesLog(operationType = "logList", operationName = "日志页面")
    @RequestMapping(value = "logList")
    String logList() {
        return "log/list";
    }

    @ArchivesLog(operationType = "userInfo", operationName = "用户基本信息")
    @RequestMapping(value = "userInfo")
    @RequiresRoles("administrator")
    public ModelAndView userInfo(@RequestParam(value = "uId") Long uId) {
        ModelAndView mav = new ModelAndView("member/info");
        User userInfo = userRepository.findOne(uId);
        mav.addObject("userInfo",userInfo);
        return mav;
    }



    @ResponseBody
    @RequestMapping(value = "/backupLog")
    public String exportMysql(){
        result = new JSONObject();
        boolean backFlag = false;
        try {
            backUpFileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date())+".sql";
            backFlag = MySQLDatabaseBackup.exportDatabaseTool("localhost","root","root",savePath,backUpFileName,"zscq");
            msg = SUCCESS;
            result.put("path",savePath+"/"+backUpFileName);
        } catch (InterruptedException e) {
            msg = "Error";
            e.printStackTrace();
        }

        result.put("msg",msg);
        result.put("result",backFlag);
        return result.toString();
    }

    /**
     * 日志列表
     *
     * @param page
     * @param size
     * @return
     */
    @ArchivesLog(operationType = "logs", operationName = "日志列表")
    @RequestMapping(value = "logs")
    @ResponseBody
    String logs(@RequestParam(value = "pageIndex", defaultValue = "0") Integer page,
                @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                @RequestParam(value = "operationUsername", defaultValue = "") String operationUsername,
                @RequestParam(value = "operationDetail", defaultValue = "") String operationDescrib,
                @RequestParam(value = "startDate", defaultValue = "") String startDate,
                @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        page = page - 1;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);


        Specification<OperationLog> specification = (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (!operationUsername.equals("")) {
                predicates.add(criteriaBuilder.like(root.get("operationUserName"), "%" + operationUsername + "%"));
            }

            if (!operationDescrib.equals("")) {
                predicates.add(criteriaBuilder.like(root.get("operationDescrib"), "%" + operationDescrib + "%"));
            }

            if (!startDate.equals("") && !endDate.equals("")){
                predicates.add(criteriaBuilder.between(root.get("operationStartTime"),startDate,endDate));
            }


            //审计员查看管理员和保密员的操作日志
            if (getUser().getUsername().equals("comptroller")){
                CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("operationUserName"));
                in.value("securitor");
                in.value("administrator");
                predicates.add(in);
            }

            //保密员查看用户和审计员的日志
            if (getUser().getUsername().equals("securitor")){

                CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("operationUserName"));
                in.value("administrator");
                in.value("securitor");

                predicates.add(in.not());

            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));

        };

        Page<OperationLog> operationLogs = operationRepository.findAll(specification, pageable);

        operationLogInfo = "用户【" + getUser().getRealName() + "】查询日志";
        result.put("msg", msg);
        result.put("operationLog", operationLogInfo);
        result.put("result", JSONArray.fromObject(operationLogs));
        return result.toString();
    }

    /**
     * 修改账户状态
     *
     * @param id
     * @param status
     * @return
     */
    @ArchivesLog(operationType = "operateUser", operationName = "修改账户状态")
    @RequestMapping(value = "operateUser")
    @ResponseBody
    String operateUser(@RequestParam(value = "uid") Long id,
                       @RequestParam(value = "status") Boolean status) {

        result = new JSONObject();

        try {

            User user = userRepository.findOne(id);
            user.setAvailable(status);
            userRepository.save(user);

            operationLogInfo = "用户【" + getUser().getRealName() + "】修改用户【" + user.getUsername() + "】状态为:" + status;
            msg = SUCCESS;

        } catch (Exception e) {
            logger.error("修改账户状态:" + e.getMessage());
            msg = "Exception";
        }

        result.put("msg", msg);
        result.put("operationLog", operationLogInfo);

        return result.toString();
    }

    @RequestMapping(value = "updatePassword")
    @ResponseBody
    public String updatePwd(@RequestParam(value = "oldPwd")String oldPwd,
                            @RequestParam(value = "newPwd")String newPwd,
                            @RequestParam(value = "repeatPwd")String repeatPwd){

        operationLogInfo = "用户【" + getUser().getRealName() + "】修改密码";
        result = new JSONObject();
        String passWord = new SimpleHash(Md5Hash.ALGORITHM_NAME, oldPwd, getUser().getUsername(), 2).toHex();

        if (!getUser().getPassword().equals(passWord)){
            msg = "原始密码错误";
        }else {

            if (!newPwd.equals(repeatPwd)){
                msg = "新密码与确认密码不一致";
            }

            User user = getUser();
            user.setPassword(new SimpleHash(Md5Hash.ALGORITHM_NAME, repeatPwd, getUser().getUsername(), 2).toHex());
            user.setLastUpdatepwd(new Date());
            userRepository.save(user);
            msg = SUCCESS;

        }




        result.put("msg", msg);
        result.put("operationLog", operationLogInfo);

        return result.toString();

    }

    /**
     * 新建用户
     *
     * @param user
     * @return
     */
    @ArchivesLog(operationType = "saveUser", operationName = "新建用户")
    @RequestMapping(value = "saveUser")
    @ResponseBody
    String saveUser(User user) {

        user.setPassword(new SimpleHash(Md5Hash.ALGORITHM_NAME, user.getPassword(), user.getUsername(), 2).toHex());
        user.setType(0);
        user.setAvailable(true);

        try {
            User existUser= userRepository.findByUsername(user.getUsername());
            if(existUser!=null){
                msg="用户名已经存在";
                operationLogInfo = "用户【" + getUser().getRealName() + "】新建用户【" + user.getRealName()+"】,用户名已经存在";
            }else {
                user.setLastUpdatepwd(new Date());
                userRepository.save(user);
                operationLogInfo = "用户【" + getUser().getRealName() + "】新建用户【" + user.getRealName()+"】成功";
                msg = "success";
            }

        } catch (Exception e) {
            msg = "save user failed";
        }

        operationLogInfo = "用户【" + getUser().getRealName() + "】新建用户【" + user.getRealName()+"】";
        result.put("msg", msg);
        result.put("operationLog", operationLogInfo);
        return result.toString();
    }


    @ArchivesLog(operationType = "updateUser", operationName = "更新用户信息")
    @RequestMapping(value = "updateUser")
    @ResponseBody
    @Transactional
    String updateUser(User user) {

        StringBuffer optDesc = new StringBuffer();

        HashMap<String, String> userMap = new HashMap<>();

        //用户信息
        userMap.put("username","用户名");
        userMap.put("mobile","联系电话");
        userMap.put("type","用户类型");
        userMap.put("description","描述");
        userMap.put("positions","职务");
        userMap.put("realName","真实姓名");
        userMap.put("idNo","证件号码");

        User oldUser = userRepository.findOne(user.getId());
        try {
            if (null == user.getPassword() || user.getPassword().equals("")){
                user.setPassword(oldUser.getPassword());
            }else {
                user.setPassword(new SimpleHash(Md5Hash.ALGORITHM_NAME, user.getPassword(), user.getUsername(), 2).toHex());
            }
            user.setType(user.getType() == null?oldUser.getType():user.getType());
            user.setAvailable(user.getAvailable()?user.getAvailable():false);
            user.setRoles(user.getRoles().size()==0?oldUser.getRoles():user.getRoles());

            if(user.getPositions()==null){
                user.setPositions("");
            }

            if(user.getDescription()==null) {
                user.setDescription("");
            }

            if (!user.getUsername().equals(oldUser.getUsername())){
                optDesc.append("将【用户名】由【"+oldUser.getUsername()+"】修改为【"+user.getUsername()+"】、");
            }

            if (!user.getRealName().equals(oldUser.getRealName())){
                optDesc.append("将【真实姓名】由【"+oldUser.getRealName()+"】修改为【"+user.getRealName()+"】、");
            }

            if (!user.getMobile().equals(oldUser.getMobile())){
                optDesc.append("将【联系电话】由【"+oldUser.getMobile()+"】修改为【"+user.getRealName()+"】、");
            }

            if (!user.getDescription().equals(oldUser.getDescription())){
                optDesc.append("将【描述】由【"+oldUser.getDescription()+"】修改为【"+user.getDescription()+"】、");
            }

            if (!user.getPositions().equals(oldUser.getPositions())){
                optDesc.append("将【职务】由【"+oldUser.getPositions()+"】修改为【"+user.getPositions()+"】、");
            }

            if (!user.getIdNo().equals(oldUser.getIdNo())){
                optDesc.append("将【证件号码】由【"+oldUser.getIdNo()+"】修改为【"+user.getIdNo()+"】、");
            }

            userRepository.save(user);
            msg = "success";
        } catch (Exception e) {
            msg = "update user failed";
        }
        operationLogInfo = "用户【" + getUser().getRealName() + "】更新用户【" + user.getUsername()+"】信息, "+optDesc.toString();

        result.put("msg", msg);
        result.put("operationLog", operationLogInfo);
        return result.toString();
    }


    @ArchivesLog(operationType = "getUserInfo", operationName = "获取用户信息")
    @RequestMapping(value = "getUserInfo")
    @ResponseBody
    String getUserInfo(Long uid) {

        User user = userRepository.findOne(uid);

        return JSONObject.fromObject(user).toString();

    }

    /**
     * 删除勾选用户
     *
     * @param uids
     * @return
     */
    @ArchivesLog(operationType = "deleteUserByIds", operationName = "删除勾选用户")
    @RequestMapping(value = "deleteUserByIds")
    @ResponseBody
    String deleteUserByIds(Long[] uids) {

        operationLogInfo = "用户【" + getUser().getRealName() + "】删除用户【";
        try {
            if (uids != null && uids.length > 0) {

                for (Long uid : uids) {
                    User userObj = userRepository.findOne(uid);
                    operationLogInfo += userObj.getUsername() + ",";
                    userRepository.delete(userObj);
                }
            }

            msg = SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage());
            msg = "delete user failed";
        }

        operationLogInfo = operationLogInfo.substring(0, operationLogInfo.length() - 1) + "】";
        result.put("msg", msg);
        result.put("operationLog", operationLogInfo);
        return result.toString();
    }


    /**
     * 用户列表
     *
     * @return
     */
    @ArchivesLog(operationType = "memberList", operationName = "用户列表")
    @RequestMapping(value = "/memberList")
    String memberList() {
        return "member/list";
    }


    /**
     * 查询用户信息
     *
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/users")
    @ResponseBody
    @ArchivesLog(operationType = "users", operationName = "查询用户信息")
    String users(@RequestParam(value = "pageIndex", defaultValue = "0") Integer page,
                 @RequestParam(value = "pageSize", defaultValue = "10") Integer size) {
        page = page - 1;
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(page, size, sort);

        Specification<User> specification = (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            //屏蔽超级管理员
            predicates.add(criteriaBuilder.notEqual(root.get("username"), "superadmin"));
            predicates.add(criteriaBuilder.notEqual(root.get("username"), "administrator"));
            predicates.add(criteriaBuilder.notEqual(root.get("username"), "securitor"));
            predicates.add(criteriaBuilder.notEqual(root.get("username"), "comptroller"));

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };


        List<User> users = userRepository.findAll(specification, pageable);

        operationLogInfo = "用户【" + getUser().getRealName() + "】查询用户列表";
        result.put("msg", msg);
        result.put("operationLog", operationLogInfo);
        result.put("result", JSONArray.fromObject(users));
        return result.toString();
    }

    @RequestMapping(value = "/getlallusers")
    @ResponseBody
    @ArchivesLog(operationType = "getlallusers", operationName = "查询所有用户信息")
   public String GetAllUsers() {

        Specification<User> specification = (root, criteriaQuery, criteriaBuilder) -> {

            // List<Predicate> predicates = new ArrayList<>();

            Path<Integer> usertype = root.get("type");


            Predicate predicate = criteriaBuilder.equal(usertype.as(Integer.class), 0);


            //return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            return predicate;
        };


        Iterable<User> users = userRepository.findAll(specification);


        return JSONArray.fromObject(users).toString();
    }


    @RequestMapping(value = "/getusersByClassLevel")
    @ResponseBody
    @ArchivesLog(operationType = "getusersByClassLevel", operationName = "根据密级查询所有用户信息")
    public String GetUsersByClassLevel(@RequestParam(value = "cl") Integer cl) {

        Specification<User> specification = (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            Path<Integer> usertype = root.get("type");


            Predicate predicate = criteriaBuilder.equal(usertype.as(Integer.class), 0);

            Path<Integer> userClassLevel = root.get("permissionLevel");

            Predicate predicate1 = criteriaBuilder.greaterThanOrEqualTo(userClassLevel.as(Integer.class), cl);

            predicates.add(predicate);
            predicates.add(predicate1);

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            //return predicate;
        };


        Iterable<User> users = userRepository.findAll(specification);


        return JSONArray.fromObject(users).toString();
    }


    @RequestMapping(value = "/checkPwdDate")
    @ResponseBody
    public String checkPwdDate() {

        User user = getUser();

        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(null == user.getLastUpdatepwd()?new Date():user.getLastUpdatepwd());
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        int year1 = aCalendar.get(Calendar.YEAR);
        aCalendar.setTime(new Date());
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        int year2 = aCalendar.get(Calendar.YEAR);
        int days = day2 - day1;

        result = new JSONObject();

        if (year2 > year1) {
            result.put("msg", "change");
        } else {
            if (days > 7) {
                result.put("msg", "change");
            } else {
                result.put("msg", "no");
            }
        }
        return JSONObject.fromObject(result).toString();
    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/sign_in")
    @ResponseBody
    @ArchivesLog(operationType = "sign_in", operationName = "登录系统")
    String sign_in(@RequestParam("username") String username,
                   @RequestParam("password") String password, HttpSession session) {

        result = new JSONObject();

        Subject currentUser = SecurityUtils.getSubject();

        Md5Hash mh = new Md5Hash(password, username, 2);
        UsernamePasswordToken token = new UsernamePasswordToken(username, mh.toString());

        token.setRememberMe(true);


        try {

            currentUser.login(token);
            User user = (User) currentUser.getPrincipal();
//            SecurityUtils.getSubject().getSession().setTimeout(1000);

            if(user.getPwdErrorTime()>=5) {
                long nd = 1000 * 24 * 60 * 60;
                long nh = 1000 * 60 * 60;
                long nm = 1000 * 60;
                // long ns = 1000;
                // 获得两个时间的毫秒时间差异
                long diff = new Date().getTime()-user.getLastPwdLockTime().getTime();
                // 计算差多少天
                //long day = diff / nd;
                // 计算差多少小时
                //long hour = diff % nd / nh;
                long min = diff % nd % nh / nm;

                if(min<=10){
                    msg = "账号已经锁定，请10分钟后重试!";
                }else
                {
                    user.setPwdErrorTime(0);
                    userRepository.save(user);

                    session.setAttribute("user", user);

                    UpdateStorageInfo(user, session);

                    msg = SUCCESS;
                }
            }else {
                SecurityUtils.getSubject().getSession().setTimeout(timeOut*1000);


                session.setAttribute("user", user);

                UpdateStorageInfo(user, session);

                msg = SUCCESS;
            }
        } catch (UnknownAccountException e) {
            msg = "账户不存在!";
        } catch (IncorrectCredentialsException e) {
            User user = userRepository.findByUsername(username);
            int errorTime=user.getPwdErrorTime();
            user.setPwdErrorTime(errorTime+1);
            if(user.getPwdErrorTime()>=5){
                msg = "账户密码错误超过5次，请十分钟后重试!";
                user.setLastPwdLockTime(new Date());
                userRepository.save(user);
            }else {
                msg = "账户密码错误"+user.getPwdErrorTime()+"!";
            }
        } catch (DisabledAccountException e) {
            msg = "账户状态异常!";
        } catch (AuthenticationException e) {
            logger.error("认证失败 : " + e.getMessage());
            msg = "登录异常!";
        }

       // operationLogInfo = "用户【" + getUser().getRealName() + "】登录系统";

        overDate = DateUtil.parseStrToDate(overDateStr, "yyyy-MM-dd", null);
        if (overDate.before(new Date())){
            msg = "系统密钥已过期";
        }

        result.put("operationLog", "用户【"+username+"】登录系统");
        result.put("msg", msg);
        return JSONObject.fromObject(result).toString();

    }

    @RequestMapping(value = "/getAuditByClassify")
    @ResponseBody
    public String getAuditByClassify(Integer classify){
        List<User> auditUserByClassify = userRepository.findAuditUserByClassify(classify);

        return JSONArray.fromObject(auditUserByClassify).toString();
    }

    @RequestMapping(value = "/test")
    @ResponseBody
    public String test(HttpServletRequest request){

        JSONObject resJson = new JSONObject();

        try {
            String tStrDN = request.getHeader("dnname"); //用户证书主题
            if(tStrDN==null){
                tStrDN = "";
            }else{
                tStrDN = new String(tStrDN.getBytes("ISO8859-1"),"GB2312"); //由于DN中可能存在中文,所以对DN转码
            }
            String tStrSN = request.getHeader("serialnumber");//用户证书序列号
            if(tStrSN == null){
                tStrSN = "";
            }
            String tStrIssuerDN = request.getHeader("issuerdn");//用户证书颁发者主题
            if(tStrIssuerDN == null){
                tStrIssuerDN = "";
            }else{
                tStrIssuerDN = new String(tStrIssuerDN.getBytes("ISO8859-1"),"GB2312"); //由于IssuerDN中可能存在中文,所以对DN转码
            }
            String tStrNotBefore = request.getHeader("notbefore");//证书起始有效期
            if(tStrNotBefore == null){
                tStrNotBefore = "";
            }
            String tStrNotAfter = request.getHeader("notafter");//证书终止有效期
            if(tStrNotAfter == null){
                tStrNotAfter = "";
            }
            String tStrCertCode = request.getHeader("certinfo");//证书Baes64编码
            if(tStrCertCode == null){
                tStrCertCode = "";
            }
            String tStrClientIP = request.getHeader("clientip");//取用户客户端IP
            if(tStrClientIP == null){
                tStrClientIP = "";
            }
            String tStrPrivilege = request.getHeader("privilege");//取用户权限
            if(tStrPrivilege==null){
                tStrPrivilege = "";
            }else{
                tStrPrivilege = new String(tStrPrivilege.getBytes("ISO8859-1"),"GB2312"); //由于权限中可能存在中文,所以对权限信息转码
            }
            String tStrServicelist = request.getHeader("servicelist");//取用户权限
            if(tStrServicelist==null){
                tStrServicelist = "";
            }else{
                tStrServicelist = new String(tStrServicelist.getBytes("ISO8859-1"),"GB2312"); //用户可访问应用列表
            }
            String tStrResidenterCardNumber = request.getHeader("ResidenterCardNumber");//扩展中的身份证号
            if(tStrNotAfter == null){
                tStrNotAfter = "";
            }
            String tStrEmail = request.getHeader("email");//扩展中的邮箱
            if(tStrNotAfter == null){
                tStrNotAfter = "";
            }
            String tStrIP = request.getHeader("SubjectAltName.IP");//扩展中的IP
            if(tStrNotAfter == null){
                tStrNotAfter = "";
            }

            resJson.put("tStrDN",tStrDN);
            resJson.put("tStrSN",tStrSN);
            resJson.put("tStrIssuerDN",tStrIssuerDN);
            resJson.put("tStrNotBefore",tStrNotBefore);
            resJson.put("tStrNotAfter",tStrNotAfter);
            resJson.put("tStrCertCode",tStrCertCode);
            resJson.put("tStrClientIP",tStrClientIP);
            resJson.put("tStrPrivilege",tStrPrivilege);
            resJson.put("tStrServicelist",tStrServicelist);
            resJson.put("tStrResidenterCardNumber",tStrResidenterCardNumber);
            resJson.put("tStrEmail",tStrEmail);
            resJson.put("tStrIP",tStrIP);




        }catch (Exception e){
            logger.error(e.getMessage());
        }

        return resJson.toString();
    }

    @RequestMapping(value = "/UniformLogin")
    @ResponseBody
    @ArchivesLog(operationType = "UniformLogin", operationName = "认证网关登录")
   public ModelAndView UniformLogin(HttpServletRequest request, HttpSession session) {


        ModelAndView mav = new ModelAndView("index");
        result = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            String tStrDN = request.getHeader("dnname"); //用户证书主题
            if(tStrDN==null){
                tStrDN = "";
            }else{
                tStrDN = new String(tStrDN.getBytes("ISO8859-1"),"GB2312"); //由于DN中可能存在中文,所以对DN转码
                String[] dnArr = tStrDN.split(",");

                for (String dn:dnArr) {
                    String[] key = dn.split("=");
                    object.put(key[0],key[1]);
                }
            }
            String tStrSN = request.getHeader("serialnumber");//用户证书序列号
            if(tStrSN == null){
                tStrSN = "";
            }
            String tStrIssuerDN = request.getHeader("issuerdn");//用户证书颁发者主题
            if(tStrIssuerDN == null){
                tStrIssuerDN = "";
            }else{
                tStrIssuerDN = new String(tStrIssuerDN.getBytes("ISO8859-1"),"GB2312"); //由于IssuerDN中可能存在中文,所以对DN转码
            }
            String tStrNotBefore = request.getHeader("notbefore");//证书起始有效期
            if(tStrNotBefore == null){
                tStrNotBefore = "";
            }
            String tStrNotAfter = request.getHeader("notafter");//证书终止有效期
            if(tStrNotAfter == null){
                tStrNotAfter = "";
            }
            String tStrCertCode = request.getHeader("certinfo");//证书Baes64编码
            if(tStrCertCode == null){
                tStrCertCode = "";
            }
            String tStrClientIP = request.getHeader("clientip");//取用户客户端IP
            if(tStrClientIP == null){
                tStrClientIP = "127.0.0.1";
            }
            session.setAttribute("clientIp",tStrClientIP);
            String tStrPrivilege = request.getHeader("privilege");//取用户权限
            if(tStrPrivilege==null){
                tStrPrivilege = "";
            }else{
                tStrPrivilege = new String(tStrPrivilege.getBytes("ISO8859-1"),"GB2312"); //由于权限中可能存在中文,所以对权限信息转码
            }
            String tStrServicelist = request.getHeader("servicelist");//取用户权限
            if(tStrServicelist==null){
                tStrServicelist = "";
            }else{
                tStrServicelist = new String(tStrServicelist.getBytes("ISO8859-1"),"GB2312"); //用户可访问应用列表
            }
            String tStrResidenterCardNumber = request.getHeader("ResidenterCardNumber");//扩展中的身份证号
            if(tStrNotAfter == null){
                tStrNotAfter = "";
            }
            String tStrEmail = request.getHeader("email");//扩展中的邮箱
            if(tStrNotAfter == null){
                tStrNotAfter = "";
            }
            String tStrIP = request.getHeader("SubjectAltName.IP");//扩展中的IP
            if(tStrNotAfter == null){
                tStrNotAfter = "";
            }

            logger.info("tStrDN:"+tStrDN);
            logger.info("tStrSN:"+tStrSN);
            logger.info("tStrIssuerDN:"+tStrIssuerDN);
            logger.info("tStrNotBefore:"+tStrNotBefore);
            logger.info("tStrNotAfter:"+tStrNotAfter);
            logger.info("tStrCertCode:"+tStrCertCode);
            logger.info("tStrClientIP:"+tStrClientIP);
            logger.info("tStrPrivilege:"+tStrPrivilege);
            logger.info("tStrServicelist:"+tStrServicelist);
            logger.info("tStrResidenterCardNumber:"+tStrResidenterCardNumber);
            logger.info("tStrEmail:"+tStrEmail);
            logger.info("tStrIP:"+tStrIP);
            String idNo = object.getString("T");
            logger.info("IDNO:"+object.getString("T"));

            if (null == idNo || idNo.equals("")){
                mav = new ModelAndView("login");
                logger.error("网关授权认证未通过,证件号码为空");
                return mav;
            }

            User user1 = userRepository.findByIdNo(idNo);


            if (null == user1){
                mav = new ModelAndView("login");
                logger.error("网关授权认证未通过,未查找与【"+tStrResidenterCardNumber+"】到匹配的用户信息");
                return mav;
            }
            overDate = DateUtil.parseStrToDate(overDateStr, "yyyy-MM-dd", null);
            if (overDate.before(new Date())){
                mav = new ModelAndView("login");
                logger.error("系统密钥已过期");
                mav.addObject("error","系统密钥已过期");
                return mav;
            }

            Subject currentUser = SecurityUtils.getSubject();

            UsernamePasswordToken token = new UsernamePasswordToken(user1.getUsername(), user1.getPassword());

            token.setRememberMe(true);


            currentUser.login(token);
            User user = (User) currentUser.getPrincipal();
            SecurityUtils.getSubject().getSession().setTimeout(timeOut*1000);

            session.setAttribute("user", user);

            UpdateStorageInfo(user, session);
            msg = SUCCESS;
        } catch (UnknownAccountException e) {
            msg = "账户不存在!";
        } catch (IncorrectCredentialsException e) {
            msg = "账户密码错误!";
        } catch (DisabledAccountException e) {
            msg = "账户状态异常!";
        } catch (AuthenticationException e) {
            logger.error("认证失败 : " + e.getMessage());
            msg = "登录异常!";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return mav;

    }

    private void UpdateStorageInfo(User user,HttpSession session) {
        try {
            StorageEntity storageEntity = new StorageEntity();

            List<StorageEntity> storageEntityList = (List<StorageEntity>) storageRepository.findAll();

            if (storageEntityList == null || storageEntityList.size() <= 0) {
                storageEntity = new StorageEntity();
                storageEntity.setId(1);
                storageEntity.setTotalAmount("1TB");
                storageEntity.setAlertAmount("900GB");
                storageEntity.setLogSaveTime("6");
            } else {
                storageEntity = storageEntityList.get(0);
            }

            double d0 = storageRepository.GetDataBaseSpace();
            double d1 = storageRepository.GetFileSpaceByType(1);
            double d2 = storageRepository.GetFileSpaceByType(2);
            double d3 = storageRepository.GetFileSpaceByType(3);
            double d4 = storageRepository.GetFileSpaceByType(4);
            double d5 = storageRepository.GetFileSpaceByType(5);
            double d6 = storageRepository.GetLogSpace();

            storageEntity.setDbAmount(GetStorageDesc(d0));

            storageEntity.setProjectAmount(GetStorageDesc(d1));
            storageEntity.setAnliAmount(GetStorageDesc(d2));
            storageEntity.setZiliaoAmount(GetStorageDesc(d3));
            storageEntity.setExpertAmount(GetStorageDesc(d4));
            storageEntity.setGongaoAmount(GetStorageDesc(d5));
            storageEntity.setLogAmount(GetStorageDesc(d6));

           // storageEntity.setCurrentUsed(GetStorageDesc(d0 + d1 + d2 + d3 + d4 + d5 + d6));
            storageEntity.setCurrentUsed(GetStorageDesc(d6));
//
            storageRepository.save(storageEntity);

            if (storageEntity.getCurrentUsed().equals(storageEntity.getAlertAmount())) {

                //List<User>  userList=userRepository.findUsersByTypeId(1L);
                //for(User user1:userList) {

                synchronized (spaceAlertLock) {
                    if (user.getType() == 1 && session.getAttribute("spacealert" + user.getUsername()) == null) {

                        NotifyEntity notify = new NotifyEntity();
                        notify.setContent("存储空间已经达到预警值：" + storageEntity.getAlertAmount());
                        notify.setOperateTime(DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYYMMDD_HH_MI));
                        notify.setApprover(user.getUsername());
                        notify.setPersonal(user.getUsername());
                        notify.setStatus(0);
                        notifyRepository.save(notify);
                        session.setAttribute("spacealert" + user.getUsername(), "1");
                    }
                }
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
    }

    private String GetStorageDesc(double amounts) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (amounts < 1000) {
            return df.format(amounts) + "Bytes";
        } else if (amounts > 1000 && amounts < 1000000) {
            return df.format(amounts / 1000) + "KB";
        } else if (amounts >= 1000000 && amounts < 1000000000) {
            return df.format(amounts / 1000000) + "MB";
        } else if (amounts >= 1000000000 && amounts < 1000000000000d) {
            return df.format(amounts / 1000000000) + "GB";
        } else {
            return df.format(amounts / 1000000000000d) + "TB";
        }
    }

    @RequestMapping(value = "/sign_out")
    @ArchivesLog(operationType = "sign_out", operationName = "登出")
    public String sign_out() {

        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";

    }

    protected User getUser() {
        Subject subject = SecurityUtils.getSubject();
        return (User) subject.getPrincipal();
    }

}
