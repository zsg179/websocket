package com.tk.sz.service.impl;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tk.controller.UserController;
import com.tk.sz.dao.VoteUserVoMapper;
import com.tk.sz.model.VoteUserVo;
import com.tk.sz.service.CrlandService;
import com.tk.sz.utils.CrlandUtils;

@Transactional
@Service
public class CrlandServiceImpl  implements  CrlandService{

	private static Logger logger = LoggerFactory.getLogger(CrlandServiceImpl.class);

	@Autowired
	private  VoteUserVoMapper voteUserVoMapper;
	
	
	@SuppressWarnings("unused")
	@Override
	public boolean saveData(String filepath) {

		boolean flag = true;

		logger.info("开始读取人员信息文件111111111");
		ArrayList<VoteUserVo> voteUserList = (CrlandUtils.read(filepath));
		logger.info("已读取人员信息：【"  + voteUserList.size()  + "】笔数据");
		try {
			for (VoteUserVo voteUserVo : voteUserList) {

				VoteUserVo myVoteUserVo = null;
				
				
				try {
					myVoteUserVo = voteUserVoMapper.selectVoteUserVoByUserCode(voteUserVo.getUsercode());
				} catch (Exception e) {
					logger.info("查询人员信息失败" + voteUserVo.getUsercode() );
					e.printStackTrace();
					continue;
				}

				if (myVoteUserVo != null) {
					Integer voteTimes = myVoteUserVo.getVotetimes() + voteUserVo.getVotetimes();
					
					myVoteUserVo.setVotetimes(voteTimes);
					myVoteUserVo.setVotecode(myVoteUserVo.getVotecode());

					voteUserVoMapper.updateByPrimaryKeySelective(myVoteUserVo);
					
				} else {
					voteUserVoMapper.insert(voteUserVo);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			return flag;
		}

		// ls.removeFile(file, path+ "data_bak\\" + CommonUtils.getNowData()); //移除文件

		return flag;
	}

	
	
	
	@SuppressWarnings("unused")
	@Override
	public boolean saveData2(String filepath, String  votecode) {

		boolean flag = true;

		logger.info("开始读取人员信息文件22222222222");
		ArrayList<VoteUserVo> voteUserList = (CrlandUtils.read(filepath));
		logger.info("已读取人员信息：【"  + voteUserList.size()  + "】笔数据");
		try {
			for (VoteUserVo voteUserVo : voteUserList) {

				VoteUserVo myVoteUserVo = null;
				
				voteUserVo.setVotecode(votecode);
				
				try {
					//myVoteUserVo = voteUserVoMapper.selectVoteUserVoByUserCode(voteUserVo.getUsercode());
					myVoteUserVo = voteUserVoMapper.selectVoteUserVoByUserCodeAndVoteCode(voteUserVo.getUsercode() ,votecode);
					
				} catch (Exception e) {
					logger.info("查询人员信息失败" + voteUserVo.getUsercode() );
					e.printStackTrace();
					continue;
				}

				if (myVoteUserVo != null) {
					
					Integer voteTimes = myVoteUserVo.getVotetimes() + voteUserVo.getVotetimes();
					
					myVoteUserVo.setVotecode(votecode);
					myVoteUserVo.setVotetimes(voteTimes);

					voteUserVoMapper.updateByPrimaryKeySelective(myVoteUserVo);
					
				} else {
					
					voteUserVo.setVotecode(votecode);
					voteUserVoMapper.insert(voteUserVo);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			return flag;
		}

		// ls.removeFile(file, path+ "data_bak\\" + CommonUtils.getNowData()); //移除文件

		return flag;
	}

	
	
	
	
	
}
