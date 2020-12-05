package com.tk.sz.model;

public class VoteUserVo {
	private Integer id;

	private String usercode;

	private String username;

	private String userdeptchannel;

	private String userdept;

	private String usertel;

	private Integer votetimes;

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

	public String getUserdeptchannel() {
		return userdeptchannel;
	}

	public void setUserdeptchannel(String userdeptchannel) {
		this.userdeptchannel = userdeptchannel == null ? null : userdeptchannel.trim();
	}

	public String getUserdept() {
		return userdept;
	}

	public void setUserdept(String userdept) {
		this.userdept = userdept == null ? null : userdept.trim();
	}

	public String getUsertel() {
		return usertel;
	}

	public void setUsertel(String usertel) {
		this.usertel = usertel == null ? null : usertel.trim();
	}

	public Integer getVotetimes() {
		return votetimes;
	}

	public void setVotetimes(Integer votetimes) {
		this.votetimes = votetimes;
	}
}