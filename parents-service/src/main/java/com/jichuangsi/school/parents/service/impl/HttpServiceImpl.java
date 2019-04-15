package com.jichuangsi.school.parents.service.impl;

import com.jichuangsi.school.parents.commons.ResultCode;
import com.jichuangsi.school.parents.config.HttpHeaderConfig;
import com.jichuangsi.school.parents.exception.ParentHttpException;
import com.jichuangsi.school.parents.service.IHttpService;
import com.jichuangsi.school.parents.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class HttpServiceImpl implements IHttpService {

    @Value("${com.jichuangsi.school.wx.host}")
    private String getTokenHost;
    @Value("${com.jichuangsi.school.wx.getToken.path}")
    private String getTokenPath;
    @Value("${com.jichuangsi.school.wx.getToken.method}")
    private String getTokenMethod;
    @Resource
    private HttpHeaderConfig httpHeaderConfig;

    @Override
    public String findWxTokenModel(String code) throws ParentHttpException {
        Map<String,String> headers = httpHeaderConfig.getheaders();
        Map<String,String> querys = new HashMap<String,String>();
        querys.put("appid","wx6242cfcc7e43e927");
        querys.put("secret","be8cfbba1effe57abb0b08ce6d3e0834");
        querys.put("code",code);
        querys.put("grant_type","authorization_code");
        HttpResponse response = null;
        try {
            response = HttpUtils.doGet(getTokenHost,getTokenPath,getTokenMethod,headers,querys);
            String result = EntityUtils.toString(response.getEntity());
            return result;
        } catch (Exception e) {
            throw new ParentHttpException(ResultCode.HTTP_IO_MSG);
        }
    }

    @Override
    public String findWxUserInfo(String token, String openId,String code) throws ParentHttpException {
        Map<String,String> headers = httpHeaderConfig.getheaders();
        Map<String,String> querys = new HashMap<String,String>();
        querys.put("access_token",token);
        querys.put("appid","wx6242cfcc7e43e927");
        querys.put("secret","be8cfbba1effe57abb0b08ce6d3e0834");
        querys.put("grant_type","authorization_code");
        querys.put("code",code);
        querys.put("openid",openId);
        querys.put("lang","zh_CN");
        HttpResponse response = null;
        try {
            response = HttpUtils.doGet(getTokenHost,getTokenPath,getTokenMethod,headers,querys);
            String result = EntityUtils.toString(response.getEntity());
            return result;
        } catch (Exception e) {
            throw new ParentHttpException(ResultCode.HTTP_IO_MSG);
        }
    }
}
