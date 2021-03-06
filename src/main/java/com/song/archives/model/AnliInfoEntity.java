package com.song.archives.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by ghl on 2018/2/26.
 */
@Entity
@Table(name = "anli_info", schema = "", catalog = "")
public class AnliInfoEntity implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer typeId;
    private String title;
    private Integer classificlevelId;
    private String creator;
    private String createTimes;
    @Lob
    private String content;

    private String nationality;
    private String caseName;
    private String closeTime;
    private String finalAppeal;
    private String projectName;
    private String projectSource;


    private String principal;
    private String participant;
    private String judicialStatus;

    private String type;

    @Override
    public String toString() {
        return "{" +
                "标题='" + title + '\'' +
                ", 密级=" + classificlevelId +
                ", 创建人='" + creator + '\'' +
                ", 创建时间='" + createTimes + '\'' +
                ", 摘要='" + content + '\'' +
                ", 国别='" + nationality + '\'' +
                ", 案例名称='" + caseName + '\'' +
                ", 结案时间='" + closeTime + '\'' +
                ", 终审法院='" + finalAppeal + '\'' +
                ", 项目名称='" + projectName + '\'' +
                ", 项目来源='" + projectSource + '\'' +
                ", 主要负责人='" + principal + '\'' +
                ", 参与人='" + participant + '\'' +
                ", 状态='" + judicialStatus + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getClassificlevelId() {
        return classificlevelId;
    }

    public void setClassificlevelId(Integer classificlevelId) {
        this.classificlevelId = classificlevelId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateTimes() {
        return createTimes;
    }

    public void setCreateTimes(String createTimes) {
        this.createTimes = createTimes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getFinalAppeal() {
        return finalAppeal;
    }

    public void setFinalAppeal(String finalAppeal) {
        this.finalAppeal = finalAppeal;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectSource() {
        return projectSource;
    }

    public void setProjectSource(String projectSource) {
        this.projectSource = projectSource;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getJudicialStatus() {
        return judicialStatus;
    }

    public void setJudicialStatus(String judicialStatus) {
        this.judicialStatus = judicialStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
