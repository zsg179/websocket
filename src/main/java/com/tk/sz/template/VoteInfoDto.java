package com.tk.sz.template;

import java.util.Date;

public class VoteInfoDto {

	private String usercode;

	private String username;

	private String userdeptchannel;

	private String userdept;

	private String usertel;

	private Integer votetimes;

	private String createdate;

	private String price;

	private String answer1;
	private String answer2;
	private String answer3;
	private String answer4;

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	public String getAnswer4() {
		return answer4;
	}

	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserdeptchannel() {
		return userdeptchannel;
	}

	public void setUserdeptchannel(String userdeptchannel) {
		this.userdeptchannel = userdeptchannel;
	}

	public String getUserdept() {
		return userdept;
	}

	public void setUserdept(String userdept) {
		this.userdept = userdept;
	}

	public String getUsertel() {
		return usertel;
	}

	public void setUsertel(String usertel) {
		this.usertel = usertel;
	}

	public Integer getVotetimes() {
		return votetimes;
	}

	public void setVotetimes(Integer votetimes) {
		this.votetimes = votetimes;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
