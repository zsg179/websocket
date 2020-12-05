package com.tk.sz.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.tk.sz.model.UserVo;


@Repository
@Mapper
public interface UserVoMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(UserVo record);

	int insertSelective(UserVo record);

	UserVo selectByPrimaryKey(@Param("id") Integer id);

	int updateByPrimaryKeySelective(UserVo record);

	int updateByPrimaryKey(UserVo record);

	List<UserVo> selectByUserVo(UserVo user);
}