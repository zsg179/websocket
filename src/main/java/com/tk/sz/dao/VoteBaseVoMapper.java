package com.tk.sz.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.tk.sz.model.VoteBaseVo;

@Repository
@Mapper
public interface VoteBaseVoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VoteBaseVo record);

    int insertSelective(VoteBaseVo record);

    VoteBaseVo selectByPrimaryKey(@Param("id")Integer id);
    
    VoteBaseVo selectByVoteCode(@Param("votecode")String  votecode);

    int updateByPrimaryKeySelective(VoteBaseVo record);

    int updateByPrimaryKey(VoteBaseVo record);
    
    
    int  updateFistPrize(@Param("votecode")String  votecode); 
    int  updateSencondPrize(@Param("votecode")String  votecode); 
    int  updateThirdPrize(@Param("votecode")String  votecode); 
    int  updateSumPrize(@Param("votecode")String  votecode); 
    
    int  updateFourthPrice(@Param("votecode")String  votecode); 
    int  updateFifthPrice(@Param("votecode")String  votecode); 
}