package com.song.archives.controller;

import com.song.archives.aspect.ArchivesLog;
import com.song.archives.dao.ExpertRepository;
import com.song.archives.dao.ModuleFileRespository;
import com.song.archives.model.*;
import com.song.archives.utils.DateUtil;
import com.song.archives.utils.ExcelUtil;
import com.song.archives.utils.LoggerUtils;
import com.song.archives.utils.WebUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Encoder;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by souyouyou on 2018/3/12.
 */

@Controller
@RequestMapping("/")
public class ExpertController {

    Logger logger = Logger.getLogger(AnliController.class);

    public static String SUCCESS = "success";

    private String msg = "failed";


    @Autowired
    private HttpServletRequest request;

    private JSONObject result;

    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    private ModuleFileRespository moduleFileRespository;

    /**
     * 删除专家
     *
     * @param ids
     * @return
     */
    @ArchivesLog(operationType = "delExpert", description = "删除专家")
    @RequestMapping(value = "/delExpert")
    @ResponseBody
    String delExpert(Long[] ids) {

        try {

            if (null != ids && ids.length > 0) {
                for (Long id : ids) {
                    ExpertInfoEntity entity = expertRepository.findOne(id);
                    expertRepository.delete(entity);

                }
            }
            msg = SUCCESS;

        } catch (Exception e) {
            logger.error(e.getMessage());
            msg = "删除专家异常";

        }


        result.put("msg", msg);
        LoggerUtils.setLoggerReturn(request,msg);
        return result.toString();
    }


    @ArchivesLog(operationType = "createExpert", description = "创建专家信息")
    @RequestMapping(value = "/createExpert")
    @ResponseBody
    @Transactional
    public String createExpert(@RequestParam(value = "pic", required = false) MultipartFile file,
                               @RequestParam(value = "id", required = false) Long id,
                               HttpServletRequest request) {
        ExpertInfoEntity infoEntity = new ExpertInfoEntity();

        result = new JSONObject();



        try {
            ExpertInfoEntity expertInfoEntity;
            expertInfoEntity = WebUtils.request2Bean(request, infoEntity.getClass());

            if (null != id) {
                infoEntity = expertRepository.findOne(id);
                expertInfoEntity.setPic(infoEntity.getPic());
                expertInfoEntity.setId(id);            }


            if (null != file && !file.isEmpty()) {
                String encode = new BASE64Encoder().encode(file.getBytes());
                expertInfoEntity.setPic("data:image/jpeg;base64," + encode);
            }

            expertRepository.save(expertInfoEntity);

            msg = SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("创建专家信息:" + e.getMessage());
            msg = "创建专家信息异常";
        }
        LoggerUtils.setLoggerReturn(request,msg);

        result.put("msg", msg);


        return result.toString();

    }

    @ArchivesLog(operationType = "importExpert", description = "新建专家页面")
    @RequestMapping(value = "/importExpert")
    @ResponseBody
    public String importExpert(@RequestParam(value = "dataFile") MultipartFile file) throws IOException, InvalidFormatException {

        result = new JSONObject();
        String successImport = "导入成功%d条;";
        String failImport = "导入失败%d条;";
        int successCnt = 0;
        int failCnt = 0;
        try {
            List<String[]> experts = ExcelUtil.excel2List(file);


            if (null == experts || experts.isEmpty()){
                msg = "文件不能为空";
                result.put("msg",msg);
                LoggerUtils.setLoggerFailed(request,msg);
                return result.toString();
            }

            for (String[] expertData:experts){
                try {
                    ExpertInfoEntity expert = new ExpertInfoEntity();
                    String name = expertData.length >= 1?expertData[0]:"";
                    String gender = expertData.length >= 2?expertData[1]:"";
                    String birth = expertData.length >= 3?expertData[2]:"";
                    String szdanwei = expertData.length >= 4?expertData[3]:"";
                    String zhicheng = expertData.length >= 5?expertData[4]:"";
                    String sxzhuanye = expertData.length >= 6?expertData[5]:"";
                    String mobile = expertData.length >= 7?expertData[6]:"";
                    String idNo = expertData.length >= 8?expertData[7]:"";
                    String faxCode = expertData.length >= 9?expertData[8]:"";
                    String remark = expertData.length >= 10?expertData[9]:"";
                    String education = expertData.length >= 11?expertData[10]:"";
                    String zhiwu = expertData.length >= 12?expertData[11]:"";
                    String profile = expertData.length >= 13?expertData[12]:"";
                    String szbumen = expertData.length >= 14?expertData[13]:"";
                    String sslingyu = expertData.length >= 15?expertData[14]:"";
                    String address = expertData.length >= 16?expertData[15]:"";
                    String postcode = expertData.length >= 17?expertData[16]:"";
                    String pycishu = expertData.length >= 18?expertData[17]:"";


                    expert.setName(name);
                    expert.setGender(gender);
                    expert.setBirth(birth);
                    expert.setSzdanwei(szdanwei);
                    expert.setZhicheng(zhicheng);
                    expert.setSxzhuanye(sxzhuanye);
                    expert.setMobile(mobile);
                    expert.setIdNo(idNo);
                    expert.setFaxcode(faxCode);
                    expert.setRemark(remark);
                    expert.setEducation(education);
                    expert.setZhiwu(zhiwu);
                    expert.setProfile(profile);
                    expert.setSzbumen(szbumen);
                    expert.setSslingyu(sslingyu);
                    expert.setAddress(address);
                    expert.setPostcode(postcode);
                    expert.setPycishu(Integer.parseInt(pycishu));


                    expertRepository.save(expert);
                    successCnt++;
                }catch (Exception e){
                    failCnt++;
                }

            }

            msg = SUCCESS;

        } catch (Exception e) {
            msg = "导入专家异常";
            e.printStackTrace();
        }

        LoggerUtils.setLoggerReturn(request,msg);
        result.put("msg", msg);
        result.put("result",String.format(successImport, successCnt)+""+String.format(failImport,failCnt));

        return result.toString();
    }




