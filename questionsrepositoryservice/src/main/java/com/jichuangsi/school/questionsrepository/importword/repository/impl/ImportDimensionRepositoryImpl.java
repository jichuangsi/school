package com.jichuangsi.school.questionsrepository.importword.repository.impl;

import com.jichuangsi.school.questionsrepository.entity.Dimension;
import com.jichuangsi.school.questionsrepository.importword.repository.ImportDimensionRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class ImportDimensionRepositoryImpl implements ImportDimensionRepository {
    @Resource
    private MongoTemplate mongoTemplate;
    @Override
    public void addWordTitle(Dimension dimension) {
        mongoTemplate.save(dimension);
    }
}
