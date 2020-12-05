package com.tk.sz.model;

import com.alibaba.fastjson.annotation.JSONField;

public class InsureInfoVo {
    private Integer id;

    @JSONField(name = "applicantName")
    private String applicantName;

    private String applicantTel;

    private String applicantAge;

    private String applicantSex;

    private String insuredName;

    private String insuredAge;

    private String insuredSex;

    private String insuredRelation;

    private String productCode;

    private String company;

    private String department;

    
    private String  ownerCode;
    
    private String  ownerName;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

   
    public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public String getApplicantTel() {
		return applicantTel;
	}

	public void setApplicantTel(String applicantTel) {
		this.applicantTel = applicantTel;
	}

	public String getApplicantAge() {
		return applicantAge;
	}

	public void setApplicantAge(String applicantAge) {
		this.applicantAge = applicantAge;
	}

	public String getApplicantSex() {
		return applicantSex;
	}

	public void setApplicantSex(String applicantSex) {
		this.applicantSex = applicantSex;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getInsuredAge() {
		return insuredAge;
	}

	public void setInsuredAge(String insuredAge) {
		this.insuredAge = insuredAge;
	}

	public String getInsuredSex() {
		return insuredSex;
	}

	public void setInsuredSex(String insuredSex) {
		this.insuredSex = insuredSex;
	}

	public String getInsuredRelation() {
		return insuredRelation;
	}

	public void setInsuredRelation(String insuredRelation) {
		this.insuredRelation = insuredRelation;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }
}