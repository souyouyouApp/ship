package com.song.archives.controller;

import com.song.archives.aspect.ArchivesLog;
import com.song.archives.dao.AnnounceRepository;
import com.song.archives.dao.ModuleFileRespository;
import com.song.archives.dao.UserRepository;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by souyouyou on 2018/3/15.
 */


@Controller
@RequestMapping("/")
public class AnnounceController {

    Logger logger = Logger.getLogger(AnnounceController.class);

    public static String SUCCESS = "success";

    private String msg = "failed";

    private JSONObject result;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnnounceRepository announceRepository;

    @Autowired
    private ModuleFileRespository moduleFileRespository;


    /**
     * 创建公告信息
     * @param entity
     * @param mids
     * @return
     */
    @ArchivesLog(operationType = "createAnnounce", description = "创建公告信息")
    @RequestMapping(value = "/createAnnounce")
    @ResponseBody
//    @Transactional
    public String createAnnounce(AnnounceInfoEntity entity,
                                 @RequestParam(value = "mids[]",required = false) List<Long> mids,
                                 @RequestParam(value = "type",required = false) String type,
                                 @RequestParam(value = "relatedUsers[]") Long[] relatedUsers){

        result = new JSONObject();

        Long id = entity.getId();

        try{

            if (null != entity.getId()){
                moduleFileRespository.updateTidByIdAndType(entity.getId(),type);
            }

            JSONArray nameArr = new JSONArray();
            JSONArray idArr = new JSONArray();
            for (Long uid: relatedUsers){
                User user = userRepository.findOne(uid);
                nameArr.add(user.getRealName());
                idArr.add(user.getId());
            }

            entity.setRelatedUserIds(idArr.toString());
            entity.setRelatedUserName(nameArr.toString());


            AnnounceInfoEntity announceInfoEntity = announceRepository.save(entity);


            if (null != mids && mids.size() >0){
                for (Long mid:mids) {
                    ModuleFileEntity fileEntity = moduleFileRespository.findOne(mid);
                    fileEntity.setT_id(announceInfoEntity.getId());
                    moduleFileRespository.save(fileEntity);
                }

            }


            if (entity != null){

                String content = entity.getContent();

                if (StringUtils.isNotEmpty(content) && StringUtils.isNotBlank(content)){
                    entity.setContent(content.replace("\n",""));
                }

            }
            LoggerUtils.setLoggerSuccess(request);
            announceRepository.save(entity);
            msg = SUCCESS;


        }catch (Exception e){
            logger.error("创建公告信息:"+e.getMessage());
            e.printStackTrace();
            msg = "Exception";
            LoggerUtils.setLoggerFailed(request);
        }

        result.put("msg",msg);


        return result.toString();

    }


