package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.UrlRelation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IUrlRelationRepository extends JpaRepository<UrlRelation,String> {
    //
    void deleteByUid(String uid);
}
