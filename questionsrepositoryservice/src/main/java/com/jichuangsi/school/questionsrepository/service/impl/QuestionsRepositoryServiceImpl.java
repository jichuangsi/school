package com.jichuangsi.school.questionsrepository.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jichuangsi.microservice.common.constant.ResultCode;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.entity.QuestionStatistics;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.model.*;
import com.jichuangsi.school.questionsrepository.model.transfer.TransferTeacher;
import com.jichuangsi.school.questionsrepository.repository.QuestionStatisticsRepository;
import com.jichuangsi.school.questionsrepository.service.IQuestionsRepositoryService;
import com.jichuangsi.school.questionsrepository.service.IUserInfoService;
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
import java.util.*;

import static com.jichuangsi.school.questionsrepository.constant.ResultCode.PHP_CORRECT_CODE;


@Service
@CacheConfig(cacheNames = {"questions"})
public class QuestionsRepositoryServiceImpl implements IQuestionsRepositoryService {

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

    @Value("${com.jichuangsi.school.tiku.knowledgeApi}")
    private String knowledgeApi;

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private QuestionStatisticsRepository questionStatisticsRepository;

    @Override
    @Cacheable(unless="#result.isEmpty()", keyGenerator = "editionKeyGenerator")
    public List<EditionTreeNode> getTreeForSubjectEditionInfo(UserInfoForToken userInfo) throws QuestionRepositoryServiceException {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("accessKey", accessKey);

        /*resp.subscribe(text -> phpResponseModel = JSONObject.parseObject(text,
                new TypeReference<PHPResponseModel<EditionTreeNode>>() {}.getType()));
        try{
            TimeUnit.SECONDS.sleep(5);
        }catch (InterruptedException exp){
            exp.printStackTrace();
        }
        return phpResponseModel.getData();*/

        try {
            Mono<String> resp = webClientFormRequest(subjectEditionApi, formData);
            Optional<String> result = resp.blockOptional();
            if (result.isPresent()) {
                PHPResponseModel phpResponseModel = JSONObject.parseObject(resp.block(),
                        new TypeReference<PHPResponseModel<EditionTreeNode>>() {
                        }.getType());
                if (!PHP_CORRECT_CODE.equalsIgnoreCase(phpResponseModel.getErrorCode())) {
                    throw new QuestionRepositoryServiceException(phpResponseModel.getErrorCode());
                }
                return phpResponseModel.getData();
            }
        } catch (Exception exp) {
            throw new QuestionRepositoryServiceException(exp.getMessage());
        }
        return null;
    }

    @Override
    public List<EditionTreeNode> getTreeForSubjectEditionInfoByTeacher(UserInfoForToken userInfo) throws QuestionRepositoryServiceException {
        List<EditionTreeNode> editionTreeNodes =  getTreeForSubjectEditionInfo(userInfo);
        if(editionTreeNodes == null) return null;

        return getEditionsByTeacherInfo(userInfo, editionTreeNodes);
    }

    @Override
    @Cacheable(unless = "#result.isEmpty()", keyGenerator = "basicInfoKeyGenerator")
    public Map<String, List> getMapForOtherBasicInfo(UserInfoForToken userInfo) throws QuestionRepositoryServiceException {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("accessKey", accessKey);

        try {
            Mono<String> resp = webClientFormRequest(otherBasicApi, formData);
            Optional<String> result = resp.blockOptional();
            if (result.isPresent()) {
                PHPResponseModel2 phpResponseModel2 = JSONObject.parseObject(resp.block(),
                        new TypeReference<PHPResponseModel2<QuestionTypeNode, PaperTypeNode, DifficultyTypeNode, YearNode, AreaNode>>() {
                        }.getType());
                if (!PHP_CORRECT_CODE.equalsIgnoreCase(phpResponseModel2.getErrorCode())) {
                    throw new QuestionRepositoryServiceException(phpResponseModel2.getErrorCode());
                }
                Map<String, List> result2 = new HashMap<String, List>();
                result2.put("questionType", phpResponseModel2.getQtypes());
                result2.put("paperType", phpResponseModel2.getPaperTypes());
                result2.put("difficultyType", phpResponseModel2.getDiffTypes());
                result2.put("years", phpResponseModel2.getYears());
                result2.put("areas", phpResponseModel2.getAreas());
                return result2;
            }
        } catch (Exception exp) {
            throw new QuestionRepositoryServiceException(exp.getMessage());
        }
        return null;
    }

