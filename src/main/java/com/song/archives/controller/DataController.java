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
import org.springframework.util.ResourceUtils;
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
import java.util.*;
import java.util.zip.ZipOutputStream;

import static com.song.archives.utils.FileUtils.zipFile;


/**
 * Created by souyouyou on 2018/2/28.
 */

@Controller
@RequestMapping("/")
public class DataController {

    Logger logger = Logger.getLogger(DataController.class);

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
     * 查询资料文件
     * @param fileCode
     * @return
     */
    @ArchivesLog(operationType = "findModuleFileByFileCode", operationName = "查询资料文件")
    @RequestMapping(value = "/findModuleFileByFileCode")
    @ResponseBody
    public String findModuleFileByFileCode(@RequestParam(value = "fileCode") String fileCode) {
        ModuleFileEntity moduleFileEntity = moduleFileRespository.findByFileCode(fileCode);

        FileInfoEntity fileInfoEntity = fileInfoRepository.findByFileCode(fileCode);
        JsonConfig jsonConfig = new JsonConfig();

        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

        JSONObject json = JSONObject.fromObject(moduleFileEntity, jsonConfig);

        operationLogInfo = "用户【"+getUser().getRealName()+"】查询资料文件";
        json.put("operationLog",operationLogInfo);
        json.put("fileType", fileInfoEntity.getFileType());
        return json.toString();
    }

    @ArchivesLog(operationType = "findFileByZid", operationName = "查询资料文件")
    @RequestMapping(value = "/findFileByZid")
    @ResponseBody
    public String findFileByZid(@RequestParam(value = "zid") Long zid,
                                @RequestParam(value = "type") String type) {

        List<FileInfoEntity> fileEntities = fileInfoRepository.findByTidAndType(zid,type,getUser().getPermissionLevel());

        JsonConfig jsonConfig = new JsonConfig();

        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

        JSONArray json = JSONArray.fromObject(fileEntities, jsonConfig);
        return json.toString();
    }


    @ArchivesLog(operationType = "findTidByFileId", operationName = "查询资料文件")
    @RequestMapping(value = "/findTidByFileId")
    @ResponseBody
    public String findTidByFileId(@RequestParam(value = "fileId") Long fileId) {

        Long tid = fileInfoRepository.findTidByFileId(fileId);

        return String.valueOf(tid);
    }




    /**
     * 查看纸质文件存档
     *
     * @param mid
     * @return
     */
    @ArchivesLog(operationType = "viewFile", operationName = "查看纸质文件存档")
    @RequestMapping(value = "/viewFile")
    @ResponseBody
    public String viewFile(@RequestParam(value = "mid") Long mid) {

        ModuleFileEntity moduleFileEntity = moduleFileRespository.findOne(mid);

        FileInfoEntity fileInfoEntity = fileInfoRepository.findByFileCode(moduleFileEntity.getFileCode());
        JSONObject json = JSONObject.fromObject(fileInfoEntity);

        operationLogInfo = "用户【"+getUser().getRealName()+"】查看纸质文件存档";
        json.put("operationLog",operationLogInfo);
        return json.toString();
    }


