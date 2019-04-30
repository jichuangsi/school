package com.jichuangsi.school.parents.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.parents.exception.ParentsException;
import com.jichuangsi.school.parents.model.NewsDetailModel;
import com.jichuangsi.school.parents.model.NewsSummaryModel;

import java.util.List;

public interface INewsInfoService {

    List<NewsSummaryModel> getNewsInfoList(UserInfoForToken userInfo, String status, int pageindex, int pagesize) throws ParentsException;

    List<NewsDetailModel> getNewsInfoDetail(UserInfoForToken userInfo, String infoid) throws ParentsException;

    List<NewsSummaryModel> queryNewsByKeywords(UserInfoForToken userInfo, String title, int pageindex, int pagesize) throws ParentsException;
}
