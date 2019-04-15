package com.jichuangsi.school.parents.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class HttpHeaderConfig {

    @Value("${com.jichuangsi.school.wx.source}")
    private  String mySource;
    @Value("${com.jichuangsi.school.wx.formatStr}")
    private  String formatStr;
    @Value("${com.jichuangsi.school.wx.timeZone}")
    private  String timeZone;
    @Value("${com.jichuangsi.school.wx.secretKey}")
    private  String mySecretKey;
    @Value("${com.jichuangsi.school.wx.secretId}")
    private  String mySecretId;

    public  static String sign(String secret, String timeStr)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        //get signStr
        String signStr = "date: " + timeStr + "\n" + "source: " + "source";
        //get sig
        String sig = null;
        Mac mac1 = Mac.getInstance("HmacSHA1");
        byte[] hash;
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), mac1.getAlgorithm());
        mac1.init(secretKey);
        hash = mac1.doFinal(signStr.getBytes("UTF-8"));
        sig = new BASE64Encoder().encode(hash);
        return sig;
    }

    public Map<String,String> getheaders() {
        Map<String, String> headers = new HashMap<String, String>();
        //get current GMT time
        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        String timeStr = sdf.format(cd.getTime());

        String source = mySource;

        //云市场分配的密钥Id
        String secretId = mySecretId;
        //云市场分配的密钥Key
        String secretKey = mySecretKey;

        try {
            // 打开和URL之间的连接
            // 设置通用的请求属性
            headers.put("Accept", "text/html, */*; q=0.01");
            headers.put("Source", source);
            headers.put("Date", timeStr);
            String sig = sign(secretKey, timeStr);
            String authen = "hmac id=\"" + secretId + "\", algorithm=\"hmac-sha1\", headers=\"date source\", signature=\"" + sig + "\"";
            System.out.println("authen --->" + authen);
            headers.put("Authorization", authen);
            headers.put("X-Requested-With", "XMLHttpRequest");
            headers.put("Accept-Encoding", "gzip, deflate, sdch");
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        return headers;
    }
}