    @Override
    //@Cacheable(key = "#root.methodName", unless = "#result.isEmpty()")
    public Map<String, List> getAllBasicInfoForQuestionSelection(UserInfoForToken userInfo) throws QuestionRepositoryServiceException {
        Map<String, List> allBasicInfo = new HashMap<String, List>();

        allBasicInfo.put("subjectEditionTree", this.getTreeForSubjectEditionInfo(userInfo));

        allBasicInfo.putAll(this.getMapForOtherBasicInfo(userInfo));

        return allBasicInfo;
    }

    @Override
    @Cacheable(unless = "#result.isEmpty()", keyGenerator = "chaptersKeyGenerator")
    public List<ChapterTreeNode> getTreeForChapterInfo(UserInfoForToken userInfoForToken, ChapterQueryModel chapterQueryModel) throws QuestionRepositoryServiceException {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("accessKey", accessKey);
        formData.add("pharseId", chapterQueryModel.getPharseId());
        formData.add("gradeId", chapterQueryModel.getGradeId());
        formData.add("subjectId", chapterQueryModel.getSubjectId());
        formData.add("editionId", chapterQueryModel.getEditionId());

        try {
            Mono<String> resp = webClientFormRequest(chapterApi, formData);
            Optional<String> result = resp.blockOptional();
            if (result.isPresent()) {
                PHPResponseModel phpResponseModel = JSONObject.parseObject(resp.block(),
                        new TypeReference<PHPResponseModel<ChapterTreeNode>>() {
                        }.getType());
                if (!PHP_CORRECT_CODE.equalsIgnoreCase(phpResponseModel.getErrorCode())) {
                    throw new QuestionRepositoryServiceException(phpResponseModel.getErrorCode());
                }
                return phpResponseModel.getData();
            }
        } catch (Exception exp) {
            throw new QuestionRepositoryServiceException(exp.getMessage());
        }

        return null;
    }

    @Override
    //@Cacheable(unless="#result.isEmpty()", keyGenerator = "questionsKeyGenerator")
    public PageHolder<QuestionNode> getListForQuestionsByKnowledge(UserInfoForToken userInfoForToken, QuestionQueryModel questionQueryModel) throws QuestionRepositoryServiceException {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("accessKey", accessKey);
        formData.add("knowledgeId", questionQueryModel.getKnowledgeId());
        formData.add("qtypeId", questionQueryModel.getQtypeId());
        formData.add("paperType", questionQueryModel.getPaperType());
        formData.add("diff", questionQueryModel.getDiff());
        formData.add("year", questionQueryModel.getYear());
        formData.add("area", questionQueryModel.getArea());
        formData.add("page", questionQueryModel.getPage());
        formData.add("pageSize", questionQueryModel.getPageSize());

        try {
            Mono<String> resp = webClientFormRequest(questionApi, formData);
            Optional<String> result = resp.blockOptional();
            if (result.isPresent()) {
                PHPResponseModel3<PageHolder<QuestionNode>> phpResponseModel3 = JSONObject.parseObject(resp.block(),
                        new TypeReference<PHPResponseModel3<PageHolder<QuestionNode>>>() {
                        }.getType());
                if (!PHP_CORRECT_CODE.equalsIgnoreCase(phpResponseModel3.getErrorCode())) {
                    throw new QuestionRepositoryServiceException(phpResponseModel3.getErrorCode());
                }
                return phpResponseModel3.getData();
            }
        } catch (Exception exp) {
            throw new QuestionRepositoryServiceException(exp.getMessage());
        }

        return null;
    }

