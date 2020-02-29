package com.weixin.demo.weixindemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@SpringBootApplication
@RestController
public class WeixinDemoApplication {

	private String token = "aiceflower_token";
	public static void main(String[] args) {
		SpringApplication.run(WeixinDemoApplication.class, args);
	}

	@RequestMapping("/")
	public String signTocken(HttpServletRequest request, HttpServletResponse response){
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");

		System.out.println("------start check-----");

		String sortString = sort(token, timestamp, nonce);
		String myString = sha1(sortString);
		System.out.println("myString:"+myString);
		System.out.println("signature:"+signature);
		if(!isEmpty(myString)&&myString.equals(signature)){
			System.out.println("-----check success!-------");
			return echostr;
		}else {
			System.out.println("-------check error!-------");
			return "-1";
		}

	}

	@RequestMapping("/hello")
	public String hello(){
		return "helloWorld!";
	}

	/**
	 * 排序方法
	 * @param token
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public String sort(String token,String timestamp,String nonce){
		String strArray[] = {token,timestamp,nonce};
		Arrays.sort(strArray);
		StringBuilder sb = new StringBuilder();
		for (String s : strArray) {
			sb.append(s);
		}
		return  sb.toString();
	}

	/**
	 * 安全哈希算法
	 * @param str
	 * @return
	 */
	public static String sha1(String str){
		if(str==null||str.length()==0){
			return null;
		}
		char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
				'a','b','c','d','e','f'};
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes("UTF-8"));

			byte[] md = mdTemp.digest();
			int j = md.length;
			char buf[] = new char[j*2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public boolean isEmpty(String str){
		return str==null||"".equals(str);
	}
}
