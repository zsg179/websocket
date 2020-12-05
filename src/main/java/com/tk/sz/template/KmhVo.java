package com.tk.sz.template;

import java.util.List;

/**
 * @author zhusg02
 * @date 2020/11/26 16:27
 */
public class KmhVo {
    private List<KmhProductDto> productList;
    private List<KmhDeptDto> deptList;
    private List<KmhEmpDto> empList;
    private Integer totalNum;

    public List<KmhProductDto> getProductList() {
        return productList;
    }

    public void setProductList(List<KmhProductDto> productList) {
        this.productList = productList;
    }

    public List<KmhDeptDto> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<KmhDeptDto> deptList) {
        this.deptList = deptList;
    }

    public List<KmhEmpDto> getEmpList() {
        return empList;
    }

    public void setEmpList(List<KmhEmpDto> empList) {
        this.empList = empList;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    @Override
    public String toString() {
        return "KmhVo{" +
                "productList=" + productList +
                ", deptList=" + deptList +
                ", empList=" + empList +
                ", totalNum=" + totalNum +
                '}';
    }
}
