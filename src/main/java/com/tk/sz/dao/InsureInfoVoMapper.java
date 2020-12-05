package com.tk.sz.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.tk.sz.model.InsureInfoVo;

@Repository
@Mapper
public interface InsureInfoVoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InsureInfoVo record);

    int insertSelective(InsureInfoVo record);

    InsureInfoVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InsureInfoVo record);

    int updateByPrimaryKey(InsureInfoVo record);
}