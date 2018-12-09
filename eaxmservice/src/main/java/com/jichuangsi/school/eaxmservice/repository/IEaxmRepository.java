package com.jichuangsi.school.eaxmservice.repository;

import com.jichuangsi.school.eaxmservice.entity.Eaxm;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEaxmRepository extends MongoRepository<Eaxm,String>,IEaxmExtraRepository{
    Eaxm findOneByEaxmId(String id);

    List<Eaxm> findByTeacherId(String id);

    /*void deleteEaxmsByEaxmId(List<String> eids);*/
}
