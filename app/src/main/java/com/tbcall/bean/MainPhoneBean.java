package com.tbcall.bean;

import java.io.Serializable;

/**
 * 主界面--电话列表对象bean
 */
public class MainPhoneBean implements Serializable {

    public String numberOrName, numberAddr, date;

    public MainPhoneBean(String numberOrName, String numberAddr, String date) {
        this.numberOrName = numberOrName;
        this.numberAddr = numberAddr;
        this.date = date;
    }
}
