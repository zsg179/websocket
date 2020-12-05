package com.tk.sz.model;

import java.util.Date;

public class VoteLogVo {
    private Integer id;

    private String votecode;

    private String usercode;

    private Date opendate;

    private String openip;

    private String openadress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVotecode() {
        return votecode;
    }

    public void setVotecode(String votecode) {
        this.votecode = votecode == null ? null : votecode.trim();
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode == null ? null : usercode.trim();
    }

    public Date getOpendate() {
        return opendate;
    }

    public void setOpendate(Date opendate) {
        this.opendate = opendate;
    }

    public String getOpenip() {
        return openip;
    }

    public void setOpenip(String openip) {
        this.openip = openip == null ? null : openip.trim();
    }

    public String getOpenadress() {
        return openadress;
    }

    public void setOpenadress(String openadress) {
        this.openadress = openadress == null ? null : openadress.trim();
    }
}