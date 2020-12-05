package com.tk.sz.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class UserVo {
	@JSONField(name = "id")
	private Integer id;

	@JSONField(name = "usercode")
	private String usercode;

	@JSONField(name = "username")
	private String username;

	private String userpw;

	private Date createdate;

	private String createcode;

	private Date updatedate;

	private String updatecode;

	@JSONField(name = "telphone")
	private String telphone;

	private String deptment;

	private String workcode;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode == null ? null : usercode.trim();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

	public String getUserpw() {
		return userpw;
	}

	public void setUserpw(String userpw) {
		this.userpw = userpw == null ? null : userpw.trim();
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getCreatecode() {
		return createcode;
	}

	public void setCreatecode(String createcode) {
		this.createcode = createcode == null ? null : createcode.trim();
	}

	public Date getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

	public String getUpdatecode() {
		return updatecode;
	}

	public void setUpdatecode(String updatecode) {
		this.updatecode = updatecode == null ? null : updatecode.trim();
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone == null ? null : telphone.trim();
	}

	public String getDeptment() {
		return deptment;
	}

	public void setDeptment(String deptment) {
		this.deptment = deptment;
	}

	public String getWorkcode() {
		return workcode;
	}

	public void setWorkcode(String workcode) {
		this.workcode = workcode;
	}

}