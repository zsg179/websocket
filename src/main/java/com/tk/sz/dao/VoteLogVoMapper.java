package com.tk.sz.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.tk.sz.model.VoteLogVo;

@Repository
@Mapper
public interface VoteLogVoMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(VoteLogVo record);

	int insertSelective(VoteLogVo record);

	VoteLogVo selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(VoteLogVo record);

	int updateByPrimaryKey(VoteLogVo record);
}