    @ArchivesLog(operationType = "downLoadAttach", operationName = "下载附件")
    @RequestMapping(value = "/downLoadAttach")
    @ResponseBody
    public String downLoadAttach(HttpServletResponse response,
                                 @RequestParam(value = "ids") Long[] ids,
                                 @RequestParam(value = "type") String type) {



        OperationLog operation = new OperationLog();
        operation.setOperationStartTime(dateFormat.format(new Date()));

        List<File> files = new ArrayList<>();

        List<File> typeFiles;

        String logFileName = "";
        for (Long id : ids) {
            typeFiles = new ArrayList<>();
            String fileName = "";
            //AL：案例；GG：公告；PJ：项目 DT:资料
            List<ModuleFileEntity> moduleFileEntitys = null;
            if ("AL".equals(type)) {
                AnliInfoEntity anliInfoEntity = anliRepository.findOne(id);
                moduleFileEntitys = moduleFileRespository.findByT_idAndType(anliInfoEntity.getId(), "AL");
                fileName = anliInfoEntity.getTitle();

            } else if ("GG".equals(type)) {
                AnnounceInfoEntity announceInfoEntity = announceRepository.findOne(id);
                moduleFileEntitys = moduleFileRespository.findByT_idAndType(announceInfoEntity.getId(), "GG");
                fileName = announceInfoEntity.getTitle();

            } else if ("PJ".equals(type)) {


            } else if ("DT".equals(type)) {
                ZiliaoInfoEntity ziliaoInfoEntity = dataRepository.findOne(id);
                moduleFileEntitys = moduleFileRespository.findByT_idAndType(ziliaoInfoEntity.getId(), "DT");
                fileName = ziliaoInfoEntity.getTitle();

            }


            logFileName += fileName;
            if (null != moduleFileEntitys) {
                for (ModuleFileEntity moduleFileEntity : moduleFileEntitys) {

                    FileInfoEntity fileInfoEntity = fileInfoRepository.findByFileCodeAndFileType(moduleFileEntity.getFileCode(), "1");

                    if (null == fileInfoEntity)
                        continue;
                    File file = new File(fileInfoEntity.getFilePath());

                    typeFiles.add(file);
                }
            }



            operation.setOperationDescrib("用户【"+getUser().getRealName()+"】下载文件【"+logFileName+"】");
            operation.setOperationUserId(getUser().getId());
            operation.setOperationEndTime(dateFormat.format(new Date()));
            operation.setOperationUserName(getUser().getUsername());
            operation.setOperationType("getFile");
            operation.setOperationResult("成功");

            //按照个例打包成一个文件
            File caseZip = new File(filePath + "/" + fileName + ".zip");
            try {
                FileOutputStream outStream = new FileOutputStream(caseZip);
                // 压缩流
                ZipOutputStream toClient = new ZipOutputStream(outStream);
                //  toClient.setEncoding("gbk");
                zipFile(typeFiles, toClient);
                toClient.close();
                outStream.close();
                files.add(caseZip);

            } catch (Exception e) {
                e.printStackTrace();
                operation.setOperationResult("失败");
            }
            operationRepository.save(operation);

        }


        String fileName = UUID.randomUUID().toString() + ".zip";
        // 在服务器端创建打包下载的临时文件
        File fileZip = new File(filePath + "/" + fileName);

        // 文件输出流
        try {
            FileOutputStream outStream = new FileOutputStream(fileZip);
            // 压缩流
            ZipOutputStream toClient = new ZipOutputStream(outStream);
            //  toClient.setEncoding("gbk");
            zipFile(files, toClient);
            toClient.close();
            outStream.close();

            this.downloadFile(fileZip, response, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 创建资料信息
     *
     * @param entity
     * @param mids
     * @return
     */
    @ArchivesLog(operationType = "createZiliao", operationName = "创建资料信息")
    @RequestMapping(value = "/createZiliao")
    @ResponseBody
    @Transactional
    public String createZiliao(ZiliaoInfoEntity entity,
                               @RequestParam(value = "mids[]", required = false) List<Long> mids,
                               @RequestParam(value = "type",required = false) String type) {

        result = new JSONObject();

        String operationType = "";

        if (entity.getId() == null || entity.getId().equals(0L)){
            operationType = "新建资料";
        }else {
            operationType = "更新资料";
        }

        try {

            if (null != entity.getId()) {
                moduleFileRespository.updateTidByIdAndType(entity.getId(),type);
            }

            ZiliaoInfoEntity infoEntity = dataRepository.save(entity);


            if (null != mids && mids.size() > 0) {
                for (Long id : mids) {
                    ModuleFileEntity moduleFileEntity = moduleFileRespository.findOne(id);
                    moduleFileEntity.setT_id(infoEntity.getId());
                    moduleFileRespository.save(moduleFileEntity);
                }

            }

            if (entity != null) {
                    entity.setAuthor(entity.getAuthor());
                    entity.setPublishDate(DateUtil.parseDateToStr(new Date(),DateUtil.DATE_FORMAT_YYYY_MM_DD));
                    entity.setCreator(getUser().getUsername());
                    entity.setCreateTime(DateUtil.parseDateToStr(new Date(),DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));

                    String content = entity.getZiliaoContent();

                    if (StringUtils.isNotEmpty(content) && StringUtils.isNotBlank(content)) {
                        entity.setZiliaoContent(content.replace("\n", ""));
                    }

            }

            dataRepository.save(entity);
            msg = SUCCESS;

        } catch (Exception e) {
            logger.error("创建资料信息:" + e.getMessage());
            msg = "Exception";
        }



        operationLogInfo = "用户【"+getUser().getRealName()+"】"+operationType+"【"+entity.getTitle()+"】";
        result.put("operationLog",operationLogInfo);
        result.put("msg", msg);


        return result.toString();

    }


    /**
     * 上传附件
     *
     * @param multipartFile
     * @param category
     * @param classificlevel
     * @param fileName
     * @param zrr
     * @param fileType
     * @return
     */
    @ArchivesLog(operationType = "uploadAatachment", operationName = "上传附件")
    @RequestMapping(value = "/uploadAatachment")
    @ResponseBody
    public String uploadAatachment(@RequestParam(value = "dataFile", required = false) MultipartFile multipartFile,
                                   @RequestParam(value = "category") String category,
                                   @RequestParam(value = "classificlevel") Integer classificlevel,
                                   @RequestParam(value = "fileName", required = false) String fileName,
                                   @RequestParam(value = "zrr", required = false) String zrr,
                                   @RequestParam(value = "fileType") String fileType,
                                   @RequestParam(value = "auditUser") String auditUser,
                                   @RequestParam(value = "fileClassify",required = false) Integer fileClassify,
                                   @RequestParam(value = "filingNum",required = false) String filingNum) {

        result = new JSONObject();



        FileInfoEntity fileInfo = new FileInfoEntity();
        ModuleFileEntity moduleFile = new ModuleFileEntity();
        String fileCode = String.valueOf(System.currentTimeMillis());

        try {

            /* 保存文件详细信息 */

            /* 电子附件 */
            if (fileType.equals("1")) {
                String originalFilename = multipartFile.getOriginalFilename();

                File file = new File(filePath + "/" + fileCode);
                multipartFile.transferTo(file);

                fileName = originalFilename;

                fileInfo.setFileName(originalFilename);
                fileInfo.setOriginalFileName(multipartFile.getOriginalFilename());
                fileInfo.setFilePath(file.getAbsolutePath());
                fileInfo.setFileSize(multipartFile.getSize());
                fileInfo.setFileType(fileType);
                fileInfo.setStatus(1);

                result.put("fileName", originalFilename);
            } else {
                fileName = filingNum;
                /* 纸质附件 */
                fileInfo.setFileName(fileName);
                fileInfo.setZrr(zrr);
                fileInfo.setFileType(fileType);
                fileInfo.setStatus(1);
                fileInfo.setFilingNum(filingNum);

                result.put("fileName", fileName);
            }

            fileInfo.setFileCode(fileCode);
            fileInfo.setClassificlevelId(classificlevel);
            fileInfo.setFileClassify(fileClassify);
            fileInfo.setCreator(getUser().getUsername());
            fileInfo.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            FileInfoEntity infoEntity = fileInfoRepository.save(fileInfo);


            if (fileType.equals("1")){

                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setFileId(infoEntity.getId());
                auditInfo.setApplicant(getUser().getUsername());
                auditInfo.setApplicationTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                auditInfo.setFileName(infoEntity.getFileName());
                auditInfo.setIsAudit(0);
                auditInfo.setType("UPLOAD");
                auditInfo.setFileClassify(fileInfo.getFileClassify());
                auditInfo.setClassificlevelId(fileInfo.getClassificlevelId());
                auditInfo.setAuditUser(auditUser);
                auditInfoRepository.save(auditInfo);
            }




            /* 保存基础信息 */

            moduleFile.setType(category);
            //TODO 需要修改
            moduleFile.setFileCode(fileInfo.getFileCode());
            moduleFile.setUserId(getUser().getId());
            moduleFile.setCreator(getUser().getUsername());
            moduleFile.setCreateTime(new Timestamp(System.currentTimeMillis()));

            ModuleFileEntity moduleFileEntity = moduleFileRespository.save(moduleFile);


            result.put("mid", moduleFileEntity.getId());
            msg = SUCCESS;

        } catch (IOException e) {
            logger.error("上传附件:" + e.getMessage());
            msg = "IOException";
        }

        operationLogInfo = "用户【"+getUser().getRealName()+"】上传附件【"+fileName+"】";
        result.put("operationLog",operationLogInfo);
        result.put("msg", msg);

        return result.toString();
    }

    /**
     * 获取文件
     *
     * @param mid
     * @param type
     * @param response
     */
    @ArchivesLog(operationType = "getFile", operationName = "获取文件")
    @RequestMapping(value = "/getFile")
    public void getFile(@RequestParam(value = "mid") Long mid,
                        @RequestParam(value = "type", required = false) String type,
                        HttpServletResponse response) {
        OperationLog operation = new OperationLog();
        operation.setOperationStartTime(dateFormat.format(new Date()));


        File file;
        String fileType,fileName = null;
        if (null != type) {
            MessageAttach messageAttach = msgAttachRepository.findOne(mid);
            file = new File(messageAttach.getFilePath());
            fileType = messageAttach.getFileName().substring(messageAttach.getFileName().lastIndexOf(".") + 1).toLowerCase();

        } else {
            ModuleFileEntity fileEntity = moduleFileRespository.findOne(mid);
            FileInfoEntity fileInfoEntity = fileInfoRepository.findByFileCode(fileEntity.getFileCode());
            fileName = fileInfoEntity.getOriginalFileName();

            fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            file = new File(fileInfoEntity.getFilePath());

        }

        operation.setOperationDescrib("用户【"+getUser().getRealName()+"】下载文件【"+fileName+"】");
        operation.setOperationUserId(getUser().getId());
        operation.setOperationEndTime(dateFormat.format(new Date()));
        operation.setOperationUserName(getUser().getUsername());
        operation.setOperationType("getFile");





        //判断文件是否存在如果不存在就返回默认图标
//        if(!(file.exists() && file.canRead())) {
//            file = new File(request.getSession().getServletContext().getRealPath("/")
//                    + "resource/icons/auth/root.png");
//        }

        try {
            FileInputStream inputStream = new FileInputStream(file);
            int length = inputStream.available();
            byte data[] = new byte[length];
            response.setContentLength(length);
            response.setContentType(ImageUploadUtil.imageContentType.get(fileType));
            inputStream.read(data);
            OutputStream out = response.getOutputStream();
            out.write(data);
            out.flush();
            operation.setOperationResult("成功");
        } catch (FileNotFoundException e) {
            operation.setOperationResult("失败");
            logger.error("获取文件:" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        operationRepository.save(operation);


    }

    /**
     * 上传编辑图片
     *
     * @param request
     * @param response
     * @return
     */
    @ArchivesLog(operationType = "uploadImage", operationName = "上传编辑图片")
    @RequestMapping(value = "/uploadImage")
    @ResponseBody
    public String uploadImage(HttpServletRequest request, HttpServletResponse response) {
        result = new JSONObject();
        try {

            String DirectoryName = imgPath;
            String serverImgUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "" + request.getContextPath() + "/getFile";
            try {
                ImageUploadUtil.ckeditor(request, response, DirectoryName, serverImgUrl);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            msg = SUCCESS;

        } catch (Exception e) {
            logger.error("上传编辑图片:" + e.getMessage());
            msg = "Exception";
        }
        operationLogInfo = "用户【"+getUser().getRealName()+"】上传编辑图片";
        result.put("operationLog",operationLogInfo);
        result.put("msg", msg);
        return result.toString();
    }

    /**
     * 新建资料页面
     *
     * @return
     */
    @ArchivesLog(operationType = "createData", operationName = "新建资料页面")
    @RequestMapping(value = "/createData")
    public ModelAndView createData(@RequestParam(value = "zid", required = false) Long zid) {

        ZiliaoInfoEntity ziliaoInfoEntity;

        List<User> auditUser = userRepository.findAuditUser();

        if (null == zid) {
            ziliaoInfoEntity = new ZiliaoInfoEntity();
            ziliaoInfoEntity.setCreateTime(DateUtil.parseDateToStr(new Date(),DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        } else {
            ziliaoInfoEntity = dataRepository.findOne(zid);
        }


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("data/createData");
        modelAndView.addObject("info", ziliaoInfoEntity);
        modelAndView.addObject("proentity",ziliaoInfoEntity);
        modelAndView.addObject("mid",5);
        modelAndView.addObject("levelId",getUser().getPermissionLevel());
        modelAndView.addObject("auditUsers",auditUser);
        return modelAndView;
    }

    /**
     * 下载资料模板
     *
     * @param response
     * @throws IOException
     */
    @ArchivesLog(operationType = "downLoadDataTempate", operationName = "下载资料模板")
    @RequestMapping(value = "/downLoadDataTempate")
    public void downLoadDataTempate(HttpServletResponse response,
                                    @RequestParam(value = "fileName") String fileName) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="+ URLEncoder.encode(fileName));
        try {
//            InputStream inputStream = new FileInputStream(new File(dataTemplatePath + File.separator + fileName));
            System.out.println(this.getClass().getResource("/").getPath());
            InputStream inputStream = new FileInputStream(this.getClass().getResource("/").getPath() +"downfiles/"+ fileName);

            OutputStream os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }

            os.close();

            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 资料列表
     *
     * @return
     */
    @ArchivesLog(operationType = "dataList", operationName = "资料列表")
    @RequestMapping(value = "/dataList")
    ModelAndView dataList(@RequestParam(value = "typeId", required = false) Integer typeId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("data/list");
        ZiliaoInfoEntity ziliaoInfoEntity = new ZiliaoInfoEntity();
        ziliaoInfoEntity.setTypeId(typeId);
        modelAndView.addObject(ziliaoInfoEntity);


        return modelAndView;
    }


    /**
     * 删除资料
     * @param ids
     * @return
     */
    @ArchivesLog(operationType = "delData", operationName = "删除资料")
    @RequestMapping(value = "/delData")
    @ResponseBody
    String delData(Long[] ids) {
        operationLogInfo = "用户【" + getUser().getRealName() + "】删除资料【";

        try{

            if (null != ids && ids.length >0){
                for (Long id:ids){
                    ZiliaoInfoEntity entity = dataRepository.findOne(id);
                    operationLogInfo += entity.getTitle() + ",";
                    dataRepository.delete(entity);

                }
            }
            msg = SUCCESS;

        }catch (Exception e){
            logger.error(e.getMessage());
            msg = "delete data failed";

        }


        operationLogInfo = operationLogInfo.substring(0, operationLogInfo.length() - 1) + "】";
        result.put("msg", msg);
        result.put("operationLog", operationLogInfo);
        return result.toString();
    }

        /**
         * 查询资料列表数据
         *
         * @param page
         * @param size
         * @param searchValue
         * @return
         */
    @ArchivesLog(operationType = "datas", operationName = "查询资料列表")
    @RequestMapping(value = "/datas")
    @ResponseBody
    String datas(@RequestParam(value = "pageIndex", defaultValue = "1") Integer page,
                 @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                 @RequestParam(value = "searchValue",required = false) String searchValue,
                 @RequestParam(value = "typeId",required = false) Long typeId,
                 @RequestParam(value = "sortOrder", required = false, defaultValue = "asc") String sortOrder,
                 @RequestParam(value = "sortName", required = false, defaultValue = "id") String sortName) {

        result = new JSONObject();
        page = page - 1;
        Sort sort = new Sort(sortOrder.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortName);
        Pageable pageable = new PageRequest(page, size, sort);

        List<Long> ziliaoAuditRes = dataRepository.findZiliaoAuditRes();

        List<Long> selfZiliaoIds = dataRepository.findIdByCreator(getUser().getUsername());

        ziliaoAuditRes.addAll(selfZiliaoIds);


        Specification<ZiliaoInfoEntity> specification = (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            List<Predicate> predicatesWhereArr = new ArrayList<>();

            if (ziliaoAuditRes.size() == 0){
                predicatesWhereArr.add(criteriaBuilder.equal(root.get("classificlevelId"),"9999"));
                return criteriaBuilder.and(predicatesWhereArr.toArray(new Predicate[predicates.size()]));
            }

            if (null == typeId && StringUtils.isEmpty(searchValue)) {
                predicatesWhereArr.add(criteriaBuilder.lessThanOrEqualTo(root.get("classificlevelId"),getUser().getPermissionLevel()));
                Expression<String> exp = root.<String>get("id");
                if (ziliaoAuditRes.size() > 0){
                    predicatesWhereArr.add(exp.in(ziliaoAuditRes));
                }
                return criteriaBuilder.and(predicatesWhereArr.toArray(new Predicate[predicates.size()]));
            }


            if (null != typeId) {
                predicatesWhereArr.add(criteriaBuilder.equal(root.get("typeId"), typeId));
            }

            Expression<String> exp = root.<String>get("id");
            if (ziliaoAuditRes.size() > 0){
                predicatesWhereArr.add(exp.in(ziliaoAuditRes));
            }
            predicatesWhereArr.add(criteriaBuilder.lessThanOrEqualTo(root.get("classificlevelId"),getUser().getPermissionLevel()));


            Predicate whereEquals = criteriaBuilder.and(predicatesWhereArr.toArray(new Predicate[predicatesWhereArr.size()]));


            if (StringUtils.isNotEmpty(searchValue)) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("author"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("publishDate"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("ziliaoFrom"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("ziliaoContent"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("creator"), "%" + searchValue + "%"));
                predicates.add(criteriaBuilder.like(root.get("createTime"), "%" + searchValue + "%"));
            }

            Predicate whereLike = criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));

            List<Predicate> predicateArr = new ArrayList<>();
            if (predicatesWhereArr.size() > 0){
                predicateArr.add(whereEquals);
            }
            if (predicates.size() > 0){
                predicateArr.add(whereLike);
            }
            if (ziliaoAuditRes.size() > 0){
                predicatesWhereArr.add(exp.in(ziliaoAuditRes));
            }

            return criteriaQuery.where(predicateArr.toArray(new Predicate[predicateArr.size()])).getRestriction();
        };

        Page<ZiliaoInfoEntity> datas = null;
        try {
            datas = dataRepository.findAll(specification, pageable);
            msg = SUCCESS;
        } catch (Exception e) {
            logger.error("资料列表数据:" + e.getMessage());
            msg = "Exception";
        }


        JsonConfig jsonConfig = new JsonConfig();

        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

        JSONArray json = JSONArray.fromObject(datas, jsonConfig);

        operationLogInfo = "用户【" + getUser().getRealName() + "】查询资料列表";
        result.put("msg", msg);
        result.put("operationLog", operationLogInfo);
        result.put("result", json);
        return result.toString();
    }


    protected User getUser() {
        Subject subject = SecurityUtils.getSubject();
        return (User) subject.getPrincipal();
    }


    public void downloadFile(File file, HttpServletResponse response, boolean isDelete) {
        try {
            // 以流的形式下载文件。
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"));
            toClient.write(buffer);
            toClient.flush();
            toClient.close();

            if (isDelete) {
                file.delete();        //是否将生成的服务器端文件删除
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @ArchivesLog(operationType = "getFileAuditResult", operationName = "查询审核结果")
    @RequestMapping(value = "/getFileAuditResult")
    @ResponseBody
    String getFileAuditResult(@RequestParam(value = "fileId") Long fileId,
                              @RequestParam(value = "type") String type,
                              @RequestParam(value = "auditUser",required = false) String auditUser,
                              @RequestParam(value = "classificlevelId",required = false) Integer classificlevelId) {

        result = new JSONObject();
        AuditInfo auditInfo;
        if (type.equals("UPLOAD")){
            auditInfo = auditInfoRepository.findByFileIdAndType(fileId, type);
        }else {
            auditInfo = auditInfoRepository.findByFileIdAndApplicantAndType(fileId, getUser().getUsername(),type);
        }


        if (auditInfo == null){

            auditInfo = new AuditInfo();
            //下载单个文件
            if (null == classificlevelId || classificlevelId.equals(0)){
                FileInfoEntity fileInfo = fileInfoRepository.findById(fileId);
                auditInfo.setFileId(fileInfo.getId());
                auditInfo.setFileName(fileInfo.getFileName());
                auditInfo.setFileClassify(fileInfo.getFileClassify());
                auditInfo.setClassificlevelId(fileInfo.getClassificlevelId());
            //下载整个附件
            }else {
                auditInfo.setClassificlevelId(classificlevelId);
                /**
                 * 1 项目文件
                 * 2 案例库文件
                 * 3 资料库文件
                 * 4 专家库文件
                 */
                auditInfo.setFileId(fileId);
                if (classificlevelId.equals(2)){
                    auditInfo.setFileName(anliRepository.findOne(fileId).getTitle());
                    auditInfo.setFileClassify(2);
                }else if (classificlevelId.equals(3)){
                    auditInfo.setFileName(dataRepository.findOne(fileId).getTitle());
                    auditInfo.setFileClassify(3);
                }else if (classificlevelId.equals(4)){
                    auditInfo.setFileName(expertRepository.findOne(fileId).getName());
                    auditInfo.setFileClassify(4);
                }

            }

            auditInfo.setApplicant(getUser().getUsername());
            auditInfo.setApplicationTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            auditInfo.setIsAudit(0);
            auditInfo.setType(type);

            auditInfoRepository.save(auditInfo);
        }

        if (auditInfo.getAuditUser() == null || auditInfo.getAuditUser().equals("")){
            if (auditUser != null && !auditUser.equals("")){
                auditInfo.setAuditUser(auditUser);
                auditInfoRepository.save(auditInfo);
                result.put("auditResult","0");
                result.put("type",auditInfo.getType());
                return result.toString();
            }else {
                result.put("auditResult","2");
                result.put("type",auditInfo.getType());
                return result.toString();
            }

        }
        if (!auditInfo.getApplicant().equals(getUser().getUsername()) && auditInfo.getIsAudit() != 1){
            result.put("finalResult",-1);
        }

        result.put("auditResult",auditInfo.getIsAudit());
        result.put("type",auditInfo.getType());
        return result.toString();
    }


    @ArchivesLog(operationType = "ReqFileDownLoad", operationName = "请求下载项目文件")
    @RequestMapping(value = "/ReqFileDownLoad")
    @ResponseBody
    String ReqFileDownLoad(@RequestParam(value = "fileId") String fileIds,
                              @RequestParam(value = "type") String type,
                           @RequestParam(value = "daudituser") String auditUser) {

        result = new JSONObject();

        String[] fileIdArr=fileIds.split(",");
        AuditInfo auditInfo = new AuditInfo();
        for(String fileId:fileIdArr){

            FileInfoEntity fileInfo = fileInfoRepository.findById(Long.parseLong(fileId));
            auditInfo.setFileId(fileInfo.getId());
            auditInfo.setApplicant(getUser().getUsername());
            auditInfo.setApplicationTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            auditInfo.setFileName(fileInfo.getFileName());
            auditInfo.setIsAudit(0);
            auditInfo.setType("DOWNLOAD");
            auditInfo.setAuditUser(auditUser);
            auditInfo.setFileClassify(fileInfo.getFileClassify());
            auditInfo.setClassificlevelId(fileInfo.getClassificlevelId());

            auditInfoRepository.save(auditInfo);
        }
        result.put("auditResult",auditInfo.getIsAudit());
        result.put("type",auditInfo.getType());
        return result.toString();
    }

}
