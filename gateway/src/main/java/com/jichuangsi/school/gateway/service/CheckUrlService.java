package com.jichuangsi.school.gateway.service;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.gateway.feign.model.UrlMapping;
import com.jichuangsi.school.gateway.feign.model.User;
import com.jichuangsi.school.gateway.feign.service.IUserFeignService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@Service
public class CheckUrlService {
    @Resource
    private IUserFeignService feignService;
    Map<String,List<String>> urlList=null;
    public boolean checkUserRole(UserInfoForToken userInfoForToken,String url){
        ResponseModel<User> user=feignService.findUser(userInfoForToken.getUserId());
        if(urlList==null){
            ResponseModel<List<UrlMapping>> roleList=feignService.getAllRole();
            if(roleList!=null){
                urlList=new WeakHashMap<String, List<String>>();
                for (UrlMapping userRole:roleList.getData()) {
                    urlList.put(userRole.getRoleName(),userRole.getUrlList());

                }
            }
        }
       for (Map.Entry<String,List<String>> entry: urlList.entrySet()) {
            if (entry.getKey().equals(user.getData().getRoles().get(0).getRoleName())){
                for (String roleUrl:entry.getValue()) {
                    if(roleUrl.equals(url) || url.startsWith(roleUrl)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    Map<String,String> freeUrlList=null;
    public boolean checkIsFreeUrl(String url){
        if (freeUrlList==null ){
            freeUrlList=new WeakHashMap<String, String>();
            List<UrlModel> freeUrl=feignService.getAllFreeUrl().getData();
            for (UrlModel free:freeUrl) {
                freeUrlList.put(free.getName(),free.getUrl());
            }
        }
        for (Map.Entry<String,String> entry:freeUrlList.entrySet()) {
            if (entry.getValue().equals(url) || url.startsWith(entry.getValue())){
                return  true;
            }
        }
        return  false;
    }
}
