package com.song.archives.controller;

import com.song.archives.aspect.ArchivesLog;
import com.song.archives.dao.*;
import com.song.archives.model.*;
import com.song.archives.utils.DateUtil;
import com.song.archives.utils.ImageUploadUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

import static com.song.archives.utils.FileUtils.zipFile;


/**
 * Created by souyouyou on 2018/2/28.
 */

@Controller
@RequestMapping("/")
public class LowController {

    Logger logger = Logger.getLogger(LowController.class);

    public static String SUCCESS = "success";

    private String msg = "failed";

    private String operationLogInfo = "";

    private JSONObject result;


    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Autowired
    private ModuleFileRespository moduleFileRespository;

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private AnliRepository anliRepository;

    @Autowired
    private AnnounceRepository announceRepository;

    @Autowired
    private MsgAttachRepository msgAttachRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private AuditInfoRepository auditInfoRepository;

    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${data.templatePath}")
    private String dataTemplatePath;


    @Value("${data.imgPath}")
    private String imgPath;

    @Value("${filePath}")
    private String filePath;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 法律法规列表页面
     * @return
     */
    @ArchivesLog(operationType = "lowList",operationName = "法律法规列表页面")
    @RequestMapping(value = "/lowList")
    ModelAndView lowList(@RequestParam(value = "typeId", required = false) Integer typeId){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("lows/list");
        ZiliaoInfoEntity ziliaoInfoEntity = new ZiliaoInfoEntity();
        ziliaoInfoEntity.setTypeId(typeId);
        modelAndView.addObject(ziliaoInfoEntity);

        return modelAndView;
    }


    /**
     * 新建法律法规页面
     *
     * @return
     */
    @ArchivesLog(operationType = "createLow", operationName = "新建法律法规页面")
    @RequestMapping(value = "/createLow")
    public ModelAndView createData(@RequestParam(value = "lid", required = false) Long lid) {

//        ZiliaoInfoEntity ziliaoInfoEntity;
//
//        List<User> auditUser = userRepository.findAuditUser();
//
//        if (null == zid) {
//            ziliaoInfoEntity = new ZiliaoInfoEntity();
//            ziliaoInfoEntity.setCreateTime(DateUtil.parseDateToStr(new Date(),DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
//        } else {
//            ziliaoInfoEntity = dataRepository.findOne(zid);
//        }
//
//
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("data/createData");
//        modelAndView.addObject("info", ziliaoInfoEntity);
//        modelAndView.addObject("proentity",ziliaoInfoEntity);
//        modelAndView.addObject("mid",5);
//
//        modelAndView.addObject("auditUsers",auditUser);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("lows/createLow");
        modelAndView.addObject("levelId",getUser().getPermissionLevel());
        modelAndView.addObject("info", new ZiliaoInfoEntity());
        return modelAndView;
    }

    protected User getUser() {
        Subject subject = SecurityUtils.getSubject();
        return (User) subject.getPrincipal();
    }

}
