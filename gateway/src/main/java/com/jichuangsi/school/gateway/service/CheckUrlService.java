package com.jichuangsi.school.gateway.service;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.gateway.feign.model.BackUserModel;
import com.jichuangsi.school.gateway.feign.model.UrlMapping;
import com.jichuangsi.school.gateway.feign.model.User;
import com.jichuangsi.school.gateway.feign.service.IUserFeignService;
import com.jichuangsi.school.gateway.feign.model.UrlModel;
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
    Map<String,String> banUrlList=null;
    public boolean checkUserRole(UserInfoForToken userInfoForToken,String url){
        ResponseModel<User> user=feignService.findUser(userInfoForToken.getUserId());
        ResponseModel<BackUserModel> backUser=null;
        if(user.getData()==null){
            backUser=feignService.getBackUserById(userInfoForToken.getUserId());
            if(backUser.getData().getRoleName().equals("M")){//超级管理员
                if(banUrlList==null || banUrlList.size()==0){
                    List<UrlModel> banUrl=feignService.getBanUrlBySuperAdmin().getData();
                    if(banUrl!=null){
                        banUrlList=new WeakHashMap<String, String>();
                        for (UrlModel item:banUrl) {
                            banUrlList.put(item.getName(),item.getUrl());
                        }
                    }
                }
                for (Map.Entry<String,String> entry: banUrlList.entrySet()) {
                    if (entry.getValue().equals(url) || entry.getValue().startsWith(url)){
                        return false;
                    }
                }
                return true;
            }
        }

        if(urlList==null || urlList.size()==0){
            ResponseModel<List<UrlMapping>> roleList=feignService.getAllRole();
            if(roleList!=null){
                urlList=new WeakHashMap<String, List<String>>();
                for (UrlMapping userRole:roleList.getData()) {
                    urlList.put(userRole.getRoleName(),userRole.getUrlList());

                }
            }
        }
        if(backUser!=null){
            for (Map.Entry<String,List<String>> entry: urlList.entrySet()) {
                if (entry.getKey().equals(backUser.getData().getRoleName())){
                    for (String roleUrl:entry.getValue()) {
                        if(roleUrl.equals(url)|| url.startsWith(roleUrl)){
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        for (Map.Entry<String,List<String>> entry: urlList.entrySet()) {
            if (entry.getKey().equals(user.getData().getRoles().get(0).getRoleName())){
                for (String roleUrl:entry.getValue()) {
                    if(roleUrl.equals(url)|| url.startsWith(roleUrl)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    Map<String,String> freeUrlList=null;
    public boolean checkIsFreeUrl(String url){
        if (freeUrlList==null || freeUrlList.size()==0){
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
