package com.song.archives.model;

import javax.persistence.*;

@Entity
@Table(name = "storage_info", schema = "", catalog = "")
public class StorageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String totalAmount;

    private String currentUsed;

    private String projectAmount;

    private String anliAmount;

    private String ziliaoAmount;

    private String gongaoAmount;

    private String otherAmount;

    private String dbAmount;

    private String expertAmount;

    private String logAmount;

    private String alertAmount;

    private String logSaveTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Basic
    @Column(name = "totalAmount")
    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
    @Basic
    @Column(name = "currentUsed")
    public String getCurrentUsed() {
        return currentUsed;
    }

    public void setCurrentUsed(String currentUsed) {
        this.currentUsed = currentUsed;
    }
    @Basic
    @Column(name = "projectAmount")
    public String getProjectAmount() {
        return projectAmount;
    }

    public void setProjectAmount(String projectAmount) {
        this.projectAmount = projectAmount;
    }
    @Basic
    @Column(name = "anliAmount")
    public String getAnliAmount() {
        return anliAmount;
    }

    public void setAnliAmount(String anliAmount) {
        this.anliAmount = anliAmount;
    }
    @Basic
    @Column(name = "ziliaoAmount")
    public String getZiliaoAmount() {
        return ziliaoAmount;
    }

    public void setZiliaoAmount(String ziliaoAmount) {
        this.ziliaoAmount = ziliaoAmount;
    }
    @Basic
    @Column(name = "otherAmount")
    public String getOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(String otherAmount) {
        this.otherAmount = otherAmount;
    }
    @Basic
    @Column(name = "gonggaoAmount")
    public String getGongaoAmount() {
        return gongaoAmount;
    }

    public void setGongaoAmount(String gongaoAmount) {
        this.gongaoAmount = gongaoAmount;
    }

    public String getDbAmount() {
        return dbAmount;
    }

    public void setDbAmount(String dbAmount) {
        this.dbAmount = dbAmount;
    }

    public String getExpertAmount() {
        return expertAmount;
    }

    public void setExpertAmount(String expertAmount) {
        this.expertAmount = expertAmount;
    }

    @Basic
    @Column(name = "logAmount")
    public String getLogAmount() {
        return logAmount;
    }

    public void setLogAmount(String logAmount) {
        this.logAmount = logAmount;
    }

    @Basic
    @Column(name = "alertAmount")
    public String getAlertAmount() {
        return alertAmount;
    }

    public void setAlertAmount(String alertAmount) {
        this.alertAmount = alertAmount;
    }

    @Basic
    @Column(name = "logSaveTime")
    public String getLogSaveTime() {
        return logSaveTime;
    }

    public void setLogSaveTime(String logSaveTime) {
        this.logSaveTime = logSaveTime;
    }
}
