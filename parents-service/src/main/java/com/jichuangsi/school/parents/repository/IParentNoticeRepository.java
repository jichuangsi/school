package com.jichuangsi.school.parents.repository;

import com.jichuangsi.school.parents.entity.ParentNotice;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IParentNoticeRepository extends MongoRepository<ParentNotice,String> {

    List<ParentNotice> findByParentIdAndDeleteFlag(String parentId,String delete);

    ParentNotice findFirstByIdAndDeleteFlag(String id,String delete);

    ParentNotice findFirstByIdAndParentId(String id,String parentId);

    List<ParentNotice> findByMessageId(String messageId);
}
