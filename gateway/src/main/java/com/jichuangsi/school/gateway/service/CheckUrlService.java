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
                    if(roleUrl.equals(url)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
