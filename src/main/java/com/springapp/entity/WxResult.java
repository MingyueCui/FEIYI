package com.springapp.entity;

import javax.persistence.*;

/**
 * Created by 11369 on 2016/11/21.
 * 答题情况
 */
@Entity
public class WxResult {
    private Long id;
    private WxUser wxUser;
    private String openid;
    private Integer getPrize;//领奖方式
    private String name; //收货人
    private String tel; //联系电话
    private String address; //发货地址
    private String fetchAddress;//自取地址
    private Integer status;//是否完成
    private Integer validity;//1表示全对

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "wxUser")
    public WxUser getWxUser() {
        return wxUser;
    }

    public void setWxUser(WxUser wxUser) {
        this.wxUser = wxUser;
    }

    public Integer getValidity() {
        return validity;
    }

    public void setValidity(Integer validity) {
        this.validity = validity;
    }

    public Integer getGetPrize() {
        return getPrize;
    }

    public void setGetPrize(Integer getPrize) {
        this.getPrize = getPrize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getFetchAddress() {
        return fetchAddress;
    }

    public void setFetchAddress(String fetchAddress) {
        this.fetchAddress = fetchAddress;
    }
}
