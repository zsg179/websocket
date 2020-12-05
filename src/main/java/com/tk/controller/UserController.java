package com.tk.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tk.sz.model.UserVo;
import com.tk.sz.model.VoteLogVo;
import com.tk.sz.model.VoteUserVo;
import com.tk.sz.service.CrlandService;
import com.tk.sz.service.IRedisService;
import com.tk.sz.service.UserService;
import com.tk.sz.template.RespTemplate;
import com.tk.sz.utils.QRCodeUtil;
import com.tk.sz.utils.SingMsgUtil;
import com.tk.sz.utils.SystemConfig;

@CrossOrigin
@RestController
public class UserController {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private IRedisService redisService;
	
	@Autowired
	private  CrlandService  crlandService;
	
	
	private final String VOTE_CODE_CJ2020 ="CJ20200124"; //春节假日经营
	/**
	 * 保存用户生成链接
	 * d
	 * @param request
	 * @param jsno
	 * @return
	 */

	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET,
			RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH }, origins = "*")
	@RequestMapping(value = "/getuser", method = { RequestMethod.GET, RequestMethod.POST })
	public String getIndex(HttpServletRequest request, @RequestBody String jsno) throws Exception {

		logger.info("后端接收的原始...................json:   " + jsno);
		String revJsno = URLDecoder.decode(jsno, "utf-8");
		logger.info("后端接收的...................json:   " + revJsno);

		JSONObject jsonb = new JSONObject();
		RespTemplate resp = new RespTemplate();

		UserVo userVo = jsonb.parseObject(jsno, UserVo.class);
		/*
		 * String[] myjson = revJsno.split("&"); String name = myjson[0].split("=")[1];
		 * String telnum = myjson[1].split("=")[1];
		 * 
		 * UserVo userVo = new UserVo(); userVo.setUsername(name);
		 * userVo.setTelphone(telnum);
		 */

		if (StringUtils.isEmpty(userVo.getUsername())) {
			resp.setCode("9");
			resp.setMessage("用户名不能为空！");
		} else if (StringUtils.isEmpty(userVo.getTelphone())) {
			resp.setCode("9");
			resp.setMessage("手机号码不能为空！");
		} else {

			List<UserVo> list = userService.selectByUserVo(userVo);
			if (null != list && list.size() > 0) {
				UserVo rstUser = list.get(0);
				// 生成二维码
				// String text = "http://10.26.140.138:8088/#/index1/?userId=" +
				// rstUser.getUsercode();
				// String imagePath = "src/main/resources/image/logo.jpg";
				// String destPath = "E:/workspace2/myapp2/static";// 前端static目录

				// 生产配置
				String text = "http://pensionlife.95522.cn/szyl_vote/#/index1/?userId=" + rstUser.getUsercode();
				String imagePath = "/app/images/logo.jpg";

				String destPath = "/app/apache-tomcat-7.0.40-app/webapps/szyl_vote/static/";
				try {
					QRCodeUtil.encode(text, imagePath, destPath, true, rstUser.getUsercode() + ".jpg");
				} catch (Exception e) {
					logger.error("生成二维码失败， 请求信息：  " + jsno);
					e.printStackTrace();
				}

				VoteLogVo voteLogVo = new VoteLogVo();
				voteLogVo.setVotecode("ZC20190601");
				voteLogVo.setUsercode(rstUser.getUsercode());
				voteLogVo.setOpendate(new Date());
				voteLogVo.setOpenadress(userVo.getUsername());
				userService.saveLog(voteLogVo);// 保存用户注册记录信息

				resp.setCode("0");// 成功
				resp.setMessage("已经成功生成二维码，转至二维码页");
				resp.setData(rstUser.getUsercode());

			} else {
				resp.setCode("9");// 失败
				resp.setMessage("对不起！请核实你的HR信息或与系统管理员联系！");
			}
			logger.info("后端服务返回......json: " + jsonb.toJSONString(resp));
		}
		return jsonb.toJSONString(resp);
	}

	/**
	 * 获取验证码
	 * 
	 * @param request
	 * @param jsno
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET,
			RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH }, origins = "*")
	@RequestMapping(value = "/sendSms", method = { RequestMethod.GET, RequestMethod.POST })
	public String sendSms(HttpServletRequest request, @RequestBody String jsno) throws Exception {

		logger.info("获取验证码接收的...................json:   " + jsno);

		JSONObject myjson = new JSONObject();
		RespTemplate resp = new RespTemplate();
		JSONObject refJsonObj = JSON.parseObject(jsno);

		String verifyCode = redisService.getValue(refJsonObj.getString("telphone")); // 先从缓存中取
		if (StringUtils.isEmpty(verifyCode)) {
			verifyCode = SingMsgUtil.getSmsCode();// 生成验证码
			redisService.setKey(refJsonObj.getString("telphone"), verifyCode);
		}

		SingMsgUtil.sendSmsCode(refJsonObj.getString("telphone"), verifyCode); // 发送验证码短信

		myjson.put("verifyCode", verifyCode);
		myjson.put("createTime", System.currentTimeMillis());

		resp.setCode("0");// 成功
		resp.setMessage("成功!已发送验证码" + verifyCode);

		logger.info("获取验证码返回的...................json:   " + JSON.toJSONString(resp));
		return JSON.toJSONString(resp);
	}

	/**
	 * 假日活动登录验证
	 * 
	 * @param request
	 * @param jsno
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET,
			RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH }, origins = "*")
	@RequestMapping(value = "/checkSmsCode", method = { RequestMethod.GET, RequestMethod.POST })
	public String checkSmsCode(HttpServletRequest request, @RequestBody String jsno) throws Exception {

		logger.info("假日活动登录验证接收的...................json:   " + jsno);
		JSONObject refJsonObj = JSON.parseObject(jsno);
		RespTemplate resp = new RespTemplate();

		String usercode = refJsonObj.getString("usercode");
		String telphone = refJsonObj.getString("telphone");
		String smsCode = refJsonObj.getString("smsCode");
		String voteCode =  refJsonObj.getString("voteCode");
		
		if (StringUtils.isEmpty(telphone) || StringUtils.isEmpty(smsCode) || StringUtils.isEmpty(usercode)) {
			resp.setCode("1");
			resp.setMessage("口令|手机号|验证码不能为空!");
			return JSON.toJSONString(resp);
		}

		String verifyCode = redisService.getValue(telphone);
		
		if (!smsCode.equals(verifyCode)) {
			resp.setCode("1");
			resp.setMessage("验证码不正确!");
			return JSON.toJSONString(resp);
		}

		//校验业务工号的合法性
		VoteUserVo  voteUserVo = userService.selectVoteUserVoByUserCodeAndVoteCode(usercode,voteCode);
		
		if (voteUserVo!=null) {
			resp.setCode("0");
			resp.setData(String.valueOf(voteUserVo.getVotetimes()));
			resp.setMessage("成功!");
		} else {
			resp.setCode("1");
			resp.setMessage("此幸运口令暂无参与活动，请反馈给您的客户经理！");
		}

		logger.info("假日活动登录验证返回...................json:   " + JSON.toJSONString(resp));
		return JSON.toJSONString(resp);
		
	}
	
	
	
	/** 
	 *   假日经营查询可抽奖次数
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET,
			RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH }, origins = "*")
	@RequestMapping(value = "/getVoteTimes", method = { RequestMethod.GET, RequestMethod.POST })
	public String getVoteTimes(HttpServletRequest request, @RequestBody String jsno) throws Exception {

		logger.info("查询业务员可抽奖次数请求报文:   " + jsno);
		JSONObject refJsonObj = JSON.parseObject(jsno);
		RespTemplate resp = new RespTemplate();

		String usercode = refJsonObj.getString("usercode");
		String voteCode = refJsonObj.getString("voteCode");

		if (StringUtils.isEmpty(usercode) ) {
			resp.setCode("1");
			resp.setMessage("业务工号不能为空!");
			return JSON.toJSONString(resp);
		}

		// 查询抽奖次数
		VoteUserVo  voteUserVo = userService.selectVoteUserVoByUserCodeAndVoteCode(usercode,voteCode);

		if (voteUserVo!=null) {
			resp.setCode("0");
			resp.setData(String.valueOf(voteUserVo.getVotetimes()));
			resp.setMessage("成功!");
		} else {
			resp.setCode("1");
			resp.setMessage("无可用抽奖次数"); 
		}

		logger.info("查询业务员可抽奖次数返回...................json:   " + JSON.toJSONString(resp));
		return JSON.toJSONString(resp);
	}
	
	
	
	/**
	 *  假日经营名单导入上传
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET,
			RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH }, origins = "*")
	@RequestMapping(value = "/fileUpload", method = { RequestMethod.GET, RequestMethod.POST })
	public String fileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {

		RespTemplate resp = new RespTemplate();

		// boolean a = driverBenefitService.batchImport(fileName, file, createdBy);//导入
		// 先设定一个放置上传文件的文件夹(该文件夹可以不存在，下面会判断创建)
		String deposeFilesDir = SystemConfig.getProperty("tk.file.path");
		// 判断文件手否有内容
		if (file.isEmpty()) {
			logger.info("该文件无任何内容!!!");
		}
		// 获取附件原名(有的浏览器如ie获取到的是含整个路径的含后缀的文件名，如C:\\Users\\images\\myImage.png)
		String fileName = file.getOriginalFilename();
		// 如果是获取的含有路径的文件名，那么截取掉多余的，只剩下文件名和后缀名
		int index = fileName.lastIndexOf("\\");
		if (index > 0) {
			fileName = fileName.substring(index + 1);
		}
		// 判断单个文件大于1M
		long fileSize = file.getSize();
		if (fileSize > 1024 * 1024) {
			logger.info("文件大小为(单位字节):" + fileSize);
			logger.info("该文件大于1M");
		}
		// 当文件有后缀名时
		if (fileName.indexOf(".") >= 0) {
			// split()中放正则表达式; 转义字符"\\."代表 "."
			String[] fileNameSplitArray = fileName.split("\\.");
			// 加上random戳,防止附件重名覆盖原文件
			fileName = fileNameSplitArray[0] + (int) (Math.random() * 100000) + "." + fileNameSplitArray[1];
		}
		// 当文件无后缀名时(如C盘下的hosts文件就没有后缀名)
		if (fileName.indexOf(".") < 0) {
			// 加上random戳,防止附件重名覆盖原文件
			fileName = fileName + (int) (Math.random() * 100000);
		}
		logger.info("fileName:" + fileName);

		// 根据文件的全路径名字(含路径、后缀),new一个File对象dest
		File dest = new File(deposeFilesDir + fileName);
		// 如果该文件的上级文件夹不存在，则创建该文件的上级文件夹及其祖辈级文件夹;
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		try {
			// 将获取到的附件file,transferTo写入到指定的位置(即:创建dest时，指定的路径)
			file.transferTo(dest);
			
			// 导入到数据库
			crlandService.saveData2(deposeFilesDir + fileName , "JR20200927");

			resp.setCode("0");
			resp.setMessage("文件导入成功！");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.setCode("9");
			resp.setMessage("文件导入失败！");
			return JSON.toJSONString(resp);
		}

		logger.info("导入名单返回报文：" + JSON.toJSONString(resp));
		return JSON.toJSONString(resp);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 路桥集团导入上传
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET,
			RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH }, origins = "*")
	@RequestMapping(value = "/fileUpload2", method = { RequestMethod.GET, RequestMethod.POST })
	public String fileUpload2(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {

		RespTemplate resp = new RespTemplate();

		// boolean a = driverBenefitService.batchImport(fileName, file, createdBy);//导入
		// 先设定一个放置上传文件的文件夹(该文件夹可以不存在，下面会判断创建)
		String deposeFilesDir = SystemConfig.getProperty("tk.file.path");
		// 判断文件手否有内容
		if (file.isEmpty()) {
			logger.info("该文件无任何内容!!!");
		}
		// 获取附件原名(有的浏览器如ie获取到的是含整个路径的含后缀的文件名，如C:\\Users\\images\\myImage.png)
		String fileName = file.getOriginalFilename();
		// 如果是获取的含有路径的文件名，那么截取掉多余的，只剩下文件名和后缀名
		int index = fileName.lastIndexOf("\\");
		if (index > 0) {
			fileName = fileName.substring(index + 1);
		}
		// 判断单个文件大于1M
		long fileSize = file.getSize();
		if (fileSize > 1024 * 1024) {
			logger.info("文件大小为(单位字节):" + fileSize);
			logger.info("该文件大于1M");
		}
		// 当文件有后缀名时
		if (fileName.indexOf(".") >= 0) {
			// split()中放正则表达式; 转义字符"\\."代表 "."
			String[] fileNameSplitArray = fileName.split("\\.");
			// 加上random戳,防止附件重名覆盖原文件
			fileName = fileNameSplitArray[0] + (int) (Math.random() * 100000) + "." + fileNameSplitArray[1];
		}
		// 当文件无后缀名时(如C盘下的hosts文件就没有后缀名)
		if (fileName.indexOf(".") < 0) {
			// 加上random戳,防止附件重名覆盖原文件
			fileName = fileName + (int) (Math.random() * 100000);
		}
		logger.info("fileName:" + fileName);

		// 根据文件的全路径名字(含路径、后缀),new一个File对象dest
		File dest = new File(deposeFilesDir + fileName);
		// 如果该文件的上级文件夹不存在，则创建该文件的上级文件夹及其祖辈级文件夹;
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		try {
			// 将获取到的附件file,transferTo写入到指定的位置(即:创建dest时，指定的路径)
			file.transferTo(dest);
			// 导入到数据库
			crlandService.saveData2(deposeFilesDir + fileName , "LQJT20191010");

			resp.setCode("0");
			resp.setMessage("文件导入成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.setCode("9");
			resp.setMessage("文件导入失败！");
			return JSON.toJSONString(resp);
		}

		logger.info("导入名单返回报文：" + JSON.toJSONString(resp));
		return JSON.toJSONString(resp);
	}
	
}
