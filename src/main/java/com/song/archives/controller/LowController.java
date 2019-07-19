package com.song.archives.controller;

import com.song.archives.aspect.ArchivesLog;
import com.song.archives.dao.*;
import com.song.archives.model.*;
import com.song.archives.utils.DateUtil;
import com.song.archives.utils.LoggerUtils;
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
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private JSONObject result;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ModuleFileRespository moduleFileRespository;

    @Autowired
    private LowRepository lowRepository ;

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
    @ArchivesLog(operationType = "lowList", description = "法律法规列表页面",writeFlag = false)
    @RequestMapping(value = "/lowList")
    ModelAndView lowList(@RequestParam(value = "typeId", required = false) Integer typeId){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("lows/list");
        ZiliaoInfoEntity ziliaoInfoEntity = new ZiliaoInfoEntity();
        ziliaoInfoEntity.setTypeId(typeId);
        modelAndView.addObject(ziliaoInfoEntity);

        LoggerUtils.setLoggerSuccess(request);

        return modelAndView;
    }


    /**
     * 新建法律法规页面
     *
     * @return
     */
    @ArchivesLog(operationType = "createLowPage", description = "新建法律法规页面")
    @RequestMapping(value = "/createLowPage")
    public ModelAndView createLowPage(@RequestParam(value = "lid", required = false) Long lid) {

        LowInfoEntity lowInfoEntity;

        if (null == lid){
            lowInfoEntity = new LowInfoEntity();
            lowInfoEntity.setUploader(getUser().getRealName());
            lowInfoEntity.setCreator(getUser().getUsername());
            lowInfoEntity.setUploadTime(DateUtil.parseDateToStr(new Date(),DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        }else {
            lowInfoEntity = lowRepository.findOne(lid);
        }


        List<User> auditUser = userRepository.findAuditUser();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("lows/createLow");
        modelAndView.addObject("info",lowInfoEntity);
        modelAndView.addObject("levelId",getUser().getPermissionLevel());
        modelAndView.addObject("auditUsers",auditUser);
        modelAndView.addObject("currentUser",getUser().getUsername());

        return modelAndView;
    }


    /**
     * 创建资料信息
     *
     * @param entity
     * @param mids
     * @return
     */
    @ArchivesLog(operationType = "createLow", description = "创建法律法规库信息")
    @RequestMapping(value = "/createLow")
    @ResponseBody
    @Transactional
    public String createLow(LowInfoEntity entity,
                               @RequestParam(value = "mids[]", required = false) List<Long> mids,
                               @RequestParam(value = "type",required = false) String type) {

        result = new JSONObject();
        try {

            if (null != entity.getId()) {
                moduleFileRespository.updateTidByIdAndType(entity.getId(),type);
            }

            LowInfoEntity infoEntity = lowRepository.save(entity);


            if (null != mids && mids.size() > 0) {
                for (Long id : mids) {
                    ModuleFileEntity moduleFileEntity = moduleFileRespository.findOne(id);
                    moduleFileEntity.setT_id(infoEntity.getId());
                    moduleFileRespository.save(moduleFileEntity);
                }

            }

            if (entity != null) {
                String content = entity.getContent();

                if (StringUtils.isNotEmpty(content) && StringUtils.isNotBlank(content)) {
                    entity.setContent(content.replace("\n", ""));
                }

            }

            lowRepository.save(entity);
            msg = SUCCESS;

        } catch (Exception e) {
            logger.error("创建法律法规:" + e.getMessage());
            msg = "创建法律法规异常";
        }


        LoggerUtils.setLoggerReturn(request,msg);
        result.put("msg", msg);


        return result.toString();

    }



    @ArchivesLog(operationType = "lows", description = "查询法律法规列表",descFlag = true)
    @RequestMapping(value = "/lows")
    @ResponseBody
    String lows(@RequestParam(value = "pageIndex", defaultValue = "1") Integer page,
                 @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                 @RequestParam(value = "searchValue",required = false) String searchValue,
                 @RequestParam(value = "typeId",required = false) Long typeId,
                 @RequestParam(value = "sortOrder", required = false, defaultValue = "asc") String sortOrder,
                 @RequestParam(value = "sortName", required = false, defaultValue = "id") String sortName) {

        result = new JSONObject();
        page = page - 1;
        Sort sort = new Sort(sortOrder.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortName);
        Pageable pageable = new PageRequest(page, size, sort);



        Specification<LowInfoEntity> specification = (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            List<Predicate> predicatesWhereArr = new ArrayList<>();


            predicatesWhereArr.add(criteriaBuilder.lessThanOrEqualTo(root.get("classificlevelId"),getUser().getPermissionLevel()));

            Predicate whereEquals = criteriaBuilder.and(predicatesWhereArr.toArray(new Predicate[predicatesWhereArr.size()]));


            if (StringUtils.isNotEmpty(searchValue)) {
                predicates.add(criteriaBuilder.like(root.get("type"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("publishDept"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("content"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("keyword"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("uploader"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + searchValue + "%"));
            }

            Predicate whereLike = criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));

            List<Predicate> predicateArr = new ArrayList<>();
            if (predicatesWhereArr.size() > 0){
                predicateArr.add(whereEquals);
            }
            if (predicates.size() > 0){
                predicateArr.add(whereLike);
            }

            return criteriaQuery.where(predicateArr.toArray(new Predicate[predicateArr.size()])).getRestriction();
        };

        Page<LowInfoEntity> lows = null;
        try {
            lows = lowRepository.findAll(specification,pageable);
            msg = SUCCESS;
        } catch (Exception e) {
            logger.error("查询法律法规列表异常数据:" + e.getMessage());
            msg = "查询法律法规列表异常";
        }


        JsonConfig jsonConfig = new JsonConfig();

        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

        JSONArray json = JSONArray.fromObject(lows, jsonConfig);

        LoggerUtils.setLoggerReturn(request,msg);
        result.put("msg", msg);
        result.put("result", json);
        return result.toString();
    }


    /**
     * 删除法律法规
     * @param ids
     * @return
     */
    @ArchivesLog(operationType = "delLow", description = "删除法律法规")
    @RequestMapping(value = "/delLow")
    @ResponseBody
    String delData(Long[] ids) {

        try{

            if (null != ids && ids.length >0){
                for (Long id:ids){
                    LowInfoEntity entity = lowRepository.findOne(id);
                    lowRepository.delete(entity);

                }
            }
            msg = SUCCESS;

        }catch (Exception e){
            logger.error(e.getMessage());
            msg = "删除法律法规异常";

        }


        result.put("msg", msg);
        LoggerUtils.setLoggerReturn(request,msg);
        return result.toString();
    }

    protected User getUser() {
        Subject subject = SecurityUtils.getSubject();
        return (User) subject.getPrincipal();
    }

}
