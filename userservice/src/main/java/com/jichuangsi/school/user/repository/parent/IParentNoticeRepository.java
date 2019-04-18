package com.jichuangsi.school.user.repository.parent;

import com.jichuangsi.school.user.entity.parent.ParentNotice;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IParentNoticeRepository extends MongoRepository<ParentNotice,String> {

    List<ParentNotice> findByParentIdAndDeleteFlag(String parentId, String delete);

    ParentNotice findFirstByIdAndDeleteFlag(String id, String delete);
}