    /**
     * 删除公告
     * @param ids
     * @return
     */
    @ArchivesLog(operationType = "delAnnounce", description = "删除公告")
    @RequestMapping(value = "/delAnnounce")
    @ResponseBody
    String delAnnounce(Long[] ids) {

        try{

            if (null != ids && ids.length >0){
                for (Long id:ids){
                    AnnounceInfoEntity entity = announceRepository.findOne(id);

                    if (!entity.getCreator().equals(getUser().getUsername())){
                        msg = "不能删除非本人新建的记录";
                        result.put("msg", msg);
                        LoggerUtils.setLoggerFailed(request,msg);
                        return result.toString();
                    }

                    announceRepository.delete(entity);

                }
            }
            msg = SUCCESS;
            LoggerUtils.setLoggerSuccess(request);
        }catch (Exception e){
            logger.error(e.getMessage());
            msg = "delete announce failed";
            LoggerUtils.setLoggerFailed(request);
        }

        result.put("msg", msg);
        return result.toString();
    }
    /**
     * 新建公告页面
     * @return
     */
    @ArchivesLog(operationType = "createAnnoucePage", description = "新建公告页面")
    @RequestMapping(value = "/createAnnoucePage")
    public ModelAndView createAnliPage(@RequestParam(value = "aid",required = false) Long aid){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("announce/createAnnounce");
        AnnounceInfoEntity announceInfoEntity;

        List<User> auditUser = userRepository.findAuditUser();

        if(null == aid){
            announceInfoEntity = new AnnounceInfoEntity();
            modelAndView.addObject("uids",null);
            announceInfoEntity.setSponsorDate(DateUtil.parseDateToStr(new Date(),DateUtil.DATE_FORMAT_YYYY_MM_DD));
            announceInfoEntity.setCreator(getUser().getUsername());
            announceInfoEntity.setCreateTime(DateUtil.parseDateToStr(new Date(),DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        }else {
            announceInfoEntity = announceRepository.findOne(aid);
            modelAndView.addObject("uids",announceInfoEntity.getRelatedUserIds().split(","));
        }


        Specification<User> specification = (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            //屏蔽超级管理员
            predicates.add(criteriaBuilder.notEqual(root.get("username"), "administrator"));
            predicates.add(criteriaBuilder.notEqual(root.get("username"), "superadmin"));
            predicates.add(criteriaBuilder.notEqual(root.get("username"), "securitor"));
            predicates.add(criteriaBuilder.notEqual(root.get("username"), "comptroller"));

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };

        List<User> users = userRepository.findAll(specification);


        modelAndView.addObject("info",announceInfoEntity);
        modelAndView.addObject("proentity",announceInfoEntity);
        modelAndView.addObject("mid",3);
        modelAndView.addObject("users",users);
        modelAndView.addObject("levelId",getUser().getPermissionLevel());
        modelAndView.addObject("currentUser",getUser().getUsername());
        modelAndView.addObject("auditUsers",auditUser);

        LoggerUtils.setLoggerSuccess(request);

        return modelAndView;
    }


    /**
     * 查询公告列表
     * @param page
     * @param size
     * @param searchValue
     * @return
     */
    @ArchivesLog(operationType = "announces", description = "查询公告列表",descFlag = true)
    @RequestMapping(value = "/announces")
    @ResponseBody
    String announces(@RequestParam(value = "pageIndex", defaultValue = "0") Integer page,
                     @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                     @RequestParam(value = "searchValue",required = false) String searchValue,
                     @RequestParam(value = "typeId", required = false) Long typeId,
                     @RequestParam(value = "sortOrder", required = false, defaultValue = "asc") String sortOrder,
                     @RequestParam(value = "sortName", required = false, defaultValue = "id") String sortName){


        result = new JSONObject();
        page = page - 1;
        Sort sort = new Sort(sortOrder.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortName);
        Pageable pageable = new PageRequest(page, size, sort);


        Specification<AnnounceInfoEntity> specification = (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            List<Predicate> predicatesWhereArr = new ArrayList<>();

            if (null == typeId && StringUtils.isEmpty(searchValue)) {
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }


            if (null != typeId) {
                predicatesWhereArr.add(criteriaBuilder.equal(root.get("typeId"), typeId));
            }

            Predicate whereEquals = criteriaBuilder.or(predicatesWhereArr.toArray(new Predicate[predicatesWhereArr.size()]));


            if (StringUtils.isNotEmpty(searchValue)) {
                predicates.add(criteriaBuilder.like(root.get("title"),"%"+searchValue+"%"));
                predicates.add(criteriaBuilder.like(root.get("creator"),"%"+searchValue+"%"));
                predicates.add(criteriaBuilder.like(root.get("createTime"),"%"+searchValue+"%"));
                predicates.add(criteriaBuilder.like(root.get("sponsorDate"),"%"+searchValue+"%"));
                predicates.add(criteriaBuilder.like(root.get("deadlineDate"),"%"+searchValue+"%"));
                predicates.add(criteriaBuilder.like(root.get("sponsor"),"%"+searchValue+"%"));
                predicates.add(criteriaBuilder.like(root.get("content"),"%"+searchValue+"%"));
                predicates.add(criteriaBuilder.like(root.get("typeName"),"%"+searchValue+"%"));
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


        Page<AnnounceInfoEntity> datas = null;
        try {
            datas = announceRepository.findAll(specification, pageable);
            msg = SUCCESS;
            LoggerUtils.setLoggerSuccess(request);
        }catch (Exception e){
            logger.error("查询公告列表:"+e.getMessage());
            msg = "Exception";
            LoggerUtils.setLoggerFailed(request);
        }


        JsonConfig jsonConfig = new JsonConfig();

        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

        JSONArray json =JSONArray.fromObject(datas, jsonConfig);

        result.put("msg",msg);
        result.put("result", json);
        return result.toString();
    }
    /**
     * 公告列表页面
     * @return
     */
    @ArchivesLog(operationType = "announceList", description = "公告列表页面",writeFlag = false)
    @RequestMapping(value = "/announceList")
    ModelAndView announceList(@RequestParam(value = "typeId", required = false) Integer typeId){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("announce/list");
        ZiliaoInfoEntity ziliaoInfoEntity = new ZiliaoInfoEntity();
        ziliaoInfoEntity.setTypeId(typeId);
        modelAndView.addObject(ziliaoInfoEntity);

        return modelAndView;
    }


    protected User getUser(){
        Subject subject = SecurityUtils.getSubject();
        return (User) subject.getPrincipal();
    }
}
