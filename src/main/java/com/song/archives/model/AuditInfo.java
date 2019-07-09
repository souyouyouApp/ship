package com.song.archives.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "audit_info")
public class AuditInfo implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //操作类别 下载DOWN 上传UPLOAD 处理OPERATE
    private String type;
    private Integer isAudit;
    private String auditUser;
    private String auditTime;
    private String auditContent;
    private String applicant;
    private String applicationTime;
    private String fileName;
    private Long fileId;
    private Integer classificlevelId;
    private String fileType;
    private String fileCode;

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * 1 项目文件
     * 2 案例库文件
     * 3 资料库文件
     * 4 公告库文件
     */


    private Integer fileClassify;

    public Integer getClassificlevelId() {
        return classificlevelId;
    }

    public void setClassificlevelId(Integer classificlevelId) {
        this.classificlevelId = classificlevelId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(Integer isAudit) {
        this.isAudit = isAudit;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditContent() {
        return auditContent;
    }

    public void setAuditContent(String auditContent) {
        this.auditContent = auditContent;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getApplicationTime() {
        return applicationTime;
    }

    public void setApplicationTime(String applicationTime) {
        this.applicationTime = applicationTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Integer getFileClassify() {
        return fileClassify;
    }

    public void setFileClassify(Integer fileClassify) {
        this.fileClassify = fileClassify;
    }
}
