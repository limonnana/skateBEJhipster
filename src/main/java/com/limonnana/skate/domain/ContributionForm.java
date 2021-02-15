package com.limonnana.skate.domain;

import java.io.Serializable;

public class ContributionForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String amount;
    private Trick trick;
    private String fanId;
    private String fanFullName;
    private String phone;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFanId() {
        return fanId;
    }

    public void setFanId(String fanId) {
        this.fanId = fanId;
    }

    public String getFanFullName() {
        return fanFullName;
    }

    public void setFanFullName(String fanFullName) {
        this.fanFullName = fanFullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Trick getTrick() {
        return trick;
    }

    public void setTrick(Trick trick) {
        this.trick = trick;
    }
}
