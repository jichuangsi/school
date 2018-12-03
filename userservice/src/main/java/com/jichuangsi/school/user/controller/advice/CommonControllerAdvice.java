package com.jichuangsi.school.user.controller.advice;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.model.System.Role;
import com.jichuangsi.school.user.model.roles.Student;
import com.jichuangsi.school.user.model.roles.Teacher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestControllerAdvice
public class CommonControllerAdvice {

	@Value("${com.jichuangsi.school.token.userClaim}")
	private String userClaim;

	@ModelAttribute
	public UserInfoForToken translateHeader(@RequestHeader @Nullable String userInfo, @RequestHeader @Nullable String accessToken,
											Model model) throws UnsupportedEncodingException {

		if (!StringUtils.isEmpty(accessToken)) {
			DecodedJWT jwt = JWT.decode(accessToken);
			String user = jwt.getClaim(userClaim).asString();
			//model.addAttribute(userClaim, JSONObject.parseObject(roles,UserInfoForToken.class));
			return JSONObject.parseObject(user,UserInfoForToken.class);
		}
		return null;
	}

	@ModelAttribute
	public List<Role> translateBody(Model model, @RequestBody @Nullable LinkedHashMap param) throws UnsupportedEncodingException{

		List<Role> userRoles = new ArrayList<Role>();
		if(param==null||param.isEmpty()) return null;
		java.util.List roles = ((java.util.List)param.get("roles"));
		if(roles==null||roles.isEmpty()) return null;
		roles.forEach(role -> {
			LinkedHashMap r = (LinkedHashMap)role;
			try{
				Type t = Class.forName("com.jichuangsi.school.user.model.roles."+capitalStr((String)r.get("roleName")));
				userRoles.add(JSONObject.parseObject(JSONObject.toJSONString(role),	t));
			}catch (ClassNotFoundException cnfExp){

			}
		});

		return userRoles;
	}

	@ExceptionHandler
	public ResponseModel<Object> handler(Exception e) {
		return ResponseModel.fail("", e.getMessage());

	}

	private String capitalStr(String str) {
		char[] cs=str.toLowerCase().toCharArray();
		cs[0]-=32;
		return String.valueOf(cs);

	}

}
