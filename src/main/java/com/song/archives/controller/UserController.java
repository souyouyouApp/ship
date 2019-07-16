package com.song.archives.controller;

import com.song.archives.aspect.ArchivesLog;
import com.song.archives.dao.NotifyRepository;
import com.song.archives.dao.OperationRepository;
import com.song.archives.dao.StorageRepository;
import com.song.archives.dao.UserRepository;
import com.song.archives.model.*;
import com.song.archives.utils.DateUtil;
import com.song.archives.utils.LoggerUtils;
import com.song.archives.utils.MySQLDatabaseBackup;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.Logical;
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

import static com.song.archives.utils.LoggerUtils.LOGGER_RESULT;

/**
 * Created by souyouyou on 2018/2/1.
 */

@Controller
@RequestMapping("/")
public class UserController {

    Logger logger = Logger.getLogger(UserController.class);

    public static String SUCCESS = "success";

    private String msg = "failed";

    private JSONObject result = new JSONObject();

    @Autowired
    HttpServletRequest request;

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
    @Value("${mysqldump.path}")
    private String mysqlDumpath;

    private Date overDate;

    private static Object spaceAlertLock=new Object();

    /**
     * 视图跳转
     *
     * @param viewName
     * @return
     */
    @ArchivesLog(operationType = "viewName", description = "视图跳转" , writeFlag = false)
    @RequestMapping(value = "/{viewName}")
    String viewJump(@PathVariable("viewName") String viewName) {
        return viewName;
    }

    @ArchivesLog(operationType = "login", description = "登录页面")
    @RequestMapping(value = "/")
    String login() {
        return "login";
    }

    /**
     * 日志页面
     *
     * @return
     */
    @ArchivesLog(operationType = "logList", description = "日志页面",writeFlag = false)
    @RequestMapping(value = "logList")
    String logList() {
        return "log/list";
    }

    @ArchivesLog(operationType = "userInfo", description = "用户基本信息")
    @RequestMapping(value = "userInfo")
    public ModelAndView userInfo(@RequestParam(value = "uId") Long uId) {
        ModelAndView mav = new ModelAndView("member/info");
        User userInfo = userRepository.findOne(uId);
        mav.addObject("userInfo",userInfo);
        LoggerUtils.setLoggerSuccess(request);
        return mav;
    }



