package com.tk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tk.sz.model.InsureInfoVo;
import com.tk.sz.service.InsureInfoService;
import com.tk.sz.template.RespTemplate;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class InsureInfoController {
	
	@Autowired
	private  InsureInfoService  insureInfoService ;

	
	private static Logger log = LoggerFactory.getLogger(InsureInfoController.class);

	
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET }, origins = "*")
	@RequestMapping(value = "/sendInfo2SZ", method = { RequestMethod.GET, RequestMethod.POST })
	public String sendInfo2SZ(HttpServletRequest request, @RequestBody String jsno) {

		
		log.info("深圳个账接收请求报文：" + jsno);
	//	JSONObject rspjsonb = new JSONObject();
		RespTemplate  respObj= new RespTemplate();
		
		
		JSONObject reqJsonObj=JSON.parseObject(jsno);
		
		InsureInfoVo  insureInfoVo = reqJsonObj.toJavaObject(InsureInfoVo.class);
		
		try {
			insureInfoService.saveInsureInfo(insureInfoVo);
			respObj.setCode("0");
			respObj.setMessage("发送成功");
		} catch (Exception e) {
			e.printStackTrace();
			respObj.setCode("9");
			respObj.setMessage("发送失败");
			return JSON.toJSONString(respObj) ;
		}
		
		return JSON.toJSONString(respObj) ;
	}
	
}
