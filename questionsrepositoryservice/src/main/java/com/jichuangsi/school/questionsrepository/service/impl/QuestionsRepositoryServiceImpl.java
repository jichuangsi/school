package com.jichuangsi.school.questionsrepository.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.model.*;
import com.jichuangsi.school.questionsrepository.service.IQuestionsRepositoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
@CacheConfig(cacheNames = {"questions"})
public class QuestionsRepositoryServiceImpl implements IQuestionsRepositoryService{

    @Value("${com.jichuangsi.school.tiku.schema}")
    private String schema;

    @Value("${com.jichuangsi.school.tiku.host}")
    private String host;

    @Value("${com.jichuangsi.school.tiku.accessKey}")
    private String accessKey;

    @Value("${com.jichuangsi.school.tiku.subjectEditionApi}")
    private String subjectEditionApi;

    @Value("${com.jichuangsi.school.tiku.otherBasicApi}")
    private String otherBasicApi;

    @Value("${com.jichuangsi.school.tiku.chapterApi}")
    private String chapterApi;

    @Value("${com.jichuangsi.school.tiku.questionApi}")
    private String questionApi;

    @Value("${com.jichuangsi.school.tiku.answerApi}")
    private String answerApi;

    @Override
    @Cacheable(key = "#root.methodName", unless="#result.isEmpty()")
    public List<EditionTreeNode> getTreeForSubjectEditionInfo(UserInfoForToken userInfo) throws QuestionRepositoryServiceException{

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("accessKey",accessKey);

        /*resp.subscribe(text -> phpResponseModel = JSONObject.parseObject(text,
                new TypeReference<PHPResponseModel<EditionTreeNode>>() {}.getType()));
        try{
            TimeUnit.SECONDS.sleep(5);
        }catch (InterruptedException exp){
            exp.printStackTrace();
        }
        return phpResponseModel.getData();*/

        try{
            Mono<String> resp = webClientFormRequest(subjectEditionApi, formData);
            Optional<String> result = resp.blockOptional();
            if(result.isPresent()) {
                PHPResponseModel phpResponseModel = JSONObject.parseObject(resp.block(),
                        new TypeReference<PHPResponseModel<EditionTreeNode>>() {}.getType());
                return phpResponseModel.getData();
            }
        }catch (Exception exp){
            throw new QuestionRepositoryServiceException(exp.getMessage());
        }
        return null;
    }

    @Override
    @Cacheable(key = "#root.methodName", unless="#result.isEmpty()")
    public Map<String, List> getMapForOtherBasicInfo(UserInfoForToken userInfo) throws QuestionRepositoryServiceException {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("accessKey",accessKey);

        try{
            Mono<String> resp = webClientFormRequest(otherBasicApi, formData);
            Optional<String> result = resp.blockOptional();
            if(result.isPresent()) {
                PHPResponseModel2 phpResponseModel2 = JSONObject.parseObject(resp.block(),
                        new TypeReference<PHPResponseModel2<QuestionTypeNode,PaperTypeNode,DifficultyTypeNode,YearNode,AreaNode>>() {}.getType());
                Map<String, List> result2 = new HashMap<String, List>();
                result2.put("questionType", phpResponseModel2.getQtypes());
                result2.put("paperType", phpResponseModel2.getPaperTypes());
                result2.put("difficultyType", phpResponseModel2.getDiffTypes());
                result2.put("years", phpResponseModel2.getYears());
                result2.put("areas", phpResponseModel2.getAreas());
                return result2;
            }
        }catch (Exception exp){
            throw new QuestionRepositoryServiceException(exp.getMessage());
        }
        return null;
    }

    @Override
    @Cacheable(key = "#root.methodName", unless="#result.isEmpty()")
    public Map<String, List> getAllBasicInfoForQuestionSelection(UserInfoForToken userInfo) throws QuestionRepositoryServiceException{
        Map<String, List> allBasicInfo = new HashMap<String, List>();

        allBasicInfo.put("subjectEditionTree", this.getTreeForSubjectEditionInfo(userInfo));

        allBasicInfo.putAll(this.getMapForOtherBasicInfo(userInfo));

        return allBasicInfo;
    }

