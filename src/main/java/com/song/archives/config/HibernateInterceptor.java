package com.song.archives.config;

import com.song.archives.model.*;
import com.song.archives.utils.LoggerUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.PropertySetStrategy;
import org.apache.tomcat.jni.FileInfo;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HibernateInterceptor extends EmptyInterceptor {

    private HashMap<String, String> proMap = new HashMap<>();
    private HashMap<String, String> dataMap = new HashMap<>();
    private HashMap<String, String> anliMap = new HashMap<>();
    private HashMap<String, String> lowsMap = new HashMap<>();
    private HashMap<String, String> expertMap = new HashMap<>();
    private HashMap<String, String> announceMap = new HashMap<>();
    private HashMap<String, String> userMap = new HashMap<>();

    {
        //项目
        proMap.put("proNo","项目编号");
        proMap.put("proName","项目名称");
        proMap.put("proFrom","项目来源");
        //资料库
        dataMap.put("typeId","类别");
        dataMap.put("title","标题");
        dataMap.put("author","作者");
        dataMap.put("publishDate","发布时间");
        dataMap.put("ziliaoFrom","资料来源");
        dataMap.put("ziliaoContent","资料摘要");
        dataMap.put("classificlevelId","密级");
        dataMap.put("creator","创建人");
        dataMap.put("createTime","创建时间");
        dataMap.put("keyword","关键词");

        //案例库
        anliMap.put("typeId","类别");
        anliMap.put("title","标题");
        anliMap.put("classificlevelId","密级");
        anliMap.put("creator","创建人");
        anliMap.put("createTime","创建时间");
        anliMap.put("content","案例摘要");
        anliMap.put("nationality","国别");
        anliMap.put("caseName","案例名称");
        anliMap.put("closeTime","结案时间");
        anliMap.put("finalAppeal","终审法院");
        anliMap.put("projectName","项目名称");
        anliMap.put("projectSource","项目来源");
        anliMap.put("principal","主要负责人");
        anliMap.put("participant","参与人");
        anliMap.put("judicialStatus","状态");
//        anliMap.put("type","类型");

        //法律法规库
        lowsMap.put("typeId","类别");
        lowsMap.put("type","类型");
        lowsMap.put("publishDept","发布部门");
        lowsMap.put("publishTime","发布时间");
        lowsMap.put("keyword","关键词");
        lowsMap.put("content","摘要");
        lowsMap.put("classificlevelId","密级");
        lowsMap.put("uploader","上传人");
        lowsMap.put("uploadTime","上传时间");
        lowsMap.put("title","标题");

        //专家库
        expertMap.put("typeId","类别");
        expertMap.put("name","姓名");
        expertMap.put("gender","性别");
        expertMap.put("birth","出生日期");
        expertMap.put("education","学历");
        expertMap.put("sxzhuanye","所属专业");
        expertMap.put("cszhuanye","所属专业");
        expertMap.put("zhiwu","职务");
        expertMap.put("zhicheng","职称");
        expertMap.put("profile","个人简介");
        expertMap.put("szdanwei","所在单位");
        expertMap.put("szbumen","所在部门");
        expertMap.put("sslingyu","所属领域");
        expertMap.put("address","地址");
        expertMap.put("postcode","邮编");
        expertMap.put("mobile","手机号码");
        expertMap.put("phone","联系电话");
        expertMap.put("faxcode","传真");
        expertMap.put("email","邮箱");
        expertMap.put("zjpingjia","最佳评价");
        expertMap.put("pycishu","聘用次数");
        expertMap.put("classiclevelId","密级");
        expertMap.put("createTime","创建时间");
        expertMap.put("idNo","身份证号");
        expertMap.put("remark","备注");

        //内部原地
        announceMap.put("typeId","类别");
        announceMap.put("title","标题");
        announceMap.put("creator","创建人");
        announceMap.put("createTime","创建时间");
        announceMap.put("content","摘要");
        announceMap.put("sponsorDate","发起时间");
        announceMap.put("deadlineDate","截止时间");
        announceMap.put("sponsor","发起人");
        announceMap.put("typeName","类别名称");
        announceMap.put("relatedUserName","相关人员");
        announceMap.put("classificlevelId","密级");


        //用户
        userMap.put("username","用户名");
        userMap.put("mobile","手机号码");
        userMap.put("description","描述");
        userMap.put("positions","职位");
        userMap.put("permissionLevel","密级");
        userMap.put("realName","真实姓名");
        userMap.put("createDate","创建日期");
        userMap.put("idNo","证件号码");
        userMap.put("password","密码");





    }

    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        System.out.println("----------onLoad---------------");

        HttpServletRequest request = getRequest();
        Map saveEntity = new HashMap();
        JSONObject jsonObject = new JSONObject();

        for (int i = 0; i < propertyNames.length; i++) {
            if (propertyNames[i].equals("roles") || propertyNames[i].equals("relatedUserIds") || propertyNames[i].equals("relatedUserName") || (entity instanceof OperationEntity))
                continue;
            try {
                jsonObject.put(propertyNames[i],state[i]);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
//            System.out.println(entity.getClass());
            saveEntity.put(propertyNames[i],state[i]);
//            System.out.println(propertyNames[i]+"----"+state[i]);

        }
        Object o = new Object();
        o = JSONObject.toBean(jsonObject, entity.getClass());
//        if (!(entity instanceof User)){
//
//        }
//        System.out.println(o.toString());

        String desc;

        if (entity instanceof ZiliaoInfoEntity && request.getRequestURI().equals("/createData")){
            desc = "查看资料【"+saveEntity.get("title")+"】,"+o.toString();
        }else if (entity instanceof ProjectInfoEntity && request.getRequestURI().equals("/ProjectDetail")){
            desc = "查看项目【"+saveEntity.get("proName")+"】,"+o.toString();
        }else if (entity instanceof AnliInfoEntity){
            desc = "查看案例【"+saveEntity.get("title")+"】,"+o.toString();
        }else if (entity instanceof LowInfoEntity){
            desc = "查看法律法规【"+saveEntity.get("title")+"】,"+o.toString();
        }else if (entity instanceof ExpertInfoEntity && request.getRequestURI().equals("/expert")){
            desc = "查看专家【"+saveEntity.get("name")+"】,"+o.toString();
        }else if (entity instanceof AnnounceInfoEntity && request.getRequestURI().equals("/createAnnoucePage")){
            desc = "查看公告【"+saveEntity.get("title")+"】,"+o.toString();
        }else if (entity instanceof User && request.getRequestURI().equals("/userInfo")){
            desc = "查看用户【"+saveEntity.get("username")+"】,"+o.toString();
        }else if (entity instanceof FileInfoEntity && (request.getRequestURI().equals("/getFile")||request.getRequestURI().equals("/downLoadFile"))){
            desc = "下载文件【"+saveEntity.get("fileName")+"】";
        }else {
            return super.onLoad(entity, id, state, propertyNames, types);
        }

//        request.setAttribute(LoggerUtils.LOGGER_DESC,request.getAttribute(LoggerUtils.LOGGER_DESC)==null?desc:(request.getAttribute(LoggerUtils.LOGGER_DESC)+desc));

        request.setAttribute(LoggerUtils.LOGGER_DESC,desc);
        return super.onLoad(entity, id, state, propertyNames, types);
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        HttpServletRequest request = getRequest();
        Map saveEntity = new HashMap();
        System.out.println("----------onDelete---------------");

        for (int i = 0; i < propertyNames.length; i++) {
            saveEntity.put(propertyNames[i],state[i]);

        }

        String desc = "";

        if (entity instanceof ZiliaoInfoEntity){
            desc = "删除资料【"+saveEntity.get("title")+"】,"+entity.toString();
        }else if (entity instanceof ProjectInfoEntity){
            desc = "删除项目【"+saveEntity.get("proName")+"】,"+entity.toString();
        }else if (entity instanceof AnliInfoEntity){
            desc = "删除案例【"+saveEntity.get("title")+"】,"+entity.toString();
        }else if (entity instanceof LowInfoEntity){
            desc = "删除法律法规【"+saveEntity.get("title")+"】,"+entity.toString();
        }else if (entity instanceof ExpertInfoEntity){
            desc = "删除专家【"+saveEntity.get("name")+"】,"+entity.toString();
        }else if (entity instanceof AnnounceInfoEntity){
            desc = "删除公告【"+saveEntity.get("title")+"】,"+entity.toString();
        }else if (entity instanceof User){
            desc = "删除用户【"+saveEntity.get("username")+"】,"+entity.toString();
        }else if (entity instanceof ChengYanDanWeiEntity){
            desc = "删除承研单位【"+saveEntity.get("danweiName")+"】,"+entity.toString();
        }else if (entity instanceof MenuTypeEntity){
            desc = "删除菜单【"+saveEntity.get("typeName")+"】,"+entity.toString();
        }else if (entity instanceof Role){
            desc = "删除角色【"+saveEntity.get("identification")+"】,"+entity.toString();
        }else {
            super.onDelete(entity, id, state, propertyNames, types);
        }

//        request.setAttribute(LoggerUtils.LOGGER_DESC,request.getAttribute(LoggerUtils.LOGGER_DESC)==null?desc:(request.getAttribute(LoggerUtils.LOGGER_DESC)+desc));
        request.setAttribute(LoggerUtils.LOGGER_DESC,desc);
        super.onDelete(entity, id, state, propertyNames, types);

    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        HttpServletRequest request = getRequest();
        Map saveEntity = new HashMap();
        System.out.println("----------onSave---------------");

        for (int i = 0; i < propertyNames.length; i++) {
            saveEntity.put(propertyNames[i],state[i]);

        }
        String desc;

        if (entity instanceof ZiliaoInfoEntity){
            desc = "新建资料【"+saveEntity.get("title")+"】,"+entity.toString();
        }else if (entity instanceof ProjectInfoEntity){
            desc = "新建项目【"+saveEntity.get("proName")+"】,"+entity.toString();
        }else if (entity instanceof AnliInfoEntity){
            desc = "新建案例【"+saveEntity.get("title")+"】,"+entity.toString();
        }else if (entity instanceof LowInfoEntity){
            desc = "新建法律法规【"+saveEntity.get("title")+"】,"+entity.toString();
        }else if (entity instanceof ExpertInfoEntity){
            desc = "新建专家【"+saveEntity.get("name")+"】,"+entity.toString();
        }else if (entity instanceof AnnounceInfoEntity){
            desc = "新建公告【"+saveEntity.get("title")+"】,"+entity.toString();
        }else if (entity instanceof User){
            desc = "新建用户【"+saveEntity.get("username")+"】,"+entity.toString();
        }else if (entity instanceof AuditInfo ){
            if (request.getRequestURI().equals("/SaveProject")){
                desc = request.getAttribute(LoggerUtils.LOGGER_DESC)+"并提交审核申请,审核人【"+saveEntity.get("auditUser")+"】";
            }else if (request.getRequestURI().equals("/UpdateProject")){
                desc = request.getAttribute(LoggerUtils.LOGGER_DESC)+"并提交审核申请,审核人【"+saveEntity.get("auditUser")+"】";
            }else {
                desc = "上传附件【"+saveEntity.get("fileName")+"】提交审核申请,审核人【"+saveEntity.get("auditUser")+"】";
            }

        }else if (entity instanceof ChengYanDanWeiEntity){
            desc = "新建承研单位【"+saveEntity.get("danweiName")+"】,"+entity.toString();
        }else if (entity instanceof BaoJiangEntity){
            desc = "新建报奖信息【"+saveEntity.get("jiangLiType")+"】,"+entity.toString();
        }else if (entity instanceof HuoJiangEntity){
            desc = "新建获奖信息【"+saveEntity.get("jiangliType")+"】,"+entity.toString();
        }else if (entity instanceof MenuTypeEntity){
            desc = "新建菜单【"+saveEntity.get("typeName")+"】,"+entity.toString();
        }else if (entity instanceof Role){
            desc = "新建角色【"+saveEntity.get("identification")+"】,"+entity.toString();
        }else {
            return super.onSave(entity, id, state, propertyNames, types);
        }

        request.setAttribute(LoggerUtils.LOGGER_DESC,desc);
        request.setAttribute("isave",true);
        return super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        System.out.println("----------onFlushDirty---------------");

        HttpServletRequest request = getRequest();
//        operationLog = (OperationLog) SpringContextUtils.getBean("operationLog");
//        System.out.println("实例----------:"+ JSONObject.fromObject(entity).toString());
//        System.out.println("修改后----------:"+ JSONArray.fromObject(currentState).toString());
//        System.out.println("修改前----------:"+ JSONArray.fromObject(previousState).toString());
//        System.out.println("属性名----------:"+JSONArray.fromObject(propertyNames).toString());
        Map before = new HashMap();
        Map after = new HashMap();



        for (int i = 0; i < propertyNames.length; i++) {

            before.put(propertyNames[i],previousState[i]);
            after.put(propertyNames[i],currentState[i]);

        }


        if (!(entity instanceof ModuleFileEntity)){
//            request.setAttribute("before",JSONObject.fromObject(before).toString());
//            request.setAttribute("after",JSONObject.fromObject(after).toString());

//            operationLog.setOperationBefore(JSONObject.fromObject(before).toString());
//            operationLog.setOperationAfter(JSONObject.fromObject(after).toString());

            StringBuffer optDesc = new StringBuffer();
            String desc;

            if (entity instanceof ZiliaoInfoEntity){
                optDesc.append("更新资料【"+before.get("title")+"】,将");
                desc = getOperationLog(dataMap,before,after,optDesc);
            }else if (entity instanceof ProjectInfoEntity){
                optDesc.append("更新项目【"+before.get("proName")+"】,将");
                desc = getOperationLog(proMap,before,after,optDesc);
            }else if (entity instanceof AnliInfoEntity){
                optDesc.append("更新案例【"+before.get("title")+"】,将");
                desc = getOperationLog(anliMap,before,after,optDesc);
            }else if (entity instanceof LowInfoEntity){
                optDesc.append("更新法律法规【"+before.get("title")+"】,将");
                desc = getOperationLog(lowsMap,before,after,optDesc);
            }else if (entity instanceof ExpertInfoEntity){
                optDesc.append("更新专家【"+before.get("name")+"】,将");
                desc = getOperationLog(expertMap,before,after,optDesc);
            }else if (entity instanceof AnnounceInfoEntity){
                optDesc.append("更新公告【"+before.get("title")+"】,将");
                desc = getOperationLog(announceMap,before,after,optDesc);
            }else if (entity instanceof AuditInfo){
                desc = "审核文件【"+after.get("fileName")+"】,"+after.get("auditContent");
            }else if (entity instanceof User){
                optDesc.append("更新用户【"+before.get("username")+"】,将");
                desc = getOperationLog(userMap,before,after,optDesc);
            }else {
                return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
            }

            Object isave = request.getAttribute("isave");

            if (isave == null){
                request.setAttribute(LoggerUtils.LOGGER_DESC,desc);
            }


        }

        System.out.println("----------2---------------");

        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    protected String getOperationLog(HashMap<String, String> map, Map before, Map after, StringBuffer optDesc){
        Set<Map.Entry<String, String>> entries = map.entrySet();

        for (Map.Entry entry:entries) {
            String key = (String) entry.getKey();
            System.out.println("---------------------------------");
            System.out.println(key);
            System.out.println("---------------------------------");
            String value = (String) entry.getValue();

            Object beforeVal = before.get(key);
            Object afterVal = after.get(key);
            if (null == beforeVal && (null != afterVal && !afterVal.equals(""))){
                optDesc.append("【"+value+"】由【 】修改为【"+afterVal+"】、");
            }


            if (null == afterVal && (null != beforeVal && !beforeVal.equals(""))){
                optDesc.append("【"+value+"】由【 "+beforeVal+" 】修改为【 】、");
            }

            if ((null != beforeVal && !beforeVal.equals("")) && (null != afterVal && !afterVal.equals("")) && !beforeVal.equals(afterVal)){
                optDesc.append("【"+value+"】由【"+beforeVal+"】修改为【"+afterVal+"】、");
            }


        }

        return optDesc.toString();
    }

    protected HttpServletRequest getRequest(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        return request;
    }
}
