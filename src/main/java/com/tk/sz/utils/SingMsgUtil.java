package com.tk.sz.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class SingMsgUtil {

	/**
	 * 获取短信验证码
	 * 
	 * @return
	 */
	public static String getSmsCode() {
		String random = new Random().nextInt(1000000) + "";
		if (random.length() != 6) {
			return getSmsCode();
		} else {
			return random;
		}
	}

	/**
	 * 获取时间戳
	 */
	public static String getStamp() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	/**
	 * @Title: getMD5 sig签名
	 */
	public static String getMD5(String sid, String token, String timestamp) {
		StringBuilder sBuilder = new StringBuilder();
		String source = sid + token + timestamp;
		try {
			MessageDigest instance = MessageDigest.getInstance("MD5");
			byte[] digest = instance.digest(source.getBytes());
			for (byte b : digest) {
				String hexString = Integer.toHexString(b & 0xff);
				if (hexString.length() == 1) {
					sBuilder.append("0" + hexString);
				} else {
					sBuilder.append(hexString);
				}
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sBuilder.toString();
	}
	
	/**
	 * 发送验证码
	 * @param telphone
	 * @param smsCode
	 */
	public  static void  sendSmsCode(String telphone , String smsCode ) {
		
		DefaultProfile profile = DefaultProfile.getProfile("default", "",
				"");
		IAcsClient client = new DefaultAcsClient(profile);

		CommonRequest request = new CommonRequest(); 
		//request.setProtocol(ProtocolType.HTTPS);
		request.setMethod(MethodType.POST);
		request.setDomain(""); //dysmsapi.aliyuncs.com 
		request.setVersion("2017-05-25"); 
		request.setAction("SendSms");
		request.putQueryParameter("PhoneNumbers", telphone); //"18565800656",多个手机号用,分开
		request.putQueryParameter("SignName", "xxx公司");
		request.putQueryParameter("", "");
		String  templateParam = "{\"code\":\"" + smsCode + "\"}" ;
		
		request.putQueryParameter("TemplateParam", templateParam ); //"{\"code\":\"888888\"}" 原成功值
		try {
			CommonResponse response = client.getCommonResponse(request);
			System.out.println(response.getData());
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) throws Exception {

		 sendSmsCode("18565800656" ,"686866");
		
		String  templateParam = "{\"code\":\"" + "686866" + "\"}" ;
		
		System.out.println(templateParam);
		

	}

}
