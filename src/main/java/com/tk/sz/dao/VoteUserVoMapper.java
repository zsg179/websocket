package com.tk.sz.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.tk.sz.model.VoteUserVo;

@Repository
@Mapper
public interface VoteUserVoMapper {
	
	int deleteByPrimaryKey(Integer id);

	int insert(VoteUserVo record);

	int insertSelective(VoteUserVo record);

	VoteUserVo selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(VoteUserVo record);

	int updateByPrimaryKey(VoteUserVo record);

	VoteUserVo selectVoteUserVoByUserCode(String usercode);

	public int updateByUserCode(String usercode);

	List<Map> selectVoteUserVoByUserName(@Param("username") String username);

	
	VoteUserVo selectVoteUserVoByUserCodeAndVoteCode(@Param("usercode") String username ,@Param("votecode") String voteCode);
	

	public int updateByUserCodeAndVoteCode(@Param("usercode")String usercode,@Param("votecode") String voteCode);

}