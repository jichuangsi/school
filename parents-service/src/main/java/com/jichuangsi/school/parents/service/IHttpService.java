package com.jichuangsi.school.parents.service;

import com.jichuangsi.school.parents.exception.ParentHttpException;

public interface IHttpService {

    String findWxTokenModel(String code) throws ParentHttpException;

    String findWxUserInfo(String token,String openId,String code) throws ParentHttpException;
}