    @Override
    @Cacheable(unless="#result.isEmpty()", keyGenerator = "chaptersKeyGenerator")
    public List<ChapterTreeNode> getTreeForChapterInfo(UserInfoForToken userInfoForToken, ChapterQueryModel chapterQueryModel) throws QuestionRepositoryServiceException {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("accessKey",accessKey);
        formData.add("pharseId",chapterQueryModel.getPharseId());
        formData.add("gradeId",chapterQueryModel.getGradeId());
        formData.add("subjectId",chapterQueryModel.getSubjectId());
        formData.add("editionId",chapterQueryModel.getEditionId());

        try{
            Mono<String> resp = webClientFormRequest(chapterApi, formData);
            Optional<String> result = resp.blockOptional();
            if(result.isPresent()) {
                PHPResponseModel phpResponseModel = JSONObject.parseObject(resp.block(),
                        new TypeReference<PHPResponseModel<ChapterTreeNode>>() {}.getType());
                return phpResponseModel.getData();
            }
        }catch(Exception exp){
            throw new QuestionRepositoryServiceException(exp.getMessage());
        }

        return null;
    }

    @Override
    //@Cacheable(unless="#result.isEmpty()", keyGenerator = "questionsKeyGenerator")
    public List<QuestionNode> getListForQuestionsByKnowledge(UserInfoForToken userInfoForToken, QuestionQueryModel questionQueryModel) throws QuestionRepositoryServiceException {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("accessKey",accessKey);
        formData.add("knowledgeId",questionQueryModel.getKnowledgeId());
        formData.add("qtypeId",questionQueryModel.getQtypeId());
        formData.add("paperType",questionQueryModel.getPaperType());
        formData.add("diff",questionQueryModel.getDiff());
        formData.add("year",questionQueryModel.getYear());
        formData.add("area",questionQueryModel.getArea());
        formData.add("page",questionQueryModel.getPage());
        formData.add("pageSize",questionQueryModel.getPageSize());

        try{
            Mono<String> resp = webClientFormRequest(questionApi, formData);
            Optional<String> result = resp.blockOptional();
            if(result.isPresent()) {
                PHPResponseModel phpResponseModel = JSONObject.parseObject(resp.block(),
                        new TypeReference<PHPResponseModel<QuestionNode>>() {}.getType());
                return phpResponseModel.getData();
            }
        }catch(Exception exp){
            throw new QuestionRepositoryServiceException(exp.getMessage());
        }

        return null;
    }

    @Override
    public List<AnswerNode> getListForAnswersByQuestionId(UserInfoForToken userInfo, AnswerQueryModel answerQueryModel) throws QuestionRepositoryServiceException {
        if(answerQueryModel.getQids().isEmpty()) return null;
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("accessKey",accessKey);
        String qidsStr = StringUtils.trimAllWhitespace(Arrays.toString(StringUtils.toStringArray(answerQueryModel.getQids())));
        formData.add("qid",qidsStr.substring(1, qidsStr.length() - 1));
        try{
            Mono<String> resp = webClientFormRequest(answerApi, formData);
            Optional<String> result = resp.blockOptional();
            if(result.isPresent()) {
                PHPResponseModel phpResponseModel = JSONObject.parseObject(resp.block(),
                        new TypeReference<PHPResponseModel<AnswerNode>>() {}.getType());
                return phpResponseModel.getData();
            }
        }catch(Exception exp){
            throw new QuestionRepositoryServiceException(exp.getMessage());
        }

        return null;
    }

    private Mono<String> webClientFormRequest(String api, MultiValueMap formData) throws Exception {
        Mono<String> resp = WebClient.create().post().uri(uriBuilder -> uriBuilder
                .scheme(schema)
                .host(host)
                .path("/index.php")
                .queryParam("s","Index").queryParam("m","Api")
                .queryParam("a", api).build())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromObject(formData))
                .retrieve().bodyToMono(String.class);

        return resp;
    }
}