    @Override
    public PageHolder<QuestionExtraNode> getListForQuestionsExtraByKnowledge(UserInfoForToken userInfoForToken, QuestionQueryModel questionQueryModel) throws QuestionRepositoryServiceException{
        PageHolder<QuestionNode> questionNodes = this.getListForQuestionsByKnowledge(userInfoForToken, questionQueryModel);
        if(questionNodes == null) return null;

        PageHolder<QuestionExtraNode> questionExtraNodes = new PageHolder<QuestionExtraNode>();
        List<QuestionExtraNode> content = new ArrayList<QuestionExtraNode>();
        questionNodes.getContent().forEach(q -> {
            QuestionExtraNode questionExtraNode = new QuestionExtraNode();
            questionExtraNode.setQuestionNode(q);
            QuestionStatistics qs = questionStatisticsRepository.findOneByQidMD52(q.getQid());
            if(qs != null){
                questionExtraNode.setAddPapercount(qs.getAddPapercount());
                questionExtraNode.setAnswerCount(qs.getAnswerCount());
                questionExtraNode.setAverage(qs.getAverage());
            }
            content.add(questionExtraNode);
        });
        questionExtraNodes.setPageCount(questionNodes.getPageCount());
        questionExtraNodes.setPageNum(questionNodes.getPageNum());
        questionExtraNodes.setPageSize(questionNodes.getPageSize());
        questionExtraNodes.setTotal(questionNodes.getTotal());
        questionExtraNodes.setContent(content);

        return questionExtraNodes;
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
                if(!PHP_CORRECT_CODE.equalsIgnoreCase(phpResponseModel.getErrorCode())){
                    throw new QuestionRepositoryServiceException(phpResponseModel.getErrorCode());
                }
                return phpResponseModel.getData();
            }
        }catch(Exception exp){
            throw new QuestionRepositoryServiceException(exp.getMessage());
        }

        return null;
    }

    @Override
    @Cacheable(unless = "#result.isEmpty()", keyGenerator = "knowledgeKeyGenerator")
    public List<KnowledgeTreeNode> getTreeForKnowledgeInfoByTeacher(String phraseId, String subjectId) throws QuestionRepositoryServiceException{
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("accessKey", accessKey);
        formData.add("pharseId", phraseId);
        formData.add("subjectId", subjectId);

        try {
            Mono<String> resp = webClientFormRequest(knowledgeApi, formData);
            Optional<String> result = resp.blockOptional();
            if (result.isPresent()) {
                PHPResponseModel phpResponseModel = JSONObject.parseObject(resp.block(),
                        new TypeReference<PHPResponseModel<KnowledgeTreeNode>>() {
                        }.getType());
                if (!PHP_CORRECT_CODE.equalsIgnoreCase(phpResponseModel.getErrorCode())) {
                    throw new QuestionRepositoryServiceException(phpResponseModel.getErrorCode());
                }
                return phpResponseModel.getData();
            }
        } catch (Exception exp) {
            throw new QuestionRepositoryServiceException(exp.getMessage());
        }

        return null;

    }

    /*@Override
    public PageHolder<Map> SortByAddPapercount(UserInfoForToken userInfoForToken, QuestionQueryModel questionQueryModel) throws QuestionRepositoryServiceException {
        PageHolder<QuestionNode> listForQuestionsByKnowledge = getListForQuestionsByKnowledge(userInfoForToken, questionQueryModel);
        List<QuestionNode> content = listForQuestionsByKnowledge.getContent();
        PageHolder<Map> pageHolder=null;
        TreeMap<QuestionNode,Integer> map=null;
        for (QuestionNode q:content
                ) {
            QuestionStatistics oneByIdMD52 = questionStatisticsRepository.findOneByQidMD52(q.getQid());
            map.put(q,oneByIdMD52.getAddPapercount());
            pageHolder.getContent().add(map);

        }
        return pageHolder;
    }

    //@Cacheable(unless="#result.isEmpty()", keyGenerator = "questionsKeyGenerator")
    //根据回到次数.
    @Override
    public PageHolder<Map<QuestionNode,Integer>> SortByAnswerCount(UserInfoForToken userInfoForToken, QuestionQueryModel questionQueryModel) throws QuestionRepositoryServiceException {
        PageHolder<QuestionNode> listForQuestionsByKnowledge = getListForQuestionsByKnowledge(userInfoForToken, questionQueryModel);
        List<QuestionNode> content = listForQuestionsByKnowledge.getContent();
        Map<QuestionNode ,Integer> map=new TreeMap<>();
        PageHolder<Map<QuestionNode,Integer>> pageHolder=null;
        for (QuestionNode q:content
                ) {
            QuestionStatistics oneByIdMD52 = questionStatisticsRepository.findOneByQidMD52(q.getQid());
            map.put(q,oneByIdMD52.getAnswerCount());
            pageHolder.getContent().add(map);
        }
        return pageHolder;
    }*/

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

    private List<EditionTreeNode> getEditionsByTeacherInfo(UserInfoForToken userInfo, List<EditionTreeNode> editions) {
        TransferTeacher teacher = userInfoService.getUserForTeacherById(userInfo.getUserId());

        for (int i = editions.size() - 1; i >= 0; i--) {
            if (!editions.get(i).getCode().equals(teacher.getPhraseId())) {
                editions.remove(editions.get(i));
                continue;
            }
            for (EditionTreeNode node2 : editions.get(i).getChild()) {
                for (int j = node2.getChild().size() - 1; j >= 0; j--) {
                    if (!node2.getChild().get(j).getCode().equals(teacher.getSubjectId())) {
                        node2.getChild().remove(node2.getChild().get(j));
                    }
                }
            }
        }
        return editions;
    }
}
