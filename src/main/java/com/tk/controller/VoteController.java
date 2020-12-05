package com.tk.controller;

import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.tk.sz.template.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tk.sz.model.CustomerInfoVo;
import com.tk.sz.model.CustomerOldVo;
import com.tk.sz.model.VoteAnswerVo;
import com.tk.sz.model.VoteBaseVo;
import com.tk.sz.model.VoteLogVo;
import com.tk.sz.model.VoteUserVo;
import com.tk.sz.service.IRedisService;
import com.tk.sz.service.UserService;

@RestController
@CrossOrigin
@RequestMapping
public class VoteController {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private IRedisService redisService;
	

	private Integer  VOTE_CZXB_ID  = 2; //活动序号
	private final String  VOTE_CODE= "CZXB20190601" ;//项目编码
	private final String  VOTE_CODE_NSYY= "CZXB20190520" ;//项目编码
	private Integer  VOTE_NSYY_ID  = 3; //当前活动序号
	
	private final String VOTE_CODE_XSTD ="XSTD20191001"; //十一假日经营
	private final Integer VOTE_CODE_ID = 4; //十一假日经营活动序号
	
	
	private final String VOTE_CODE_LQJT = "LQJT20191010"; //十一假日经营
	private final Integer VOTE_CODE_LQJT_ID = 5; //十一假日经营活动序号
	
	
	private final String VOTE_CODE_XCY ="XCY20191016"; //新产业
	private final Integer VOTE_CODE_XCY_ID = 6; //新产业活动序号
	
	
	private final String VOTE_CODE_CJ2020 ="CJ20200124"; //春节假日经营
	
	
	private final Integer VOTE_CODE_CJ2020_ID = 8; //假日经营序号
	
	private Integer VOTE_NSKF_ID = 16;//南山开发员工保险福利需求有奖调研 活动序号
	
