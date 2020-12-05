package com.tk.sz.template;

/**
 * @author zhusg02
 * @date 2020/11/26 15:34
 */
public class KmhDeptDto {
    private String dept;
    private Integer deptMoney;

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public Integer getDeptMoney() {
        return deptMoney;
    }

    public void setDeptMoney(Integer deptMoney) {
        this.deptMoney = deptMoney;
    }

    @Override
    public String toString() {
        return "KmhDeptDto{" +
                "dept='" + dept + '\'' +
                ", deptMoney=" + deptMoney +
                '}';
    }
}
