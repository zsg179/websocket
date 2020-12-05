package com.tk.sz.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.tk.sz.model.CustomerOldVo;

@Repository
@Mapper
public interface CustomerOldVoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerOldVo record);

    int insertSelective(CustomerOldVo record);

    CustomerOldVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerOldVo record);

    int updateByPrimaryKey(CustomerOldVo record);
    
    List<CustomerOldVo> selectByName (@Param("name") String name , @Param("votecode") String voteCode) ;
    
    int updateByName(CustomerOldVo record) ;
    
}