    @ResponseBody
    @RequestMapping(value = "/backupLog")
    @ArchivesLog(description = "备份日志",operationType = "backupLog",descFlag = true)
    public String exportMysql(){
        result = new JSONObject();
        boolean backFlag = false;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



        String backUpFileName  = new SimpleDateFormat("yyyy-MM-dd").format(new Date())+".sql";
        try {
            backFlag = MySQLDatabaseBackup.exportDatabaseTool(mysqlDumpath,"localhost","root","root",savePath,backUpFileName ,"zscq");
            msg = SUCCESS;

            result.put("path",savePath+"/"+backUpFileName);
        } catch (InterruptedException e) {
            msg = "Error";
            logger.error(e.getMessage());
        }

        LoggerUtils.setLoggerReturn(request,msg);

//        operationRepository.save(operationLog);

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
    @ArchivesLog(operationType = "logs", description = "查询日志列表", descFlag = true)
    @RequestMapping(value = "logs")
    @ResponseBody
    @RequiresRoles(value = {"securitor","comptroller","superadmin"},logical = Logical.OR)
    String logs(@RequestParam(value = "pageIndex", defaultValue = "0") Integer page,
                @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                @RequestParam(value = "userName", defaultValue = "") String userName,
                @RequestParam(value = "description", defaultValue = "") String description,
                @RequestParam(value = "startDate", defaultValue = "") String startDate,
                @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        page = page - 1;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);


        Specification<OperationEntity> specification = (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (!userName.equals("")) {
                predicates.add(criteriaBuilder.like(root.get("userName"), "%" + userName + "%"));
            }

            if (!description.equals("")) {
                predicates.add(criteriaBuilder.like(root.get("description"), "%" + description + "%"));
            }

            if (!startDate.equals("") && !endDate.equals("")){
                predicates.add(criteriaBuilder.between(root.get("beginTime"),startDate,endDate));
            }

//            predicates.add(criteriaBuilder.notEqual(root.get("operationDescrib"),""));


            //审计员查看管理员和保密员的操作日志
            if (getUser().getUsername().equals("comptroller")){
                CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("userName"));
                in.value("securitor");
                in.value("administrator");
                in.value("安全保密员");
                in.value("系统管理员");
                predicates.add(in);
            }

            //保密员查看用户和审计员的日志
            if (getUser().getUsername().equals("securitor")){

                CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("userName"));
                in.value("administrator");
                in.value("securitor");
                in.value("系统管理员");
                in.value("安全保密员");

                predicates.add(in.not());

            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));

        };

        Page<OperationEntity> operationLogs = operationRepository.findAll(specification, pageable);
        LoggerUtils.setLoggerSuccess(request);
        result.put("msg", msg);
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
    @ArchivesLog(operationType = "operateUser", description = "修改账户状态")
    @RequestMapping(value = "operateUser")
    @ResponseBody
    String operateUser(@RequestParam(value = "uid") Long id,
                       @RequestParam(value = "status") Boolean status) {

        result = new JSONObject();

        try {

            User user = userRepository.findOne(id);
            user.setAvailable(status);
            userRepository.save(user);

            LoggerUtils.setLoggerSuccess(request);
            msg = SUCCESS;

        } catch (Exception e) {
            logger.error("修改账户状态:" + e.getMessage());
            LoggerUtils.setLoggerFailed(request);
            msg = "Exception";
        }

        result.put("msg", msg);

        return result.toString();
    }

    @RequestMapping(value = "updatePassword")
    @ResponseBody
    @ArchivesLog(operationType = "updatePassword", description = "更新用户密码",descFlag = true)
    public String updatePwd(@RequestParam(value = "oldPwd")String oldPwd,
                            @RequestParam(value = "newPwd")String newPwd,
                            @RequestParam(value = "repeatPwd")String repeatPwd){

        result = new JSONObject();
        String passWord = new SimpleHash(Md5Hash.ALGORITHM_NAME, oldPwd, getUser().getUsername(), 2).toHex();

        if (!getUser().getPassword().equals(passWord)){
            msg = "原始密码错误";
        }else {

            if (!newPwd.equals(repeatPwd)){
                msg = "新密码与确认密码不一致";
            }else {

                User user = getUser();
                user.setPassword(new SimpleHash(Md5Hash.ALGORITHM_NAME, repeatPwd, getUser().getUsername(), 2).toHex());
                user.setLastUpdatepwd(new Date());
                userRepository.save(user);
                msg = SUCCESS;
            }

        }

        if (msg.equals(SUCCESS)){
            LoggerUtils.setLoggerSuccess(request);
        }else {
            LoggerUtils.setLoggerFailed(request,msg);
        }
        result.put("msg", msg);

        return result.toString();

    }

    /**
     * 新建用户
     *
     * @param user
     * @return
     */
    @ArchivesLog(operationType = "saveUser", description = "新建用户")
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
            }else {
                user.setLastUpdatepwd(new Date());
                userRepository.save(user);
                msg = "success";
            }

        } catch (Exception e) {
            msg = "新建用户异常";
        }

        if (msg.equals(SUCCESS)){
            LoggerUtils.setLoggerSuccess(request);
        }else {
            LoggerUtils.setLoggerFailed(request,msg);
        }
        result.put("msg", msg);
        return result.toString();
    }


    @ArchivesLog(operationType = "updateUser", description = "更新用户信息")
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
            user.setAvailable(true);
            user.setRoles(user.getRoles().size()==0?oldUser.getRoles():user.getRoles());


            userRepository.save(user);
            msg = SUCCESS;
        } catch (Exception e) {
            msg = "更新用户信息异常";
        }
        if (msg.equals(SUCCESS)){
            LoggerUtils.setLoggerSuccess(request);
        }else {
            LoggerUtils.setLoggerFailed(request,msg);
        }
        result.put("msg", msg);
        return result.toString();
    }


    @ArchivesLog(operationType = "getUserInfo", description = "获取用户信息")
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
    @ArchivesLog(operationType = "deleteUserByIds", description = "删除勾选用户")
    @RequestMapping(value = "deleteUserByIds")
    @ResponseBody
    String deleteUserByIds(Long[] uids) {

        try {
            if (uids != null && uids.length > 0) {

                for (Long uid : uids) {
                    User userObj = userRepository.findOne(uid);
                    userRepository.delete(userObj);
                }
            }

            msg = SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage());
            msg = "删除用户异常";
        }


        if (msg.equals(SUCCESS)){
            LoggerUtils.setLoggerSuccess(request);
        }else {
            LoggerUtils.setLoggerFailed(request,msg);
        }
        result.put("msg", msg);
        return result.toString();
    }


    /**
     * 用户列表
     *
     * @return
     */
    @ArchivesLog(operationType = "memberList", description = "用户列表",writeFlag = false)
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
    @ArchivesLog(operationType = "users", description = "查询用户列表",descFlag = true)
    @RequiresRoles(value = {"administrator","securitor"},logical = Logical.OR)
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

        result.put("msg", msg);
        LoggerUtils.setLoggerSuccess(request);
        result.put("result", JSONArray.fromObject(users));
        return result.toString();
    }

    @RequestMapping(value = "/getlallusers")
    @ResponseBody
    @ArchivesLog(operationType = "getlallusers", description = "查询所有用户信息")
   public String GetAllUsers() {

        Specification<User> specification = (root, criteriaQuery, criteriaBuilder) -> {

            // List<Predicate> predicates = new ArrayList<>();

            Path<Integer> usertype = root.get("type");


            Predicate predicate = criteriaBuilder.equal(usertype.as(Integer.class), 0);


            //return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            return predicate;
        };


        Iterable<User> users = userRepository.findAll(specification);

        LoggerUtils.setLoggerSuccess(request);
        return JSONArray.fromObject(users).toString();
    }


    @RequestMapping(value = "/getusersByClassLevel")
    @ResponseBody
    @ArchivesLog(operationType = "getusersByClassLevel", description = "根据密级查询所有用户信息")
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
    @ArchivesLog(operationType = "sign_in", description = "登录系统",descFlag = true)
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
                user.setPwdErrorTime(0);

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
            msg = "账户认证失败!";
        }


       // operationLogInfo = "用户【" + getUser().getRealName() + "】登录系统";

        overDate = DateUtil.parseStrToDate(overDateStr, "yyyy-MM-dd", null);
        if (overDate.before(new Date())){
            msg = "系统密钥已过期";
        }
        if (!msg.equals(SUCCESS)){
            request.setAttribute(LOGGER_RESULT,"【"+username+"】"+msg);
        }else {
            LoggerUtils.setLoggerSuccess(request);
        }
        result.put("msg", msg);
        return JSONObject.fromObject(result).toString();

    }

    @RequestMapping(value = "/getAuditByClassify")
    @ResponseBody
    public String getAuditByClassify(Integer classify){
        List<User> auditUserByClassify = userRepository.findAuditUserByClassify(classify,getUser().getId());

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
    @ArchivesLog(operationType = "UniformLogin", description = "认证网关登录",descFlag = true)
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
                msg = "网关授权认证未通过,证件号码为空";
                return mav;
            }

            User user1 = userRepository.findByIdNo(idNo);


            if (null == user1){
                mav = new ModelAndView("login");
                logger.error("网关授权认证未通过,未查找与【"+tStrResidenterCardNumber+"】到匹配的用户信息");
                msg = "网关授权认证未通过,未查找与【"+tStrResidenterCardNumber+"】到匹配的用户信息";
                return mav;
            }
            overDate = DateUtil.parseStrToDate(overDateStr, "yyyy-MM-dd", null);
            if (overDate.before(new Date())){
                mav = new ModelAndView("login");
                logger.error("系统密钥已过期");
                msg = "系统密钥已过期";
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
        if (!msg.equals(SUCCESS)){
            request.setAttribute(LOGGER_RESULT,msg);
        }else {
            LoggerUtils.setLoggerSuccess(request);
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
    @ArchivesLog(operationType = "sign_out", description = "退出系统",descFlag = true)
    public String sign_out() {

        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        LoggerUtils.setLoggerSuccess(request);
        return "login";

    }

    protected User getUser() {
        Subject subject = SecurityUtils.getSubject();
        return (User) subject.getPrincipal();
    }

}
