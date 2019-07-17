package com.song.archives.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.song.archives.aspect.ArchivesLog;
import com.song.archives.dao.LoggerJpa;
import com.song.archives.model.OperationEntity;
import com.song.archives.model.User;
import com.song.archives.utils.DateUtil;
import com.song.archives.utils.LoggerUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class LoggerInterceptor implements HandlerInterceptor {

    public static final String LOGGER_ENTITY="_logger_entity";

    public static Set<String> sessinIF = new HashSet<>();

    static {
        sessinIF.add("/downLoadFile");
        sessinIF.add("/downLoadAttach");
    }


    //调用请求的时候执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {

        if (sessinIF.contains(request.getRequestURI())){
            String sessionId = request.getParameter("sessionId");
            if (null == sessionId || !request.getSession().getId().equals(sessionId)){
                request.getRequestDispatcher("/login").forward(request,response);
                return false;
            }
        }

        //创建请求实体
        OperationEntity operationEntity = new OperationEntity();

        String sessionId = request.getRequestedSessionId();
        String path = request.getRequestURI();
        String paramData = JSON.toJSONString(request.getParameterMap(), SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);

        operationEntity.setIp(LoggerUtils.getCliectIp(request));
        operationEntity.setMethod(request.getMethod());
        operationEntity.setParamData(paramData);
        operationEntity.setUri(path);
        operationEntity.setSessionId(sessionId);
        operationEntity.setBeginTime(DateUtil.parseDateToStr(new Date(),DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        operationEntity.setReturnTime(DateUtil.parseDateToStr(new Date(),DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));

        //记录用户信息
        User user = (User) request.getSession().getAttribute("user");
        if (null != user){
            operationEntity.setUserId(String.valueOf(user.getId()));
            operationEntity.setUserName(user.getRealName());
        }
        //设置请求实体到request中，方便after调用
        request.setAttribute(LOGGER_ENTITY,operationEntity);
        return true;
    }
    //controller调用之后执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }
    //viewResolve返回view到前台之前执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        //获取请求错误码
        int status = response.getStatus();
        //当前时间
        long currentTime = System.currentTimeMillis();
        //获取本次请求日志实体
        OperationEntity operationEntity = (OperationEntity) request.getAttribute(LOGGER_ENTITY);
        //设置返回时间
        operationEntity.setReturnTime(String.valueOf(currentTime));
        //设置返回错误码
        operationEntity.setHttpStatusCode(String.valueOf(status));
        //设置返回值
        operationEntity.setReturnData(JSON.toJSONString(LoggerUtils.LOGGER_RETURN,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue));

        Object result = request.getAttribute(LoggerUtils.LOGGER_RESULT);
        Object desc = request.getAttribute(LoggerUtils.LOGGER_DESC);

        operationEntity.setResult(null == result?"":result.toString());
        operationEntity.setDescription(null == desc?"":desc.toString());



        //将日志写入到数据库
        LoggerJpa loggerJpa = getDAO(LoggerJpa.class,request);


        HandlerMethod method= ((HandlerMethod)handler);
        ArchivesLog anno = method.getMethodAnnotation(ArchivesLog.class);

        if (null != anno){
            if (anno.writeFlag()){
                if (anno.descFlag()){
                    operationEntity.setDescription(anno.description());
                }

                if (null != operationEntity.getDescription() && !operationEntity.getDescription().equals(""))
                    loggerJpa.save(operationEntity);
            }
        }

    }

    private <T> T getDAO(Class<T> clazz,HttpServletRequest request)
    {
        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        return factory.getBean(clazz);
    }
}