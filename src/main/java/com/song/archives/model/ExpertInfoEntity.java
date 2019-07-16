package com.song.archives.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;

/**
 * Created by ghl on 2018/2/26.
 */
@Entity
@Table(name = "expert_info", schema = "", catalog = "")
public class ExpertInfoEntity implements Serializable{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer typeId;
    private String name;
    private String gender;
    private String birth;
    private String education;
    private String sxzhuanye;
    private String cszhuanye;
    private String zhiwu;
    private String zhicheng;
    private String profile;
    private String szdanwei;
    private String szbumen;
    private String sslingyu;
    private String address;
    private String postcode;
    private String mobile;
    private String phone;
    private String faxcode;
    private String email;
    private String zjpingjia;
    private Integer pycishu;
    @Lob
    private String pic;
    private Integer classiclevelId;
    private String createTime;
    private String idNo;
    private String remark;
    private String creator;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "{" +
                "姓名='" + name + '\'' +
                ", 性别='" + gender + '\'' +
                ", 出生日期='" + birth + '\'' +
                ", 学历='" + education + '\'' +
                ", 业务专长='" + sxzhuanye + '\'' +
                ", 所属专业='" + cszhuanye + '\'' +
                ", 职务='" + zhiwu + '\'' +
                ", 支撑='" + zhicheng + '\'' +
                ", 所在单位='" + szdanwei + '\'' +
                ", 所在部门='" + szbumen + '\'' +
                ", 所属领域='" + sslingyu + '\'' +
                ", 通信地址='" + address + '\'' +
                ", 邮编='" + postcode + '\'' +
                ", 手机='" + mobile + '\'' +
                ", 座机='" + phone + '\'' +
                ", 传真='" + faxcode + '\'' +
                ", 电子邮件='" + email + '\'' +
                ", 最佳评价='" + zjpingjia + '\'' +
                ", 专家聘用次数=" + pycishu +
                ", 创建时间=" + createTime +
                ", 证件号码='" + idNo + '\'' +
                ", 备注信息='" + remark + '\'' +
                '}';
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSxzhuanye() {
        return sxzhuanye;
    }

    public void setSxzhuanye(String sxzhuanye) {
        this.sxzhuanye = sxzhuanye;
    }

    public String getCszhuanye() {
        return cszhuanye;
    }

    public void setCszhuanye(String cszhuanye) {
        this.cszhuanye = cszhuanye;
    }

    public String getZhiwu() {
        return zhiwu;
    }

    public void setZhiwu(String zhiwu) {
        this.zhiwu = zhiwu;
    }

    public String getZhicheng() {
        return zhicheng;
    }

    public void setZhicheng(String zhicheng) {
        this.zhicheng = zhicheng;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getSzdanwei() {
        return szdanwei;
    }

    public void setSzdanwei(String szdanwei) {
        this.szdanwei = szdanwei;
    }

    public String getSzbumen() {
        return szbumen;
    }

    public void setSzbumen(String szbumen) {
        this.szbumen = szbumen;
    }

    public String getSslingyu() {
        return sslingyu;
    }

    public void setSslingyu(String sslingyu) {
        this.sslingyu = sslingyu;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFaxcode() {
        return faxcode;
    }

    public void setFaxcode(String faxcode) {
        this.faxcode = faxcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZjpingjia() {
        return zjpingjia;
    }

    public void setZjpingjia(String zjpingjia) {
        this.zjpingjia = zjpingjia;
    }

    public Integer getPycishu() {
        return pycishu;
    }

    public void setPycishu(Integer pycishu) {
        this.pycishu = pycishu;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Integer getClassiclevelId() {
        return classiclevelId;
    }

    public void setClassiclevelId(Integer classiclevelId) {
        this.classiclevelId = classiclevelId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
