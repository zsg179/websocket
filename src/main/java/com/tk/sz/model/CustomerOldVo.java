package com.tk.sz.model;

import java.util.Date;

public class CustomerOldVo {
    private Integer id;

    private String name;

    private String votefalg;

    private String voteresult;

    private Date createdate;

    private Date updatedate;

    private String usercode;

    private String votecode;
    
    public String getVotecode() {
		return votecode;
	}

	public void setVotecode(String votecode) {
		this.votecode = votecode;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getVotefalg() {
        return votefalg;
    }

    public void setVotefalg(String votefalg) {
        this.votefalg = votefalg == null ? null : votefalg.trim();
    }

    public String getVoteresult() {
        return voteresult;
    }

    public void setVoteresult(String voteresult) {
        this.voteresult = voteresult == null ? null : voteresult.trim();
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode == null ? null : usercode.trim();
    }
}