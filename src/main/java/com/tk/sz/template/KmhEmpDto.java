package com.tk.sz.template;

/**
 * @author zhusg02
 * @date 2020/11/26 15:34
 */
public class KmhEmpDto {
    private String empName;
    private Integer empMoney;
    private String empDept;

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Integer getEmpMoney() {
        return empMoney;
    }

    public void setEmpMoney(Integer empMoney) {
        this.empMoney = empMoney;
    }

    public String getEmpDept() {
        return empDept;
    }

    public void setEmpDept(String empDept) {
        this.empDept = empDept;
    }

    @Override
    public String toString() {
        return "KmhEmpDto{" +
                "empName='" + empName + '\'' +
                ", empMoney=" + empMoney +
                ", empDept='" + empDept + '\'' +
                '}';
    }
}
