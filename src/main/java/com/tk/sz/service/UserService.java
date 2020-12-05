package com.tk.sz.service;

import java.util.List;

import com.tk.sz.model.CustomerInfoVo;
import com.tk.sz.model.CustomerOldVo;
import com.tk.sz.model.UserVo;
import com.tk.sz.model.VoteAnswerVo;
import com.tk.sz.model.VoteBaseVo;
import com.tk.sz.model.VoteLogVo;
import com.tk.sz.model.VoteUserVo;
import com.tk.sz.template.KmhDeptDto;
import com.tk.sz.template.KmhEmpDto;
import com.tk.sz.template.KmhProductDto;
import com.tk.sz.template.VoteInfoDto;

public interface UserService {

	int insert(UserVo record);

	int insertSelective(UserVo record);

	int deleteByPrimaryKey(Integer id);

	UserVo selectByPrimaryKey(Integer id);

	List<UserVo> selectByUserVo(UserVo user);

	public boolean isHaveSub(String id, String telphone, String voteCode) ;
	
	int updateByPrimaryKey(UserVo record);

	/**
	 * 保存问卷答案
	 * 
	 * @param voteAnswerVo
	 */
	public void saveAnswer(VoteAnswerVo voteAnswerVo);

	/**
	 * 保存客户信息
	 * 
	 * @param customerInfoVo
	 */
	public void saveCustomerInfo(CustomerInfoVo customerInfoVo);

	/**
	 * 参与抽奖
	 * 
	 * @param map
	 */
	public int getPrice(String voteCode);

	/**
	 * 是否老客户
	 * 
	 * @param name
	 * @return
	 */
	public boolean isOldCustomer(String name,String voteCode);

	/**
	 * 是否已参加中奖
	 * 
	 * @param name
	 * @return
	 */
	public boolean isHaveGetPrice(String name,String telphone,String voteCode);

	/**
	 * 更新中奖标志
	 * 
	 * @param name
	 */
	public void updateCustomerOldVoByName(CustomerOldVo record);

	/**
	 * 保存用户浏览记录
	 * @param voteLogVo
	 */
	public void saveLog(VoteLogVo voteLogVo);
	
	public  void  updateFourthPrice(String  votecode);
	
	public  void  updateFifthPrice(String  votecode);
	
	public VoteBaseVo selectVoteBaseVoById(Integer  voteId);
	
	/**
	 * 获取用户信息
	 * @param usercode
	 * @return
	 */
	public VoteUserVo selectVoteUserVoByUserCode(String usercode);
	
	
	public VoteUserVo selectVoteUserVoByUserCodeAndVoteCode(String usercode,String voteCode);
	
	/**
	 * 更新业务可抽奖次数
	 * @param usercode
	 */
	public void  updateByUserCode(String usercode);
	
	
	public void  updateByUserCodeAndVoteCode(String usercode , String voteCode );
	
	
	public List<VoteInfoDto> selectAllVoteInfo(String voteCode) ;
	
	
	/**
	 * 路桥获取用户的信息
	 * @param usercode
	 * @return
	 */
	public  VoteUserVo selectVoteUserVoByUserName(String username);
	
	/**
	 * 路桥获取用户的手机号是否已经参与过
	 * @param usercode
	 * @return
	 */
	public boolean isHaveVote(String name, String telphone, String voteCode);
	
	
	
	/**
	 * 路桥获取查询用户的信息
	 * @param usercode
	 * @return
	 */
	public List<VoteInfoDto> selectLqVoteInfo(String  votecode);
	
	public List<VoteInfoDto> selectSubInfo(String  votecode);

	/**
	 * 开门红获取各项目押注数据
	 *
	 * @return
	 * @author zhusg02
	 * @date 2020/11/26 15:34
	 */
	List<KmhProductDto> getProductData(String voteCode);
	/**
	 *获取前十部门押注金额
	 * @author zhusg02
	 * @date 2020/11/28 22:17
	 * @return java.util.List<com.tk.sz.template.KmhDeptDto>
	 */
	List<KmhDeptDto> getDeptData(String voteCode);

	/**
	 *获取员工押注金额前十名
	 * @author zhusg02
	 * @date 2020/11/29 16:43
	 * @return java.util.List<com.tk.sz.template.KmhEmpDto>
	 */
	List<KmhEmpDto> getEmpData(String voteCode);

	/**
	 *获取总押注金额
	 * @author zhusg02
	 * @date 2020/11/30 8:59
	 * @return java.lang.Integer
	 */
	Integer getTotalNum(String voteCode);

	/**
	 *检查用户是否二次押注
	 * @author zhusg02
	 * @date 2020/12/1 9:15
	 * @return java.lang.Integer
	 */
	Integer isExist(String voteCode,String name,String tel);
}
