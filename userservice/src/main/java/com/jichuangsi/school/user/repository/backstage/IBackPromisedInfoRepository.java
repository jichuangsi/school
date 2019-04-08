package com.jichuangsi.school.user.repository.backstage;

import com.jichuangsi.school.user.entity.backstage.orz.PromisedInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IBackPromisedInfoRepository extends MongoRepository<PromisedInfo,String> {

    PromisedInfo findFirstById(String id);
}
