package com.jichuangsi.school.user.repository.backstage;

import com.jichuangsi.school.user.entity.backstage.SchoolAttachment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ISchoolAttachmentRepository extends MongoRepository<SchoolAttachment,String> {

    int countByAndOriginalName(String name);

    List<SchoolAttachment> findAll();
}
