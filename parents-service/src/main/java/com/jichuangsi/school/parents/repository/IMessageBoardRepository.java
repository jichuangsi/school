package com.jichuangsi.school.parents.repository;

import com.jichuangsi.school.parents.entity.MessageBoard;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IMessageBoardRepository extends MongoRepository<MessageBoard,String> {
}
