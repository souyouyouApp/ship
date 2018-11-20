package com.song.archives.model;

import javax.persistence.*;

@Entity
@Table(name = "storage_info", schema = "", catalog = "")
public class StorageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double totalAmount;

    private double currentUsed;

    private double projectAmount;

    private double anliAmount;

    private double ziliaoAmount;

    private double gongaoAmount;

    private double otherAmount;

    private double dbAmount;

    private double expertAmount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Basic
    @Column(name = "totalAmount")
    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    @Basic
    @Column(name = "currentUsed")
    public double getCurrentUsed() {
        return currentUsed;
    }

    public void setCurrentUsed(double currentUsed) {
        this.currentUsed = currentUsed;
    }
    @Basic
    @Column(name = "projectAmount")
    public double getProjectAmount() {
        return projectAmount;
    }

    public void setProjectAmount(double projectAmount) {
        this.projectAmount = projectAmount;
    }
    @Basic
    @Column(name = "anliAmount")
    public double getAnliAmount() {
        return anliAmount;
    }

    public void setAnliAmount(double anliAmount) {
        this.anliAmount = anliAmount;
    }
    @Basic
    @Column(name = "ziliaoAmount")
    public double getZiliaoAmount() {
        return ziliaoAmount;
    }

    public void setZiliaoAmount(double ziliaoAmount) {
        this.ziliaoAmount = ziliaoAmount;
    }
    @Basic
    @Column(name = "otherAmount")
    public double getOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(double otherAmount) {
        this.otherAmount = otherAmount;
    }
    @Basic
    @Column(name = "gonggaoAmount")
    public double getGongaoAmount() {
        return gongaoAmount;
    }

    public void setGongaoAmount(double gongaoAmount) {
        this.gongaoAmount = gongaoAmount;
    }

    public double getDbAmount() {
        return dbAmount;
    }

    public void setDbAmount(double dbAmount) {
        this.dbAmount = dbAmount;
    }

    public double getExpertAmount() {
        return expertAmount;
    }

    public void setExpertAmount(double expertAmount) {
        this.expertAmount = expertAmount;
    }
}
