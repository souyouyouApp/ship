package com.song.archives.config;

import com.song.archives.model.FileInfoEntity;
import com.song.archives.model.ModuleFileEntity;
import com.song.archives.model.OperationLog;
import com.song.archives.model.ZiliaoInfoEntity;
import com.song.archives.utils.SpringContextUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class HibernateInterceptor extends EmptyInterceptor {

    private OperationLog operationLog;

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        operationLog = (OperationLog) SpringContextUtils.getBean("operationLog");
        System.out.println("实例----------:"+ JSONObject.fromObject(entity).toString());
        System.out.println("修改后----------:"+ JSONArray.fromObject(currentState).toString());
        System.out.println("修改前----------:"+ JSONArray.fromObject(previousState).toString());
        System.out.println("属性名----------:"+JSONArray.fromObject(propertyNames).toString());
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
        }
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }
}
