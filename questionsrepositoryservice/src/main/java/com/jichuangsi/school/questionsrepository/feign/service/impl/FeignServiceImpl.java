package com.jichuangsi.school.questionsrepository.feign.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.feign.model.SechQuesionModel;
import com.jichuangsi.school.questionsrepository.feign.service.IFeignService;
import com.jichuangsi.school.questionsrepository.model.PHPResponseModel3;
import com.jichuangsi.school.questionsrepository.model.PageHolder;
import com.jichuangsi.school.questionsrepository.model.QuestionNode;
import com.jichuangsi.school.questionsrepository.model.QuestionQueryModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.jichuangsi.school.questionsrepository.constant.ResultCode.PHP_CORRECT_CODE;

@Service
public class FeignServiceImpl implements IFeignService {
    @Value("${com.jichuangsi.school.tiku.schema}")
    private String schema;

    @Value("${com.jichuangsi.school.tiku.host}")
    private String host;

    @Value("${com.jichuangsi.school.tiku.port}")
    private String port;
    @Value("${com.jichuangsi.school.tiku.accessKey}")
    private String accessKey;
    @Value("${com.jichuangsi.school.tiku.questionApi}")
    private String questionApi;

    @Override
    public PageHolder<QuestionNode> getListForQuestionsBy(UserInfoForToken userInfo, SechQuesionModel questionModel)throws QuestionRepositoryServiceException  {
          MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("accessKey", accessKey);
            formData.add("knowledgeId", questionModel.getKnowledgeId());
            formData.add("qtypeId", questionModel.getQtypeId());
            formData.add("paperType", questionModel.getPaperType());
            formData.add("diff", questionModel.getDiff());
            formData.add("year", questionModel.getYear());
            formData.add("area", questionModel.getArea());
            formData.add("pharseId", questionModel.getPharseId());
            formData.add("gradeId", questionModel.getGradeId());
            formData.add("subjectId", questionModel.getSubjectId());
            formData.add("page", questionModel.getPage());
            formData.add("pageSize", questionModel.getPageSize());

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


    private Mono<String> webClientFormRequest(String api, MultiValueMap formData) throws Exception {
        Mono<String> resp = WebClient.create().post().uri(uriBuilder -> uriBuilder
            .scheme(schema)
            .host(host).port(port)
            .path("/index.php")
            .queryParam("s","Index").queryParam("m","Api")
            .queryParam("a", api).build())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromObject(formData))
            .retrieve().bodyToMono(String.class);
        return resp;
    }
}
