package com.jichuangsi.school.parents.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.parents.exception.ParentsException;
import com.jichuangsi.school.parents.model.NewsDetailModel;
import com.jichuangsi.school.parents.model.NewsSummaryModel;
import com.jichuangsi.school.parents.model.PHPResponseModel;
import com.jichuangsi.school.parents.service.INewsInfoService;
import com.jichuangsi.school.parents.commons.ResultCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class NewsInfoServiceImpl implements INewsInfoService {

    @Value("${com.jichuangsi.school.portal.schema}")
    private String schema;

    @Value("${com.jichuangsi.school.portal.host}")
    private String host;

    @Value("${com.jichuangsi.school.portal.port}")
    private String port;

    @Value("${com.jichuangsi.school.portal.news.path}")
    private String newsPath;

    @Value("${com.jichuangsi.school.portal.news.listApi}")
    private String newsListApi;

    @Value("${com.jichuangsi.school.portal.news.detailApi}")
    private String newDetailApi;

    @Value("${com.jichuangsi.school.portal.news.lookupApi}")
    private String newLookupApi;

    @Override
    public List<NewsSummaryModel> getNewsInfoList(UserInfoForToken userInfo, String status, int pageindex, int pagesize) throws ParentsException{

        try {
            Mono<String> resp = WebClient.create().get().uri(uriBuilder -> uriBuilder
                    .scheme(schema)
                    .host(host).port(port)
                    .path(newsPath).path(newsListApi)
                    .queryParam("status",status)
                    .queryParam("pageindex",pageindex)
                    .queryParam("pagesize", pagesize).build())
                    .retrieve().bodyToMono(String.class);
            Optional<String> result = resp.blockOptional();
            if (result.isPresent()) {
                PHPResponseModel phpResponseModel = JSONObject.parseObject(resp.block(),
                        new TypeReference<PHPResponseModel<NewsSummaryModel>>() {
                        }.getType());
                if (!ResultCode.PHP_CORRECT_CODE.equalsIgnoreCase(phpResponseModel.getErrorCode())) {
                    throw new ParentsException(phpResponseModel.getErrorstatus());
                }
                return phpResponseModel.getData();
            }
        } catch (Exception exp) {
            throw new ParentsException(exp.getMessage());
        }

        return null;
    }

    @Override
    public List<NewsDetailModel> getNewsInfoDetail(UserInfoForToken userInfo, String infoid) throws ParentsException{
        try {
            Mono<String> resp = WebClient.create().get().uri(uriBuilder -> uriBuilder
                    .scheme(schema)
                    .host(host).port(port)
                    .path(newsPath).path(newDetailApi)
                    .queryParam("infoid",infoid).build())
                    .retrieve().bodyToMono(String.class);
            Optional<String> result = resp.blockOptional();
            if (result.isPresent()) {
                PHPResponseModel phpResponseModel = JSONObject.parseObject(resp.block(),
                        new TypeReference<PHPResponseModel<NewsDetailModel>>() {
                        }.getType());
                if (!ResultCode.PHP_CORRECT_CODE.equalsIgnoreCase(phpResponseModel.getErrorCode())) {
                    throw new ParentsException(phpResponseModel.getErrorstatus());
                }
                return phpResponseModel.getData();
            }
        } catch (Exception exp) {
            throw new ParentsException(exp.getMessage());
        }

        return null;
    }

    @Override
    public List<NewsSummaryModel> queryNewsByKeywords(UserInfoForToken userInfo, String title, int pageindex, int pagesize) throws ParentsException{
        try {
            Mono<String> resp = WebClient.create().get().uri(uriBuilder -> uriBuilder
                    .scheme(schema)
                    .host(host).port(port)
                    .path(newsPath).path(newLookupApi)
                    .queryParam("title",title)
                    .queryParam("pageindex",pageindex)
                    .queryParam("pagesize", pagesize).build())
                    .retrieve().bodyToMono(String.class);
            Optional<String> result = resp.blockOptional();
            if (result.isPresent()) {
                PHPResponseModel phpResponseModel = JSONObject.parseObject(resp.block(),
                        new TypeReference<PHPResponseModel<NewsSummaryModel>>() {
                        }.getType());
                if (!ResultCode.PHP_CORRECT_CODE.equalsIgnoreCase(phpResponseModel.getErrorCode())) {
                    throw new ParentsException(phpResponseModel.getErrorstatus());
                }
                return phpResponseModel.getData();
            }
        } catch (Exception exp) {
            throw new ParentsException(exp.getMessage());
        }

        return null;
    }
}