	/**
	 * 提交
	 * @param request
	 * @param jsno
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET}, origins = "*")
	@RequestMapping("/subvote")
	public String subvote(HttpServletRequest request, @RequestBody String jsno) throws Exception {

		String revJsno = URLDecoder.decode(jsno, "utf-8");
		logger.info("后端接收的...................json:   " + revJsno);

		JSONObject jsonb = new JSONObject();
		RespTemplate resp = new RespTemplate();
		int price = 0;

		VoteAnswerVo answerVO = jsonb.parseObject(jsno, VoteAnswerVo.class);
		answerVO.setCustomername(answerVO.getCustomername().trim()); //去名字的空格

		try {

			if (StringUtils.isEmpty(answerVO.getCustomername())) {
				resp.setCode("9");
				resp.setMessage("亲！名字不能为空哦！");
			} else if (StringUtils.isEmpty(answerVO.getCustomertel())) {
				resp.setCode("9");
				resp.setMessage("亲！手机号码不能为空哦！");
			} else {

				// 1、保存提交内
				answerVO.setVotecode(VOTE_CODE); 

				// 2 是否已参与过
				boolean voteflag = userService.isHaveGetPrice(answerVO.getCustomername(),answerVO.getCustomertel() , VOTE_CODE);
				if (voteflag) {
					resp.setCode("9");
					resp.setMessage("客官，您已参加过活动，不能再次参加了！");
					resp.setData("0");
				} else {
					// 3、单独保存客户信息
					CustomerInfoVo customerInfoVo = new CustomerInfoVo();
					customerInfoVo.setTelphone(answerVO.getCustomertel());
					customerInfoVo.setName(answerVO.getCustomername());
					customerInfoVo.setAdress(StringUtils.isEmpty(answerVO.getCustomercomp()) == true ? "" : answerVO.getCustomercomp());// 机构
					customerInfoVo.setUsercode(answerVO.getUsercode());
					customerInfoVo.setCreatedate(new Date());
					customerInfoVo.setUpdatedate(new Date());
					customerInfoVo.setVotecode(VOTE_CODE);

					// 4、参与抽奖
					boolean oldCustomerFlag = userService.isOldCustomer(answerVO.getCustomername() ,VOTE_CODE);
					logger.info(answerVO.getCustomername() + "  是否为老客户：  " + oldCustomerFlag);

					if (oldCustomerFlag && "愿意".equals(answerVO.getAnswer1())) {
						price = userService.getPrice(VOTE_CODE_CJ2020);

						// 5 更新老客户中奖信息
						CustomerOldVo customerOldVo = new CustomerOldVo();
						customerOldVo.setName(answerVO.getCustomername());
						customerOldVo.setUsercode(answerVO.getUsercode());
						customerOldVo.setVotefalg("1");
						customerOldVo.setUpdatedate(new Date());
						customerOldVo.setVoteresult(String.valueOf(price));

						userService.updateCustomerOldVoByName(customerOldVo);
						
					} else {
						price = 0;
						userService.updateFourthPrice(VOTE_CODE_CJ2020);
					}

					answerVO.setAnswer20(String.valueOf(price));
					customerInfoVo.setRemark(String.valueOf(price));

					userService.saveAnswer(answerVO);
					userService.saveCustomerInfo(customerInfoVo);

					resp.setCode("0");
					resp.setData(String.valueOf(price));
				}
			}
		} catch (Exception e) {
			resp.setCode("9");
			resp.setMessage("问卷提交失败!");
		}
         
		//防止恶意刷奖品     
		VoteBaseVo voteBaseVo = userService.selectVoteBaseVoById(VOTE_CZXB_ID);
		if (voteBaseVo.getTotalprize() < 0 || voteBaseVo.getFourprize() < 0) {
			resp.setCode("0");
			resp.setData("9");
			resp.setMessage("奖金池爆了");
		}
		
		// 更新中奖标志
		logger.info("后端服务返回......json: " + jsonb.toJSONString(resp));

		return jsonb.toJSONString(resp);
	}
	
	
	/**
	 * 用户行为保存
	 * @param request
	 * @param jsno
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET}, origins = "*")
	@RequestMapping("/savelog")
	public String savelog(HttpServletRequest request, @RequestBody String jsno) throws Exception {
		String revJsno = URLDecoder.decode(jsno, "utf-8");
		logger.info("后端接收的...................json:   " + revJsno);

		JSONObject jsonb = new JSONObject();
		RespTemplate resp = new RespTemplate();

		try {
			VoteLogVo voteLogVo = jsonb.parseObject(jsno, VoteLogVo.class);
			voteLogVo.setOpendate(new Date());
			voteLogVo.setVotecode(voteLogVo.getVotecode());
			userService.saveLog(voteLogVo);
			resp.setCode("0");
			resp.setMessage("用户行为保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			resp.setCode("9");
			resp.setMessage("用户行为保存失败");
		}
		
		return jsonb.toJSONString(resp);
	}
	
	
	
	
	/**
	 * 南山医院用户行为保存
	 * @param request
	 * @param jsno
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET }, origins = "*")
	@RequestMapping("/savelog2")
	public String savelog2(HttpServletRequest request, @RequestBody String jsno) throws Exception {
		String revJsno = URLDecoder.decode(jsno, "utf-8");
		logger.info("后端接收的...................json:   " + revJsno);

		JSONObject jsonb = new JSONObject();
		RespTemplate resp = new RespTemplate();

		try {
			VoteLogVo voteLogVo = jsonb.parseObject(jsno, VoteLogVo.class);
			voteLogVo.setOpendate(new Date());
			voteLogVo.setVotecode(VOTE_CODE_NSYY);
			userService.saveLog(voteLogVo);
			resp.setCode("0");
			resp.setMessage("用户行为保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			resp.setCode("9");
			resp.setMessage("用户行为保存失败");
		}
		
		return jsonb.toJSONString(resp);
	}
	
	
	/**
	 * 南山医院提交
	 * @param request
	 * @param jsno
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET}, origins = "*")
	@RequestMapping("/subvote2")
	public String subvote2(HttpServletRequest request, @RequestBody String jsno) throws Exception {

		String revJsno = URLDecoder.decode(jsno, "utf-8");
		logger.info("后端接收的...................json:   " + revJsno);

		JSONObject jsonb = new JSONObject();
		RespTemplate resp = new RespTemplate();
		int price = 0;

		VoteAnswerVo answerVO = jsonb.parseObject(jsno, VoteAnswerVo.class);
		answerVO.setCustomername(answerVO.getCustomername().trim()); //去名字的空格

		try {
			if (StringUtils.isEmpty(answerVO.getCustomername())) {
				resp.setCode("9");
				resp.setMessage("亲！名字不能为空哦！");
			} else if (StringUtils.isEmpty(answerVO.getCustomertel())) {
				resp.setCode("9");
				resp.setMessage("亲！手机号码不能为空哦！");
			} else {

				// 1、保存提交内
				answerVO.setVotecode(VOTE_CODE_NSYY); 

				// 2 是否已参与过
				boolean voteflag = userService.isHaveGetPrice(answerVO.getCustomername(),answerVO.getCustomertel(),VOTE_CODE_NSYY);
				if (voteflag) {
					resp.setCode("9");
					resp.setMessage("客官，您已参加过活动，不能再次参加了！");
					resp.setData("0");
				} else {
					// 3、单独保存客户信息
					CustomerInfoVo customerInfoVo = new CustomerInfoVo();
					customerInfoVo.setTelphone(answerVO.getCustomertel());
					customerInfoVo.setName(answerVO.getCustomername());
					customerInfoVo.setAdress(StringUtils.isEmpty(answerVO.getCustomercomp()) == true ? "" : answerVO.getCustomercomp());// 机构
					customerInfoVo.setUsercode(answerVO.getUsercode());
					customerInfoVo.setCreatedate(new Date());
					customerInfoVo.setUpdatedate(new Date());
					customerInfoVo.setVotecode(VOTE_CODE_NSYY);

					// 4、参与抽奖
					boolean oldCustomerFlag = userService.isOldCustomer(answerVO.getCustomername(), VOTE_CODE_NSYY);
					logger.info(answerVO.getCustomername() + "  是否为老客户：  " + oldCustomerFlag);

					if (oldCustomerFlag) {
						price = userService.getPrice(VOTE_CODE_CJ2020);

						// 5 更新老客户中奖信息
						CustomerOldVo customerOldVo = new CustomerOldVo();
						customerOldVo.setName(answerVO.getCustomername());
						customerOldVo.setUsercode(answerVO.getUsercode());
						customerOldVo.setVotefalg("1");
						customerOldVo.setUpdatedate(new Date());
						if (0 != price) {
							customerOldVo.setVoteresult(String.valueOf(price));
							answerVO.setAnswer20(String.valueOf(price));
							customerInfoVo.setRemark(String.valueOf(price));
						} else {
							customerOldVo.setVoteresult("5"); 
							answerVO.setAnswer20("5");
							customerInfoVo.setRemark("5");
							price = 5 ;
							
							userService.updateFifthPrice(VOTE_CODE_CJ2020);// 更新五等奖金池
						}
						
						userService.updateCustomerOldVoByName(customerOldVo);
						userService.saveAnswer(answerVO);
						userService.saveCustomerInfo(customerInfoVo);

						resp.setCode("0");
						resp.setData(String.valueOf(price));
					} else {
						resp.setCode("204");
						resp.setMessage("亲，请填写正确的信息！如信息无误，请反馈给泰康服务人员！");
					}
				}
			}
		} catch (Exception e) {
			resp.setCode("9");
			resp.setMessage("问卷提交失败!");
		}
         
		//防止恶意刷奖品     
		VoteBaseVo voteBaseVo = userService.selectVoteBaseVoById(VOTE_NSYY_ID);
		if (voteBaseVo.getTotalprize() <= 0 ) { //总奖金消耗完，默认四等奖
			resp.setCode("0");
			resp.setData("4");
			resp.setMessage("奖金池爆了");
		}
		
		// 更新中奖标志
		logger.info("后端服务返回......json: " + jsonb.toJSONString(resp));

		return jsonb.toJSONString(resp);
	}
	/**
	 * 南山医院开发员工提交
	 * @param request
	 * @param jsno
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET}, origins = "*")
	@RequestMapping("/subvote4")
	public String subvote4(HttpServletRequest request, @RequestBody String jsno) throws Exception {
		
		String revJsno = URLDecoder.decode(jsno, "utf-8");
		logger.info("后端接收的...................json:   " + revJsno);
		
		JSONObject jsonb = new JSONObject();
		RespTemplate resp = new RespTemplate();
		int price = 0;
		
		VoteAnswerVo answerVO = jsonb.parseObject(jsno, VoteAnswerVo.class);
		answerVO.setCustomername(answerVO.getCustomername().trim()); //去名字的空格
		String votecode = answerVO.getVotecode();//项目编码，前端传入
		
		try {
			if (StringUtils.isEmpty(answerVO.getCustomername())) {
				resp.setCode("9");
				resp.setMessage("亲！名字不能为空哦！");
			} else if (StringUtils.isEmpty(answerVO.getCustomertel())) {
				resp.setCode("9");
				resp.setMessage("亲！手机号码不能为空哦！");
			} else {
				
				// 1、保存提交内
				//answerVO.setVotecode(VOTE_CODE_NSYY); //由前端传入
				
				// 2 是否已参与过
				boolean voteflag = userService.isHaveGetPrice(answerVO.getCustomername(),answerVO.getCustomertel(),votecode);
				if (voteflag) {
					resp.setCode("9");
					resp.setMessage("客官，您已参加过活动，不能再次参加了！");
					resp.setData("0");
				} else {
					// 3、单独保存客户信息
					CustomerInfoVo customerInfoVo = new CustomerInfoVo();
					customerInfoVo.setTelphone(answerVO.getCustomertel());
					customerInfoVo.setName(answerVO.getCustomername());
					customerInfoVo.setAdress(StringUtils.isEmpty(answerVO.getCustomercomp()) == true ? "" : answerVO.getCustomercomp());// 机构
					//customerInfoVo.setUsercode(answerVO.getUsercode());
					customerInfoVo.setCreatedate(new Date());
					customerInfoVo.setUpdatedate(new Date());
					customerInfoVo.setVotecode(votecode);
					
					// 4、参与抽奖
					//boolean oldCustomerFlag = userService.isOldCustomer(answerVO.getCustomername(), VOTE_CODE_NSYY);
					//logger.info(answerVO.getCustomername() + "  是否为老客户：  " + oldCustomerFlag);
					
					//if (oldCustomerFlag) {
						//抽奖结果
						price = userService.getPrice(votecode);
						
						// 5 更新老客户中奖信息
						/*CustomerOldVo customerOldVo = new CustomerOldVo();
						customerOldVo.setName(answerVO.getCustomername());
						customerOldVo.setUsercode(answerVO.getUsercode());
						customerOldVo.setVotefalg("1");
						customerOldVo.setUpdatedate(new Date());
						customerOldVo.setVotecode(votecode);*/
						if (0 != price) {
							//customerOldVo.setVoteresult(String.valueOf(price));
							answerVO.setAnswer20(String.valueOf(price));
							customerInfoVo.setRemark(String.valueOf(price));
						} else {
							//customerOldVo.setVoteresult("5"); 
							answerVO.setAnswer20("5");
							customerInfoVo.setRemark("5");
							price = 5 ;
							
							userService.updateFifthPrice(votecode);// 更新五等奖金池
						}
						
