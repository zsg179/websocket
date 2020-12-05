package com.tk.sz.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.tk.sz.template.KmhDeptDto;
import com.tk.sz.template.KmhEmpDto;
import com.tk.sz.template.KmhProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tk.sz.dao.CustomerInfoVoMapper;
import com.tk.sz.dao.CustomerOldVoMapper;
import com.tk.sz.dao.UserVoMapper;
import com.tk.sz.dao.VoteAnswerVoMapper;
import com.tk.sz.dao.VoteBaseVoMapper;
import com.tk.sz.dao.VoteLogVoMapper;
import com.tk.sz.dao.VoteUserVoMapper;
import com.tk.sz.model.CustomerInfoVo;
import com.tk.sz.model.CustomerOldVo;
import com.tk.sz.model.UserVo;
import com.tk.sz.model.VoteAnswerVo;
import com.tk.sz.model.VoteBaseVo;
import com.tk.sz.model.VoteLogVo;
import com.tk.sz.model.VoteUserVo;
import com.tk.sz.service.UserService;
import com.tk.sz.template.VoteInfoDto;

@Transactional
@Service
public class UserServiceImpl implements UserService {

	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserVoMapper userMapper;

	@Autowired
	private VoteAnswerVoMapper voteAnswerVoMapper;

	@Autowired
	private VoteBaseVoMapper voteBaseVoMapper;

	@Autowired
	private CustomerOldVoMapper customerOldVoMapper;

	@Autowired
	private CustomerInfoVoMapper customerInfoVoMapper;

	@Autowired
	private VoteLogVoMapper voteLogVoMapper;
	
	@Autowired
	private  VoteUserVoMapper voteUserVoMapper;
	
	
	
	@Override
	public int insert(UserVo record) {
		return this.userMapper.insert(record);
	}

	@Override
	public int insertSelective(UserVo record) {
		return this.insertSelective(record);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return this.userMapper.deleteByPrimaryKey(id);
	}

