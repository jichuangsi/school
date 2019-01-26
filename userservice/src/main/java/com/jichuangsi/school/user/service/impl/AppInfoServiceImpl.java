package com.jichuangsi.school.user.service.impl;

import com.jichuangsi.school.user.entity.app.AppInfoEntity;
import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.model.app.AppInfoModule;
import com.jichuangsi.school.user.model.app.AppInfoQueryModule;
import com.jichuangsi.school.user.service.IAppInfoService;
import com.jichuangsi.school.user.util.MappingEntity2ModelConverter;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class AppInfoServiceImpl implements IAppInfoService {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public AppInfoModule fetchLastestAppInfo(AppInfoQueryModule appInfoQueryModule) throws UserServiceException{
        Criteria criteria = Criteria.where("name").is(appInfoQueryModule.getAppName()).and("type").is(appInfoQueryModule.getAppType());
        AppInfoEntity appInfoEntity = mongoTemplate.findOne(new Query(criteria).with(new Sort(Sort.Direction.DESC,"upgradeTime")).skip(0).limit(1), AppInfoEntity.class);

        return MappingEntity2ModelConverter.ConvertAppInfo(appInfoEntity);
    }
}
