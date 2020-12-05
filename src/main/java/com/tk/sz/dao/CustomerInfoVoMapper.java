package com.tk.sz.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.tk.sz.model.CustomerInfoVo;

@Repository
@Mapper
public interface CustomerInfoVoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerInfoVo record);

    int insertSelective(CustomerInfoVo record);

    CustomerInfoVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerInfoVo record);

    int updateByPrimaryKey(CustomerInfoVo record);
    
    List<CustomerInfoVo> selectByName (@Param("name") String name,@Param("telphone") String telphone ,@Param("votecode") String voteCode) ;
    
    List<Map>  selectAllVoteInfo (@Param("votecode") String voteCode );
    
    List<Map>  selectLqVoteInfo (@Param("votecode") String voteCode );
    
    List<CustomerInfoVo>  isHaveSub (@Param("identity") String identity  , @Param("votecode") String voteCode );
    
    /**
     * 广电查询
     * @param votecode
     * @return
     */
    List<CustomerInfoVo> selectByVoteCode (@Param("votecode") String votecode);
    
}