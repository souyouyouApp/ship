package com.song.archives.config;

import com.song.archives.model.*;
import com.song.archives.utils.SpringContextUtils;
import net.sf.json.JSONObject;
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

    private OperationLog operationLog;

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








    }
    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        operationLog = (OperationLog) SpringContextUtils.getBean("operationLog");
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
            request.setAttribute("before",JSONObject.fromObject(before).toString());
            request.setAttribute("after",JSONObject.fromObject(after).toString());

            operationLog.setOperationBefore(JSONObject.fromObject(before).toString());
            operationLog.setOperationAfter(JSONObject.fromObject(after).toString());

            StringBuffer optDesc = new StringBuffer();

            optDesc.append(", 将");
            if (entity instanceof ZiliaoInfoEntity){
                setOperationLog(dataMap,before,after,optDesc);
            }

            if (entity instanceof ProjectInfoEntity){
                setOperationLog(proMap,before,after,optDesc);
            }

            if (entity instanceof AnliInfoEntity){
                setOperationLog(anliMap,before,after,optDesc);
            }

            if (entity instanceof LowInfoEntity){
                setOperationLog(lowsMap,before,after,optDesc);
            }

            if (entity instanceof ExpertInfoEntity){
                setOperationLog(expertMap,before,after,optDesc);
            }

            if (entity instanceof AnnounceInfoEntity){
                setOperationLog(announceMap,before,after,optDesc);
            }
        }
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    protected void setOperationLog(HashMap<String, String> map, Map before, Map after, StringBuffer optDesc){
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

        operationLog.setOperationDescrib(operationLog.getOperationDescrib()+optDesc.toString());
    }
}
