package com.weixin.demo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weixin.demo.constant.WXConstant;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * java发送http请求，需要引入commons-httpclient依赖
 */
public class HttpUtil {

    /**
     * get请求
     * @return
     */
    public static String doGet(String url) throws IOException {
        HttpClient client = new HttpClient();
        // 设置代理服务器地址和端口

        //client.getHostConfiguration().setProxy("proxy_host_addr",proxy_port);
        // 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
        HttpMethod method=new GetMethod(url);
        //使用POST方法
        //HttpMethod method = new PostMethod("http://java.sun.com");
        client.getParams().setContentCharset("utf-8");
        client.executeMethod(method);

        //打印服务器返回的状态
        System.out.println("get response state"+method.getStatusLine());
        //打印返回的信息
        String result = method.getResponseBodyAsString();
        //System.out.println(result);
        //释放连接
        method.releaseConnection();
        return result;
    }


    /**
     * post请求
     * @return
     */
    public static String doPost(String url,String param) throws IOException {
        HttpClient client = new HttpClient();
        //使用POST方法
        PostMethod method = new PostMethod( url );
        method.setRequestEntity(new StringRequestEntity(param,null,"utf-8"));
        client.getParams().setContentCharset("utf-8");
        client.executeMethod(method);

        //打印服务器返回的状态
        System.out.println("post response state:"+method.getStatusLine());
        //打印返回的信息
        //System.out.println(method.getResponseBodyAsString());

        String result  = method.getResponseBodyAsString();
        //释放连接
        method.releaseConnection();

        return result;
    }


}
