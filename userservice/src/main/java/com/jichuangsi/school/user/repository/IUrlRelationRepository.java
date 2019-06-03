package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.model.UrlMapping;
import com.jichuangsi.school.user.entity.UrlRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface IUrlRelationRepository extends JpaRepository<UrlRelation,String> {
    void deleteByUid(String uid);
}
