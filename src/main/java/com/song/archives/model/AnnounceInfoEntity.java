package com.song.archives.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by ghl on 2018/2/26.
 */
@Entity
@Table(name = "announce_info", schema = "", catalog = "")
public class AnnounceInfoEntity implements Serializable{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer typeId;
    private String title;
    private String creator;
    private String createTime;
    @Lob
    private String content;
    private String sponsorDate;
    private String deadlineDate;
    private String sponsor;
    private String typeName;
    private String relatedUserName;
    private String relatedUserIds;
    private Integer classificlevelId;

    @Override
    public String toString() {
        return "{" +
                "标题='" + title + '\'' +
                ", 创建人='" + creator + '\'' +
                ", 创建时间='" + createTime + '\'' +
                ", 摘要='" + content + '\'' +
                ", 发起日期='" + sponsorDate + '\'' +
                ", 截止日期='" + deadlineDate + '\'' +
                ", 发布人='" + sponsor + '\'' +
                ", 类别名称='" + typeName + '\'' +
                ", 相关人员='" + relatedUserName + '\'' +
                ", 密级=" + classificlevelId +
                '}';
    }

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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSponsorDate() {
        return sponsorDate;
    }

    public void setSponsorDate(String sponsorDate) {
        this.sponsorDate = sponsorDate;
    }

    public String getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getRelatedUserName() {
        return relatedUserName;
    }

    public void setRelatedUserName(String relatedUserName) {
        this.relatedUserName = relatedUserName;
    }

    public String getRelatedUserIds() {
        return relatedUserIds;
    }

    public void setRelatedUserIds(String relatedUserIds) {
        this.relatedUserIds = relatedUserIds;
    }
}