	@Override
	public UserVo selectByPrimaryKey(Integer id) {
		return this.userMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(UserVo record) {
		return this.userMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<UserVo> selectByUserVo(UserVo user) {
		return this.userMapper.selectByUserVo(user);
	}

	/**
	 * 保存答案
	 */
	@Override
	public void saveAnswer(VoteAnswerVo voteAnswerVo) {

		this.voteAnswerVoMapper.insert(voteAnswerVo);

	}

	/**
	 * 保存用户信息
	 */
	@Override
	public void saveCustomerInfo(CustomerInfoVo customerInfoVo) {

		this.customerInfoVoMapper.insert(customerInfoVo);

	}

	/**
	 * 抽奖服务
	 */
	@Override
	public int getPrice(String  votecode) {
		// 向list容器中顺序添加指定数量num的整数
		ArrayList<Integer> list = new ArrayList<Integer>();

		VoteBaseVo voteBaseVo = voteBaseVoMapper.selectByVoteCode(votecode);
		int firstprize = voteBaseVo.getFirstprize();
		int sencondprize = voteBaseVo.getSecondprize();
		int thirdprize = voteBaseVo.getThirdprize();
		int fourprize = voteBaseVo.getFourprize(); //四等奖
		int totalprize = voteBaseVo.getTotalprize();
		int sumprice = firstprize + sencondprize + thirdprize + fourprize;

		int result = 0;
		if (totalprize > 0 && sumprice == totalprize) {
			for (int i = 1; i <= firstprize; i++) {
				list.add(1);
			}
			for (int i = 1; i <= sencondprize; i++) {
				list.add(2);
			}
			for (int i = 1; i <= thirdprize; i++) {
				list.add(3);
			}
			for (int i = 1; i <= fourprize; i++) {
				list.add(4);
			}
			Collections.shuffle(list);

			Random rdom = new Random();
			int index = rdom.nextInt(list.size());
			result = list.get(index);

			if (result == 1) {
				logger.info("恭喜你获得奖一等奖！     " + index + ": " + list.get(index));
				list.remove(index);
				voteBaseVoMapper.updateFistPrize(votecode);
			}
			if (result == 2) {
				logger.info("恭喜你获得奖二等奖！     " + index + ": " + list.get(index));
				list.remove(index);
				voteBaseVoMapper.updateSencondPrize(votecode);
			}
			if (result == 3) {
				logger.info("恭喜你获得奖三等奖！     " + index + ": " + list.get(index));
				list.remove(index);
				voteBaseVoMapper.updateThirdPrize(votecode);
			}
			if (result == 4) {
				logger.info("恭喜你获得奖四等奖！     " + index + ": " + list.get(index));
				list.remove(index);
				voteBaseVoMapper.updateFourthPrice(votecode);
			}
			voteBaseVoMapper.updateSumPrize(votecode);
		} else {
			result = 0;
		}
		return result;
	}

	/**
	 * 是否老客户
	 */
	@Override
	public boolean isOldCustomer(String name , String voteCode) {
		
		boolean oldCustomerFlag = false;

		List<CustomerOldVo> list = this.customerOldVoMapper.selectByName(name,voteCode);

		if (null != list && list.size() > 0) {
			oldCustomerFlag = true;
		}

		return oldCustomerFlag;

	}

	/**
	 * 是否已参加中奖
	 * 
	 * @param name
	 * @return
	 */
	public boolean isHaveGetPrice(String name,String telphone,String voteCode) {

		boolean flag = false;

		/*List<CustomerOldVo> list = this.customerOldVoMapper.selectByName(name, voteCode);

		if (null != list && list.size() > 0) {
			CustomerOldVo vo = list.get(0);
			if ("1".equals(vo.getVotefalg())) {
				flag = true; //已参与
				return flag;
			}
		} else {
			flag = false;
		}*/

		List<CustomerInfoVo> infolist = this.customerInfoVoMapper.selectByName(name,telphone ,voteCode);

		if (null != infolist && infolist.size() > 0) {
			flag = true;  //已参与
		} else {
			flag = false;
		}

		return flag;
	}
	
	
	
	/**
	 * 广电是否已经提交过
	 * @param name
	 * @param telphone
	 * @param voteCode
	 * @return
	 */
	public boolean isHaveSub(String id, String telphone, String voteCode) {

		boolean flag = false;

		List<CustomerInfoVo> list = this.customerInfoVoMapper.isHaveSub(id, voteCode);

		if (null != list && list.size() > 0) {
			flag = true; // 已参与
			return flag;
		} else {
			flag = false;
		}

		return flag;
	}
	

	public void updateCustomerOldVoByName(CustomerOldVo record) {

		this.customerOldVoMapper.updateByName(record);

	}
	
	/**
	 * 保存记录
	 */
	public void saveLog(VoteLogVo voteLogVo) {
        this.voteLogVoMapper.insert(voteLogVo);
	}

	
	/**
	 * 更新四等奖数量，四等奖不影响总奖金池
	 */
	public  void  updateFourthPrice(String  votecode) {
		voteBaseVoMapper.updateFourthPrice(votecode);
	}
	
	/**
	 * 更新五等奖数量，五等奖不影响总奖金池
	 */
	public  void  updateFifthPrice(String  votecode) {
		voteBaseVoMapper.updateFifthPrice(votecode);
	}
	
	/**
	 * 查询奖金池
	 */
	public VoteBaseVo selectVoteBaseVoById(Integer  voteId) {
		return voteBaseVoMapper.selectByPrimaryKey(voteId);
	}
	
	@Override
	public VoteUserVo selectVoteUserVoByUserCode(String usercode) {
		// TODO Auto-generated method stub
		return voteUserVoMapper.selectVoteUserVoByUserCode(usercode);
	}

	@Override
	public VoteUserVo selectVoteUserVoByUserCodeAndVoteCode(String usercode,String voteCode) {
		// TODO Auto-generated method stub
		return voteUserVoMapper.selectVoteUserVoByUserCodeAndVoteCode(usercode ,voteCode);
	}
	
	public void  updateByUserCode(String usercode) {
		 voteUserVoMapper.updateByUserCode(usercode);
	}

	
	
	public void  updateByUserCodeAndVoteCode(String usercode , String voteCode ) {
		 voteUserVoMapper.updateByUserCodeAndVoteCode(usercode ,voteCode);
	}

	
	/**
	 * 获取用户抽奖信息
	 */
	@Override
	public List<VoteInfoDto> selectAllVoteInfo(String voteCode) {

		List<VoteInfoDto> list = new ArrayList<VoteInfoDto>();

		List<Map> mylist = customerInfoVoMapper.selectAllVoteInfo(voteCode);

		for (int i = 0; i < mylist.size(); i++) {

			Map map = mylist.get(i);

			VoteInfoDto voteInfoDto = new VoteInfoDto();
			voteInfoDto.setUsercode(String.valueOf(map.get("usercode")));
			voteInfoDto.setUsername(String.valueOf(map.get("name")));
			voteInfoDto.setUsertel(String.valueOf(map.get("telphone")));
			voteInfoDto.setCreatedate(String.valueOf(map.get("createdate")));
			voteInfoDto.setPrice(String.valueOf(map.get("remark")));

			voteInfoDto.setVotetimes((Integer) map.get("votetimes"));
			voteInfoDto.setUserdeptchannel(String.valueOf(map.get("userdeptchannel")));
			voteInfoDto.setUserdept(String.valueOf(map.get("userdept")));

			list.add(voteInfoDto);
		}

		return list;
	}

	
	
	
	/**
	 * 路桥获取用户的信息
	 * @param usercode
	 * @return
	 */
	@Override
	public VoteUserVo selectVoteUserVoByUserName(String username) {

		VoteUserVo voteUserVo = null;

		List<Map> mylist = voteUserVoMapper.selectVoteUserVoByUserName(username);

		if (mylist.size() > 0) {
			Map map = mylist.get(0);

			voteUserVo = new VoteUserVo();
			
			voteUserVo.setUsercode(String.valueOf(map.get("usercode")));
			voteUserVo.setUsername(String.valueOf(map.get("username")));
			voteUserVo.setUserdept(String.valueOf(map.get("userdept")));
			voteUserVo.setUsertel(String.valueOf(map.get("usertel")));
			voteUserVo.setVotetimes((Integer) map.get("votetimes"));
		}

		return voteUserVo;
	}
	
	
	
	
	/**
	 * 路桥电话号码是否已使用
	 * @param usercode
	 * @return
	 */
	public boolean isHaveVote(String name, String telphone, String voteCode) {

		boolean flag = false;

		List<CustomerInfoVo> infolist = this.customerInfoVoMapper.selectByName(name, telphone, voteCode);

		if (null != infolist && infolist.size() > 0) {
			flag = true; // 已参与
		} else {
			flag = false;
		}

		return flag;
	}
	
	
	
	
	
	/**
	 * 获取路桥用户抽奖信息
	 */
	@Override
	public List<VoteInfoDto> selectLqVoteInfo(String  votecode) {

		List<VoteInfoDto> list = new ArrayList<VoteInfoDto>();

		List<Map> mylist = customerInfoVoMapper.selectLqVoteInfo(votecode);

		for (int i = 0; i < mylist.size(); i++) {

			Map map = mylist.get(i);

			VoteInfoDto voteInfoDto = new VoteInfoDto();
			voteInfoDto.setUsercode(String.valueOf(map.get("usercode")));
			voteInfoDto.setUsername(String.valueOf(map.get("name")));
			voteInfoDto.setUsertel(String.valueOf(map.get("telphone")));
			voteInfoDto.setUserdeptchannel(String.valueOf(map.get("userdeptchannel")));
			voteInfoDto.setPrice(String.valueOf(map.get("remark")));
			voteInfoDto.setCreatedate(String.valueOf(map.get("createdate")));
		  
			voteInfoDto.setAnswer1(String.valueOf(map.get("answer1")));
			voteInfoDto.setAnswer2(String.valueOf(map.get("answer2")));
			voteInfoDto.setAnswer3(String.valueOf(map.get("answer3")));
			voteInfoDto.setAnswer4(String.valueOf(map.get("answer4")));

			list.add(voteInfoDto);
		}

		return list;
	}

	
	
	
	
	

	/**
	 * 广电信息查询
	 */
	@Override
	public List<VoteInfoDto> selectSubInfo(String  votecode) {

		List<VoteInfoDto> list = new ArrayList<VoteInfoDto>();

		List<CustomerInfoVo> mylist = customerInfoVoMapper.selectByVoteCode(votecode);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
				
		for (int i = 0; i < mylist.size(); i++) {

			CustomerInfoVo  customerInfoVo = mylist.get(i);

			VoteInfoDto voteInfoDto = new VoteInfoDto();
			voteInfoDto.setUsername(customerInfoVo.getName());
			voteInfoDto.setUsercode(customerInfoVo.getUsercode());
			voteInfoDto.setUsertel(customerInfoVo.getTelphone());
			voteInfoDto.setUserdept(customerInfoVo.getAdress());;
			voteInfoDto.setUserdeptchannel(customerInfoVo.getRemark());
			voteInfoDto.setCreatedate(dateFormat.format(customerInfoVo.getCreatedate()));
		  
			list.add(voteInfoDto);
		}

		return list;
	}

	@Override
	public List<KmhProductDto> getProductData(String voteCode) {
		List<KmhProductDto> list = voteAnswerVoMapper.getProductData(voteCode);
		/*KmhVo kmhVo = new KmhVo();
		String[] categories = new String[list.size()];
		Integer[] data = new Integer[list.size()];
		for (int i = 0; i <list.size() ; i++) {
			categories[i] = list.get(i).getCategories();
			data[i] = list.get(i).getData();
		}
		kmhVo.setCategories(categories);
		kmhVo.setData(data);*/
		return list;
	}

	@Override
	public List<KmhDeptDto> getDeptData(String voteCode) {
		return voteAnswerVoMapper.getDeptData(voteCode);
	}

	@Override
	public List<KmhEmpDto> getEmpData(String voteCode) {
		return voteAnswerVoMapper.getEmpData(voteCode);
	}

	@Override
	public Integer getTotalNum(String voteCode) {
		return voteAnswerVoMapper.getTotalNum(voteCode);
	}

	@Override
	public Integer isExist(String voteCode, String name, String tel) {
		return voteAnswerVoMapper.isExist(voteCode, name, tel);
	}


}
