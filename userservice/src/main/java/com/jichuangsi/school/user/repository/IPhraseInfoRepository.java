package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.org.PhraseInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IPhraseInfoRepository extends MongoRepository<PhraseInfo,String> {

    PhraseInfo findFirstById(String id);

    List<PhraseInfo> findByDeleteFlagAndIdIn(String delete, List<String> ids);

    int countByPhraseNameAndDeleteFlagAndIdIn(String phraseName,String delete,List<String> ids);

    PhraseInfo findFirstByDeleteFlagAndPhraseNameAndIdInOrderByCreatedTimeDesc(String delete,String name,List<String> ids);

    PhraseInfo findFirstByGradeIdsContaining(String gradeId);
}
