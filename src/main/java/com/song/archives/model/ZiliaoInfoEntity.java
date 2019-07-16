package com.song.archives.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ghl on 2018/2/26.
 */
@Entity
@Table(name = "ziliao_info", schema = "", catalog = "")
public class ZiliaoInfoEntity implements Serializable{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer typeId;
    private String title;
    private String author;
    private String publishDate;
    private String ziliaoFrom;
    @Lob
    private String ziliaoContent;
    private Integer classificlevelId;
    private String creator;
    private String createTimes;
    private String keyword;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getZiliaoFrom() {
        return ziliaoFrom;
    }

    public void setZiliaoFrom(String ziliaoFrom) {
        this.ziliaoFrom = ziliaoFrom;
    }

    public String getZiliaoContent() {
        return ziliaoContent;
    }

    public void setZiliaoContent(String ziliaoContent) {
        this.ziliaoContent = ziliaoContent;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "{" +
                "标题='" + title + '\'' +
                ", 作者='" + author + '\'' +
                ", 发布日期='" + publishDate + '\'' +
                ", 资料来源='" + ziliaoFrom + '\'' +
                ", 摘要='" + ziliaoContent + '\'' +
                ", 密级=" + classificlevelId +
                ", 创建人='" + creator + '\'' +
                ", 创建时间='" + createTimes + '\'' +
                ", 关键词='" + keyword + '\'' +
                '}';
    }
}
