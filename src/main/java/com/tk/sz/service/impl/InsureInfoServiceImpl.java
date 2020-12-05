package com.tk.sz.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tk.sz.dao.InsureInfoVoMapper;
import com.tk.sz.model.InsureInfoVo;
import com.tk.sz.service.InsureInfoService;

@Transactional
@Service
public class InsureInfoServiceImpl  implements InsureInfoService{

	
	private static Logger logger = LoggerFactory.getLogger(InsureInfoServiceImpl.class);

	@Autowired
	private InsureInfoVoMapper insureInfoVoMapper ;
	
	@Override
	public boolean saveInsureInfo(InsureInfoVo insureInfoVo) {

		try {
			insureInfoVoMapper.insert(insureInfoVo);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