    /**
     * 新建专家页面
     *
     * @return
     */
    @ArchivesLog(operationType = "expert", description = "新建专家页面")
    @RequestMapping(value = "/expert")
    public ModelAndView expert(@RequestParam(value = "eid", required = false) Long eid) {

        ExpertInfoEntity expertInfoEntity;

        if (null == eid) {
            expertInfoEntity = new ExpertInfoEntity();
            expertInfoEntity.setCreateTime(DateUtil.parseDateToStr(new Date(),DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI));
            expertInfoEntity.setCreator(getUser().getUsername());
        } else {
            expertInfoEntity = expertRepository.findOne(eid);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("expert/createExpert");
        modelAndView.addObject("expert", expertInfoEntity);
        modelAndView.addObject("proentity", expertInfoEntity);
        modelAndView.addObject("mid", 4);
        modelAndView.addObject("currentUser",getUser().getUsername());


        LoggerUtils.setLoggerSuccess(request);

        return modelAndView;
    }

    /**
     * 查询专家列表
     *
     * @param page
     * @param size
     * @param typeId
     * @return
     */
    @ArchivesLog(operationType = "experts", description = "查询专家列表",descFlag = true)
    @RequestMapping(value = "/experts")
    @ResponseBody
    String experts(@RequestParam(value = "pageIndex", defaultValue = "0") Integer page,
                   @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                   @RequestParam(value = "searchValue", required = false) String searchValue,
                   @RequestParam(value = "typeId", required = false) Long typeId,
                   @RequestParam(value = "sortOrder", required = false, defaultValue = "asc") String sortOrder,
                   @RequestParam(value = "sortName", required = false, defaultValue = "id") String sortName) {


        result = new JSONObject();
        page = page - 1;
        Sort sort = new Sort(sortOrder.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortName);
        Pageable pageable = new PageRequest(page, size, sort);


        Specification<ExpertInfoEntity> specification = (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            List<Predicate> predicatesWhereArr = new ArrayList<>();

            if (null == typeId && StringUtils.isEmpty(searchValue)) {
//                predicatesWhereArr.add(criteriaBuilder.lessThanOrEqualTo(root.get("classiclevelId"),getUser().getPermissionLevel()));
                return criteriaBuilder.and(predicatesWhereArr.toArray(new Predicate[predicates.size()]));
            }


            if (null != typeId) {
                predicatesWhereArr.add(criteriaBuilder.equal(root.get("typeId"), typeId));
            }
//            predicatesWhereArr.add(criteriaBuilder.lessThanOrEqualTo(root.get("classiclevelId"),getUser().getPermissionLevel()));

            Predicate whereEquals = criteriaBuilder.and(predicatesWhereArr.toArray(new Predicate[predicatesWhereArr.size()]));


            if (StringUtils.isNotEmpty(searchValue)) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("sxzhuanye"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("cszhuanye"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("zhiwu"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("profile"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("szdanwei"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("szbumen"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("sslingyu"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("address"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("faxcode"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("zjpingjia"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("mobile"), "%" + searchValue + "%"));
            }

            Predicate whereLike = criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));

            List<Predicate> predicateArr = new ArrayList<>();
            if (predicatesWhereArr.size() > 0) {
                predicateArr.add(whereEquals);
            }
            if (predicates.size() > 0) {
                predicateArr.add(whereLike);
            }


            return criteriaQuery.where(predicateArr.toArray(new Predicate[predicateArr.size()])).getRestriction();
        };

        Page<ExpertInfoEntity> datas = null;
        try {
            datas = expertRepository.findAll(specification, pageable);
            msg = SUCCESS;
        } catch (Exception e) {
            logger.error("专家列表数据:" + e.getMessage());
            msg = "查询专家列表异常";
        }


        result.put("msg", msg);
        LoggerUtils.setLoggerReturn(request,msg);
        result.put("result", JSONArray.fromObject(datas));
        return result.toString();
    }

    /**
     * 专家列表
     *
     * @return
     */
    @ArchivesLog(operationType = "expertList", description = "专家列表",writeFlag = false)
    @RequestMapping(value = "/expertList")
    ModelAndView expertList(@RequestParam(value = "typeId", required = false) Integer typeId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("expert/list");
        ZiliaoInfoEntity ziliaoInfoEntity = new ZiliaoInfoEntity();
        ziliaoInfoEntity.setTypeId(typeId);
        modelAndView.addObject(ziliaoInfoEntity);

        LoggerUtils.setLoggerSuccess(request);

        return modelAndView;
    }

    protected User getUser() {
        Subject subject = SecurityUtils.getSubject();
        return (User) subject.getPrincipal();
    }
}
