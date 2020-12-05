package com.tk.sz.dao;

import com.tk.sz.template.KmhDeptDto;
import com.tk.sz.template.KmhEmpDto;
import com.tk.sz.template.KmhProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.tk.sz.model.VoteAnswerVo;

import java.util.List;

@Repository
@Mapper
public interface VoteAnswerVoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VoteAnswerVo record);

    int insertSelective(VoteAnswerVo record);

    VoteAnswerVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VoteAnswerVo record);

    int updateByPrimaryKey(VoteAnswerVo record);

    List<KmhProductDto> getProductData(String voteCode);

    List<KmhDeptDto> getDeptData(String voteCode);

    List<KmhEmpDto> getEmpData(String voteCode);

    Integer getTotalNum(String voteCode);

    Integer isExist(@Param("voteCode") String voteCode,@Param("name") String name,@Param("tel") String tel);

}