package com.tk;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@ComponentScan({"com.tk.sz"})
@EnableEurekaClient
@EnableFeignClients
@EnableDiscoveryClient
public class MyApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyApplication.class, args);
	}

	
	/**
	 * 对上传文件的配置
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 设置单个附件大小上限值(默认为1M)
		factory.setMaxFileSize("50MB");
		// 设置所有附件的总大小上限值
		factory.setMaxRequestSize("500MB");
		return factory.createMultipartConfig();
	}

}
