package com.jichuangsi.school.questionsrepository.importword.repository;

import com.jichuangsi.school.questionsrepository.entity.FavorQuestions;
import com.jichuangsi.school.questionsrepository.entity.SchoolQuestions;
import com.jichuangsi.school.questionsrepository.entity.SelfQuestions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IImportWordRepository  {
    void addWordTitle(SelfQuestions selfQuestions);
}