						//userService.updateCustomerOldVoByName(customerOldVo);
						userService.saveAnswer(answerVO);
						userService.saveCustomerInfo(customerInfoVo);
						
						resp.setCode("0");
						resp.setData(String.valueOf(price));
					}/* else {
						resp.setCode("204");
						resp.setMessage("亲，请填写正确的信息！如信息无误，请反馈给泰康服务人员！");
					}*/
				}
			
		} catch (Exception e) {
			resp.setCode("9");
			resp.setMessage("问卷提交失败!");
		}
		
		//防止恶意刷奖品     
		VoteBaseVo voteBaseVo = userService.selectVoteBaseVoById(VOTE_NSKF_ID);
		if (voteBaseVo.getTotalprize() < 0 ) { //总奖金消耗完，默认四等奖
			resp.setCode("0");
			resp.setData("4");
			resp.setMessage("奖金池爆了");
		}
		
		// 更新中奖标志
		logger.info("后端服务返回......json: " + jsonb.toJSONString(resp));
		
		return jsonb.toJSONString(resp);
	}
	/**
	 * 人社局保险登记提交
	 * @param request
	 * @param jsno
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET}, origins = "*")
	@RequestMapping("/subvote5")
	public String subvote5(HttpServletRequest request, @RequestBody String jsno) throws Exception {
		
		String revJsno = URLDecoder.decode(jsno, "utf-8");
		logger.info("后端接收的...................json:   " + revJsno);
		
		JSONObject jsonb = new JSONObject();
		RespTemplate resp = new RespTemplate();
		int price = 0;
		
		VoteAnswerVo answerVO = jsonb.parseObject(jsno, VoteAnswerVo.class);
		answerVO.setCustomername(answerVO.getCustomername().trim()); //去名字的空格
		String votecode = answerVO.getVotecode();//项目编码，前端传入
		
		try {
			if (StringUtils.isEmpty(answerVO.getCustomername())) {
				resp.setCode("9");
				resp.setMessage("亲！名字不能为空哦！");
			} else if (StringUtils.isEmpty(answerVO.getCustomertel())) {
				resp.setCode("9");
				resp.setMessage("亲！手机号码不能为空哦！");
			} else {
				
				// 1、保存提交内
				//answerVO.setVotecode(VOTE_CODE_NSYY); //由前端传入
				
				// 2 是否已参与过
				//boolean voteflag = userService.isHaveGetPrice(answerVO.getCustomername(),answerVO.getCustomertel(),votecode);
//				if (voteflag) {
//					resp.setCode("9");
//					resp.setMessage("客官，您已登记过了，不能再次登记了！");
//					resp.setData("0");
//				} else {
					// 3、单独保存客户信息
					CustomerInfoVo customerInfoVo = new CustomerInfoVo();
					customerInfoVo.setTelphone(answerVO.getCustomertel());
					customerInfoVo.setName(answerVO.getCustomername());
					customerInfoVo.setAdress(StringUtils.isEmpty(answerVO.getCustomercomp()) == true ? "" : answerVO.getCustomercomp());// 机构
					//customerInfoVo.setUsercode(answerVO.getUsercode());
					customerInfoVo.setCreatedate(new Date());
					customerInfoVo.setUpdatedate(new Date());
					customerInfoVo.setVotecode(votecode);
					
					// 4、参与抽奖
					//boolean oldCustomerFlag = userService.isOldCustomer(answerVO.getCustomername(), VOTE_CODE_NSYY);
					//logger.info(answerVO.getCustomername() + "  是否为老客户：  " + oldCustomerFlag);
					
					//if (oldCustomerFlag) {
					//抽奖结果
					//price = userService.getPrice(votecode);
					
					// 5 更新老客户中奖信息
					/*CustomerOldVo customerOldVo = new CustomerOldVo();
						customerOldVo.setName(answerVO.getCustomername());
						customerOldVo.setUsercode(answerVO.getUsercode());
						customerOldVo.setVotefalg("1");
						customerOldVo.setUpdatedate(new Date());
						customerOldVo.setVotecode(votecode);*/
					/*if (0 != price) {
						//customerOldVo.setVoteresult(String.valueOf(price));
						answerVO.setAnswer20(String.valueOf(price));
						customerInfoVo.setRemark(String.valueOf(price));
					} else {
						//customerOldVo.setVoteresult("5"); 
						answerVO.setAnswer20("5");
						customerInfoVo.setRemark("5");
						price = 5 ;
						
						userService.updateFifthPrice(votecode);// 更新五等奖金池
					}*/
					
					//userService.updateCustomerOldVoByName(customerOldVo);
					
					userService.saveAnswer(answerVO);
					userService.saveCustomerInfo(customerInfoVo);
					
					resp.setCode("0");
					resp.setData("0");
					/*} else {
						resp.setCode("204");
						resp.setMessage("亲，请填写正确的信息！如信息无误，请反馈给泰康服务人员！");
					}*/
			}
			
		} catch (Exception e) {
			resp.setCode("9");
			resp.setMessage("登记失败!");
		}
		
		//防止恶意刷奖品     
		/*VoteBaseVo voteBaseVo = userService.selectVoteBaseVoById(VOTE_NSKF_ID);
		if (voteBaseVo.getTotalprize() < 0 ) { //总奖金消耗完，默认四等奖
			resp.setCode("0");
			resp.setData("4");
			resp.setMessage("奖金池爆了");
		}*/
		
		// 更新中奖标志
		logger.info("后端服务返回......json: " + jsonb.toJSONString(resp));
		
		return jsonb.toJSONString(resp);
	}

	/**
	 * 坪山区保险登记提交
	 * @param request
	 * @param jsno
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET}, origins = "*")
	@RequestMapping("/pingshan")
	public String subvote6(HttpServletRequest request, @RequestBody String jsno) throws Exception {

		String revJsno = URLDecoder.decode(jsno, "utf-8");
		logger.info("后端接收的...................json:   " + revJsno);

		JSONObject jsonb = new JSONObject();
		RespTemplate resp = new RespTemplate();

		VoteAnswerVo answerVO = jsonb.parseObject(jsno, VoteAnswerVo.class);
		answerVO.setCustomername(answerVO.getCustomername().trim()); //去名字的空格
		String votecode = answerVO.getVotecode();//项目编码，前端传入

		try {
				// 单独保存客户信息
				CustomerInfoVo customerInfoVo = new CustomerInfoVo();
				customerInfoVo.setTelphone(answerVO.getCustomertel());
				customerInfoVo.setName(answerVO.getCustomername());
				customerInfoVo.setAdress(StringUtils.isEmpty(answerVO.getCustomercomp()) == true ? "" : answerVO.getCustomercomp());// 单位
				customerInfoVo.setCreatedate(new Date());
				customerInfoVo.setUpdatedate(new Date());
				customerInfoVo.setVotecode(votecode);

				userService.saveAnswer(answerVO);
				userService.saveCustomerInfo(customerInfoVo);

				resp.setCode("200");
				resp.setData("0");
		} catch (Exception e) {
			resp.setCode("9");
			resp.setMessage("登记失败!");
		}

		logger.info("后端服务返回......json: " + jsonb.toJSONString(resp));

		return jsonb.toJSONString(resp);
	}


	/**
	 * 通用信息采集提交
	 * @param request
	 * @param jsno
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET}, origins = "*")
	@RequestMapping("/commonCol")
	public String subvote7(HttpServletRequest request, @RequestBody String jsno) throws Exception {

		String revJsno = URLDecoder.decode(jsno, "utf-8");
		logger.info("后端接收的...................json:   " + revJsno);

		JSONObject jsonb = new JSONObject();
		RespTemplate resp = new RespTemplate();

		VoteAnswerVo answerVO = jsonb.parseObject(jsno, VoteAnswerVo.class);
		answerVO.setCustomername(answerVO.getCustomername().trim()); //去名字的空格
		String votecode = answerVO.getVotecode();//项目编码，前端传入

		try {
				// 单独保存客户信息
				CustomerInfoVo customerInfoVo = new CustomerInfoVo();
				customerInfoVo.setTelphone(answerVO.getCustomertel());
				customerInfoVo.setName(answerVO.getCustomername());
				customerInfoVo.setAdress(StringUtils.isEmpty(answerVO.getCustomercomp()) == true ? "" : answerVO.getCustomercomp());// 单位
				customerInfoVo.setCreatedate(new Date());
				customerInfoVo.setUpdatedate(new Date());
				customerInfoVo.setVotecode(votecode);

				userService.saveAnswer(answerVO);
				userService.saveCustomerInfo(customerInfoVo);

				resp.setCode("200");
				resp.setData("0");
		} catch (Exception e) {
			resp.setCode("9");
			resp.setMessage("登记失败!");
		}

		logger.info("后端服务返回......json: " + jsonb.toJSONString(resp));

		return jsonb.toJSONString(resp);
	}


	/**
	 * 开门红提交
	 * @param request
	 * @param jsno
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET}, origins = "*")
	@RequestMapping("/kmh")
	public String kaiMenHong(HttpServletRequest request, @RequestBody String jsno) throws Exception {

		String revJsno = URLDecoder.decode(jsno, "utf-8");
		logger.info("开门红押注后端接收的...................json:   " + revJsno);

		JSONObject jsonb = new JSONObject();
		RespTemplate resp = new RespTemplate();

		VoteAnswerVo answerVO = jsonb.parseObject(jsno, VoteAnswerVo.class);
		answerVO.setCustomername(answerVO.getCustomername().trim()); //去名字的空格
		String votecode = answerVO.getVotecode();//项目编码，前端传入


		//检查用户是否二次押注
		Integer exist = userService.isExist(votecode, answerVO.getCustomername(), answerVO.getCustomertel());
		if (exist != null) {
			//已押注过
			resp.setCode("9");
			resp.setMessage("您已经押注过了哦！");
			logger.info("开门红押注后端服务返回......json: " + jsonb.toJSONString(resp));
			return jsonb.toJSONString(resp);
		}
		try {
			// 单独保存客户信息
			CustomerInfoVo customerInfoVo = new CustomerInfoVo();
			customerInfoVo.setTelphone(answerVO.getCustomertel());
			customerInfoVo.setName(answerVO.getCustomername());
			//customerInfoVo.setAdress(StringUtils.isEmpty(answerVO.getCustomercomp()) == true ? "" : answerVO.getCustomercomp());// 单位
			customerInfoVo.setCreatedate(new Date());
			customerInfoVo.setUpdatedate(new Date());
			customerInfoVo.setVotecode(votecode);

			userService.saveAnswer(answerVO);
			userService.saveCustomerInfo(customerInfoVo);

			resp.setCode("200");
			resp.setData("0");
		} catch (Exception e) {
			resp.setCode("9");
			resp.setMessage("下注失败!");
		}

		logger.info("开门红押注后端服务返回......json: " + jsonb.toJSONString(resp));

		return jsonb.toJSONString(resp);
	}

	/**
	 * 获取开门红数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	/*@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET}, origins = "*")
	@RequestMapping("/kmh/getData")
	public RespTemplate kmdData(HttpServletRequest request, @RequestBody String json) throws Exception {

		JSONObject jsonb = new JSONObject();
		RespTemplate resp = new RespTemplate();

		String revJsno = URLDecoder.decode(json, "utf-8");
		logger.info("开门红押注后端接收的...................json:   " + revJsno);
		KmhVo res = new KmhVo();
		List<KmhProductDto> productList = userService.getProductData(jsonb.parseObject(json).getString("voteCode"));
		*//*List<KmhDeptDto> deptList = userService.getDeptData(jsonb.parseObject(json).getString("voteCode"));
		List<KmhEmpDto> empList = userService.getEmpData(jsonb.parseObject(json).getString("voteCode"));*//*
		Integer totalNum = userService.getTotalNum(jsonb.parseObject(json).getString("voteCode"));
		res.setProductList(productList);
		*//*res.setDeptList(deptList);
		res.setEmpList(empList);*//*
		res.setTotalNum(totalNum);
		if (res != null) {
			resp.setCode("0");
			resp.setData(JSON.toJSONString(res));
			resp.setMessage("成功");
		}else{
			resp.setCode("9");
			resp.setMessage("查询失败");
		}
		logger.info("后端服务返回......json: " + jsonb.toJSONString(resp));

		return resp;
	}*/
	
	
	
	/**
	 * 广电集团提交
	 * @param request
	 * @param jsno
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET }, origins = "*")
	@RequestMapping("/subvote3")
	public String subvote3(HttpServletRequest request, @RequestBody String jsno) throws Exception {

		String revJsno = URLDecoder.decode(jsno, "utf-8");
		logger.info("广电集团提交后端接收的...................json:   " + revJsno);

		JSONObject jsonb = new JSONObject();
		RespTemplate resp = new RespTemplate();
		int price = 0;

		VoteAnswerVo answerVO = jsonb.parseObject(jsno, VoteAnswerVo.class);
		answerVO.setCustomername(answerVO.getCustomername().trim()); //去名字的空格

		try {
			if (StringUtils.isEmpty(answerVO.getCustomername())) {
				resp.setCode("9");
				resp.setMessage("亲！名字不能为空哦！");
			} else if (StringUtils.isEmpty(answerVO.getCustomertel())) {
				resp.setCode("9");
				resp.setMessage("亲！手机号码不能为空哦！");
			} else {

				// 1、保存提交内
				answerVO.setVotecode(answerVO.getVotecode()); 
				answerVO.setAnswer2(answerVO.getCustomerCardId());

				// 2 是否已参与过
				boolean voteflag = userService.isHaveSub(answerVO.getCustomerCardId(),answerVO.getCustomertel(),answerVO.getVotecode());
				if (voteflag) {
					resp.setCode("9");
					resp.setMessage("亲，您已参与过不能再次参加了！");
					resp.setData("0");
				} else {
					// 3、单独保存客户信息
					CustomerInfoVo customerInfoVo = new CustomerInfoVo();
					customerInfoVo.setRemark(null != answerVO.getAnswer1() ? answerVO.getAnswer1() : "");// 是否员工本人
					customerInfoVo.setIdentity(answerVO.getCustomerCardId());
					customerInfoVo.setTelphone(answerVO.getCustomertel());
					customerInfoVo.setName(answerVO.getCustomername());
					customerInfoVo.setAdress(StringUtils.isEmpty(answerVO.getCustomercomp()) == true ? "" : answerVO.getCustomercomp());// 机构
					customerInfoVo.setUsercode(answerVO.getUsercode());
					customerInfoVo.setCreatedate(new Date());
					customerInfoVo.setUpdatedate(new Date());
					customerInfoVo.setVotecode(answerVO.getVotecode());

					// 4、参与抽奖
				//	boolean oldCustomerFlag = userService.isOldCustomer(answerVO.getCustomername(), answerVO.getVotecode());
				//	logger.info(answerVO.getCustomername() + "  是否为老客户：  " + oldCustomerFlag);

		
				     userService.saveAnswer(answerVO);
				     userService.saveCustomerInfo(customerInfoVo);

				  resp.setCode("0");
				  resp.setData("0");
					
				}
			}
		} catch (Exception e) {
			resp.setCode("9");
			resp.setMessage("问卷提交失败!");
		}
         
		//防止恶意刷奖品     
	/*	VoteBaseVo voteBaseVo = userService.selectVoteBaseVoById(VOTE_NSYY_ID);
		if (voteBaseVo.getTotalprize() <= 0 ) { //总奖金消耗完，默认四等奖
			resp.setCode("0");
			resp.setData("4");
			resp.setMessage("奖金池爆了");
		}*/
		
		// 更新中奖标志
		logger.info("后端服务返回......json: " + jsonb.toJSONString(resp));

		return jsonb.toJSONString(resp);
	}
	
	
	
	/**
	 * 广电活动结果查询   
	 * @param request
	 * @param jsno
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET}, origins = "*")
	@RequestMapping("/selectSubInfo")
	public String selectSubInfo(HttpServletRequest request, @RequestBody String jsno) throws Exception {
		String revJsno = URLDecoder.decode(jsno, "utf-8");
		logger.info("广电活动查询...................json:   " + revJsno);

		JSONObject refJsonObj = JSON.parseObject(jsno);
		JSONObject jsonb = new JSONObject();

		String votecode = refJsonObj.getString("votecode");
		
		
		RespTemplate resp = new RespTemplate();
		
		List<VoteInfoDto> list;

		try {

			list = userService.selectSubInfo(votecode);

			resp.setCode("0");
			resp.setData(jsonb.toJSONString(list));
			resp.setMessage("查询成功");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.setCode("9");
			resp.setMessage("查询失败");
			logger.info("假日经营抽奖活动结果查询返回：" + jsonb.toJSONString(resp));
			return jsonb.toJSONString(resp);
		}

		logger.info("假日经营抽奖活动结果查询返回：" + jsonb.toJSONString(resp));
		
		return jsonb.toJSONString(resp);
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 假日经营抽奖活动提交   
	 * @param request
	 * @param jsno
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET}, origins = "*")
	@RequestMapping("/getHolidayVote")
	public String getHolidayVote(HttpServletRequest request, @RequestBody String jsno) throws Exception {

		String revJsno = URLDecoder.decode(jsno, "utf-8");
		logger.info("假日经营抽奖活动提交请求...................json:   " + revJsno);

		JSONObject jsonb = new JSONObject();
		RespTemplate resp = new RespTemplate();
		int price = 0;

		CustomerInfoVo customerInfoVo = jsonb.parseObject(jsno, CustomerInfoVo.class);
		String voteCode = customerInfoVo.getVotecode();
		
		try {
			if (StringUtils.isEmpty(customerInfoVo.getUsercode())) {
				resp.setCode("9");
				resp.setMessage("业务员工号不能为空！");
			} else {

				VoteUserVo  voteUserVo = userService.selectVoteUserVoByUserCodeAndVoteCode(customerInfoVo.getUsercode(),customerInfoVo.getVotecode());
			
				if (voteUserVo.getVotetimes()<1) {
					resp.setCode("9");
					resp.setMessage("客官，您已无抽奖机会了！");
					resp.setData("0");
				} else {
					
					customerInfoVo.setCreatedate(new Date());
					customerInfoVo.setUpdatedate(new Date());
					customerInfoVo.setName(voteUserVo.getUsername());
					customerInfoVo.setVotecode(voteCode);//活动编码

					// 1、参与抽奖

					price = userService.getPrice(customerInfoVo.getVotecode());//活动序号

					customerInfoVo.setRemark(String.valueOf(price));
					
					//2、保存用户信息
					userService.saveCustomerInfo(customerInfoVo);
					
					//3、更新可抽奖信息
					//userService.updateByUserCode(customerInfoVo.getUsercode());
					userService.updateByUserCodeAndVoteCode(customerInfoVo.getUsercode(),voteCode);

					resp.setCode("0");
					resp.setData(String.valueOf(price));
					resp.setMessage("抽奖成功");
				}
			}
		} catch (Exception e) {
			resp.setCode("9");
			resp.setMessage("亲，请确认信息无误反馈给分公司");
		}

		// 防止恶意刷奖品
//		VoteBaseVo voteBaseVo = userService.selectVoteBaseVoById(voteCode);
//		if (voteBaseVo.getTotalprize() <= 0) { // 总奖金消耗完，默认四等奖
//			resp.setCode("0");
//			resp.setData("5");
//			resp.setMessage("奖金池爆了");
//		}

		// 更新中奖标志
		logger.info("假日经营抽奖活动提交......json: " + jsonb.toJSONString(resp));

		return jsonb.toJSONString(resp);
	}
	
	
	/**
	 * 假日经营抽奖活动结果查询   
	 * @param request
	 * @param jsno
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET }, origins = "*")
	@RequestMapping("/selectVoteInfo")
	public String selectVoteInfo(HttpServletRequest request, @RequestBody String jsno) throws Exception {
		String revJsno = URLDecoder.decode(jsno, "utf-8");
		logger.info("后端接收的...................json:   " + revJsno);

		
		logger.info("假日经营抽奖活动结果查询请求：" + jsno);
		JSONObject refJsonObj = JSON.parseObject(jsno);
		
		JSONObject jsonb = new JSONObject();
		RespTemplate resp = new RespTemplate();

		
		String voteCode = refJsonObj.getString("voteCode");
		
		List<VoteInfoDto> list;

		try {

			//list = userService.selectLqVoteInfo(voteCode);
			list = userService.selectAllVoteInfo(voteCode);

			resp.setCode("0");
			resp.setData(jsonb.toJSONString(list));
			resp.setMessage("查询成功");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.setCode("9");
			resp.setMessage("查询失败");
			logger.info("假日经营抽奖活动结果查询返回：" + jsonb.toJSONString(resp));
			return jsonb.toJSONString(resp);
		}

		logger.info("假日经营抽奖活动结果查询返回：" + jsonb.toJSONString(resp));
		
		return jsonb.toJSONString(resp);
	}
	
	

	
	
	
	
	/**
	 * 路桥活动登录验证
	 * 
	 * @param request
	 * @param jsno
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET}, origins = "*")
	@RequestMapping(value = "/checkSmsLq", method = { RequestMethod.GET, RequestMethod.POST })
	public String checkSmsLq(HttpServletRequest request, @RequestBody String jsno) throws Exception {

		logger.info(" 路桥活动登录验证接收的...................json:   " + jsno);
		JSONObject refJsonObj = JSON.parseObject(jsno);
		RespTemplate resp = new RespTemplate();

		String username = refJsonObj.getString("username").trim();
		String telphone = refJsonObj.getString("telphone").trim();
		String smsCode = refJsonObj.getString("smsCode").trim();

		if (StringUtils.isEmpty(telphone) || StringUtils.isEmpty(smsCode) || StringUtils.isEmpty(username)) {
			resp.setCode("1");
			resp.setMessage("姓名|手机号|验证码不能为空!");
			return JSON.toJSONString(resp);
		}

		String verifyCode = redisService.getValue(telphone);

		if (!smsCode.equals(verifyCode)) {
			resp.setCode("1");
			resp.setMessage("请填写正确的验证码!");
			logger.info("路桥活动登录验证返回...................json:   " + JSON.toJSONString(resp));
			return JSON.toJSONString(resp);
		}

		// 校验业务姓名的合法性
		VoteUserVo voteUserVo = userService.selectVoteUserVoByUserName(username);

		if (voteUserVo != null) {
			
			// 2 手机号是否已参与过
			boolean voteflag = userService.isHaveVote(username, telphone, VOTE_CODE_LQJT);
			if (voteflag) {
				resp.setCode("1");
				resp.setMessage("亲，您已参与过活动，不能再次参与了！");
				return JSON.toJSONString(resp);
			}
			
			if (voteUserVo.getVotetimes() > 0) {
				resp.setCode("0");
				resp.setData(String.valueOf(voteUserVo.getVotetimes()));
				resp.setMessage("成功!");
			} else {
				resp.setCode("1");
				resp.setMessage("您暂无法参与本次活动，请反馈给您的客户经理！");
			}
		} else {
			resp.setCode("1");
			resp.setMessage("亲，您暂无法参与本次活动，请反馈给您的客户经理！");
		}

		logger.info("路桥活动登录验证返回...................json:   " + JSON.toJSONString(resp));
		return JSON.toJSONString(resp);
	}
	
	
	
	/**
	 * 路桥抽奖活动提交
	 * @param request
	 * @param jsno
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET}, origins = "*")
	@RequestMapping("/subLqVote")
	public String subLqVote(HttpServletRequest request, @RequestBody String jsno) throws Exception {

		String revJsno = URLDecoder.decode(jsno, "utf-8");
		logger.info("路桥抽奖抽奖活动提交请求...................json:   " + revJsno);

		JSONObject jsonb = new JSONObject();
		RespTemplate resp = new RespTemplate();
		int price = 0;

		CustomerInfoVo customerInfoVo = jsonb.parseObject(jsno, CustomerInfoVo.class);

		try {
			if (StringUtils.isEmpty(customerInfoVo.getTelphone()) || StringUtils.isEmpty(customerInfoVo.getName())) {
				resp.setCode("9");
				resp.setMessage("姓名|电话不能为空！");
			} else {
				VoteUserVo voteUserVo = userService.selectVoteUserVoByUserName(customerInfoVo.getName());

				if (voteUserVo.getVotetimes() < 1) {
					resp.setCode("9");
					resp.setMessage("客官，您已无抽奖机会了！");
					resp.setData("0");
				} else {

					VoteAnswerVo voteAnswerVo = new VoteAnswerVo();
					
					voteAnswerVo.setAnswer1(customerInfoVo.getAnswer1());
					voteAnswerVo.setAnswer2(customerInfoVo.getAnswer2());
					voteAnswerVo.setAnswer3(customerInfoVo.getAnswer3());
					voteAnswerVo.setAnswer4(customerInfoVo.getAnswer4());
					voteAnswerVo.setCustomername(customerInfoVo.getName());
					voteAnswerVo.setCustomertel(customerInfoVo.getTelphone());
					voteAnswerVo.setVotecode(this.VOTE_CODE_LQJT);
					voteAnswerVo.setUsercode(voteUserVo.getUsercode());
					

					customerInfoVo.setCreatedate(new Date());
					customerInfoVo.setUpdatedate(new Date());
					customerInfoVo.setName(voteUserVo.getUsername());
					customerInfoVo.setVotecode(this.VOTE_CODE_LQJT);// 活动编码
					customerInfoVo.setUsercode(voteUserVo.getUsercode());

					// 1、参与抽奖

					price = userService.getPrice(this.VOTE_CODE_CJ2020);// 活动序号

					customerInfoVo.setRemark(String.valueOf(price));
					voteAnswerVo.setAnswer20(String.valueOf(price));

					// 2、保存用户信息
					userService.saveCustomerInfo(customerInfoVo);
					userService.saveAnswer(voteAnswerVo);

					// 3、更新可抽奖信息
					userService.updateByUserCode(voteUserVo.getUsercode());

					resp.setCode("0");
					resp.setData(String.valueOf(price));
					resp.setMessage("抽奖成功");
				}
			}
		} catch (Exception e) {
			resp.setCode("9");
			resp.setMessage("亲，请确认您个人信息无误后反馈给您的服务经理");
		}

		// 更新中奖标志
		logger.info("路桥抽奖抽奖活动提交......json: " + jsonb.toJSONString(resp));

		return jsonb.toJSONString(resp);
	}
	
	
	

	/**
	 * 路桥抽奖活动结果查询
	 * @param request
	 * @param jsno
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET}, origins = "*")
	@RequestMapping("/selectLqVoteInfo")
	public String selectLqVoteInfo(HttpServletRequest request, @RequestBody String jsno) throws Exception {
		String revJsno = URLDecoder.decode(jsno, "utf-8");
		logger.info("后端接收的...................json:   " + revJsno);

		JSONObject refJsonObj = JSON.parseObject(jsno);
		RespTemplate resp = new RespTemplate();
		
		JSONObject jsonb = new JSONObject();

		String votecode = refJsonObj.getString("votecode");
		
		logger.info(votecode + "活动结果查询请求：" + jsno);
		
		List<VoteInfoDto> list;

		try {
			list = userService.selectLqVoteInfo(votecode);

			resp.setCode("0");
			resp.setData(jsonb.toJSONString(list));
			resp.setMessage("查询成功");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.setCode("9");
			resp.setMessage("查询失败");
			logger.info("假日经营抽奖活动结果查询返回：" + jsonb.toJSONString(resp));
			return jsonb.toJSONString(resp);
		}

		logger.info("假日经营抽奖活动结果查询返回：" + jsonb.toJSONString(resp));
		
		return jsonb.toJSONString(resp);
	}
	
	
	
	
	
	
	
	
}
