package com.song.archives.aspect;

import com.song.archives.dao.OperationRepository;
import com.song.archives.model.OperationLog;
import com.song.archives.model.User;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by souyouyou on 2018/2/3.
 * 记录用户操作日志
 */
@Aspect
@Component
public class LogAspect {

    private static Set<String> viewNames = new HashSet<>();

    static {

        viewNames.add("sign_in");
        viewNames.add("sign_out");
        viewNames.add("createZiliao");
        viewNames.add("delData");
        viewNames.add("uploadAatachment");
        viewNames.add("createAnli");
        viewNames.add("delAnli");
        viewNames.add("createAnnounce");
        viewNames.add("delAnnounce");
        viewNames.add("saveUser");
        viewNames.add("updateUser");
        viewNames.add("deleteUserByIds");
        viewNames.add("updatePassword");
        viewNames.add("saveRole");
        viewNames.add("deleteRoleByIds");
        viewNames.add("savePermission");
        viewNames.add("deletePermissionByIds");
        viewNames.add("saveRoles");
        viewNames.add("savePermissions");
        viewNames.add("saveMenu");
        viewNames.add("deleteMenu");
        viewNames.add("auditFileById");

        viewNames.add("SaveCyDanwei");
        viewNames.add("GetCyDanweiList");
        viewNames.add("DeleteCyDanweiList");
        viewNames.add("ReviewProjectFiles");
        viewNames.add("BatchDownProjectFiles");
        viewNames.add("LoadAlertList");
        viewNames.add("LoadProjectProcess");
       // viewNames.add("CreateProject");
        //viewNames.add("ReviewProject");
        //viewNames.add("ProjectList");
       // viewNames.add("ProjectDetail");
        viewNames.add("LoadJianDingList");
        viewNames.add("SaveBaojiang");
        viewNames.add("SaveHuojiang");
        viewNames.add("LoadNiBaoJiangList");
        viewNames.add("LoadHuoJiangList");
        viewNames.add("LoadReceiveLogList");
        viewNames.add("LoadYantaoLogList");
        viewNames.add("LoadCuishouLogList");
        viewNames.add("GetProjectCount");
        viewNames.add("DeleteMoneyByIdsAndType");
        viewNames.add("DeleteJdByIds");
        viewNames.add("DeleteBjByIds");
        viewNames.add("GetFileTypeByPhaseId");
        viewNames.add("DeleteHjByIds");
        viewNames.add("DeleteYantaoByd");
        viewNames.add("DeleteFilesByIds");
        viewNames.add("DeleteProjectByIds");
        viewNames.add("SaveProject");
        viewNames.add("LoadProjectPhases");
        viewNames.add("UpdateProject");
        viewNames.add("GetFilesById");
        viewNames.add("LoadProjectFiles");
        viewNames.add("SaveJianDing");
        viewNames.add("SaveYiShou");
        viewNames.add("SaveYtRecored");
        viewNames.add("SaveCuiShou");
        viewNames.add("UpdateFilesClassId");
        viewNames.add("UploadPaper");
        viewNames.add("UpLoadYtFiles");
        viewNames.add("ImportProjectFiles");
        viewNames.add("UpdateSpaceAmount");
        viewNames.add("UpdateAlertAmount");
        viewNames.add("UpLoadProjectFiles");


        viewNames.add("createLow");
        viewNames.add("lows");
        viewNames.add("delLow");

        viewNames.add("delExpert");
        viewNames.add("createExpert");
        viewNames.add("importExpert");
        viewNames.add("experts");
        viewNames.add("logs");


    }

    private static Object OperationLogLock=new Object();

    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private OperationLog operationLog;

    User user = null;


    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Pointcut("execution(* com.song.archives.controller..*(..))")
    public void operation() {
    }

    @Before("operation()")
    public void before(JoinPoint joinPoint) throws Throwable {
        operationLog.setId(null);
        operationLog.setOperationBefore(null);
        operationLog.setOperationAfter(null);
        user = (User) getHttpServletRequest().getSession().getAttribute("user");
        Object clientIp = getHttpServletRequest().getSession().getAttribute("clientIp");

//        operationLog = new OperationLog();
        operationLog.setOperationStartTime(dateFormat.format(System.currentTimeMillis()));
        operationLog.setOperationInput(JSONObject.fromObject(getHttpServletRequest().getParameterMap()).toString());

        if (joinPoint.getSignature().getName().equals("sign_out")) {
            operationLog.setOperationUserId(user.getId());
            operationLog.setOperationUserName(user.getUsername());
            operationLog.setOperationIp(clientIp==null?null:clientIp.toString());
            operationLog.setOperationDescrib("用户【" + user.getRealName() + "】退出系统");
            operationLog.setOperationResult("成功");
        }

    }

    @After("operation()")
    public void after(JoinPoint joinPoint) {

        user = (User) getHttpServletRequest().getSession().getAttribute("user");

        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = null;
        try {
            targetClass = Class.forName(targetName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method[] methods = targetClass.getDeclaredMethods();
        String operationName = "", operationType = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs != null && clazzs.length == arguments.length && method.getAnnotation(ArchivesLog.class) != null) {
                    operationName = method.getAnnotation(ArchivesLog.class).operationName();
                    operationType = method.getAnnotation(ArchivesLog.class).operationType();
                    break;
                }
            }
        }

        operationLog.setOperationEndTime(dateFormat.format(System.currentTimeMillis()));
        operationLog.setOperationName(operationName);
        operationLog.setOperationType(operationType);

        if (user != null) {
            operationLog.setOperationUserId(user.getId());
            operationLog.setOperationUserName(user.getUsername());
        }

//        if (viewNames.contains(methodName)) {
//            operationLog.setOperationDescrib("跳转页面:" + operationName);
//        }


    }

    @AfterReturning(returning = "ret", pointcut = "operation()")
    public void afterReturn(JoinPoint joinPoint, Object ret) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        try {


            Object clientIp = getHttpServletRequest().getSession().getAttribute("clientIp");

            if (clientIp != null){
                operationLog.setOperationIp(clientIp.toString());
            }

            if (methodName.equals("sign_out")) {

                if (null == ret){
                    operationLog.setOperationOutPut("");
                }else {
                    operationLog.setOperationOutPut(ret.toString());
                }
            } else if (viewNames.contains(methodName)){

                JSONObject retObj = JSONObject.fromObject(ret);

                if (retObj != null && retObj.containsKey("operationLog")) {
                    operationLog.setOperationDescrib(retObj.getString("operationLog"));
                    operationLog.setOperationResult(retObj.getString("msg").equals("success")?"成功":"失败");
                    if (viewNames.contains(methodName)){
                        operationLog.setOperationOutPut(JSONObject.fromObject(ret).toString());
                    }

                }
            }

            synchronized (operationLog) {
                if (viewNames.contains(methodName)){
                    operationRepository.save(operationLog);
                }
            }

        }catch (Exception e){
            logger.error("异常方法："+methodName);
            logger.error("记录日志 ERROR:" + e.getMessage());
        }

    }


    public HttpServletRequest getHttpServletRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        return request;
    }

    public String getRemortIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }
}
