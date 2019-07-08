package com.jichuangsi.school.user.feign.service.impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.entity.org.ClassInfo;
import com.jichuangsi.school.user.entity.org.GradeInfo;
import com.jichuangsi.school.user.entity.org.SchoolInfo;
import com.jichuangsi.school.user.feign.model.ClassStudentModel;
import com.jichuangsi.school.user.feign.service.FeignReportService;
import com.jichuangsi.school.user.model.school.GradeModel;
import com.jichuangsi.school.user.util.MappingEntity2ModelConverter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
public class FeignReportServiceImpl implements FeignReportService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    MongoTemplate mongoTemplate;


    //查年级
    @Override
    public List<GradeModel> getGradeBySchoolId(String schoolId) {
        //根据学校id查年级
        SchoolInfo schoolInfo1=mongoTemplate.findOne(new Query(Criteria.where("id").is(schoolId)),SchoolInfo.class);

        List<GradeModel> gradeModels=getGrade(schoolInfo1.getGradeIds());
        return gradeModels;
    }

    //根据年级Id查年级详细信息
    private  List<GradeModel> getGrade(List<String> gradeId){
        List<GradeInfo> gradeModel=mongoTemplate.find(new Query(Criteria.where("id").in(gradeId)),GradeInfo.class);
        List<GradeModel> gradeModels=new ArrayList<GradeModel>();
        GradeModel gradeModel1=null;
        for (GradeInfo g:gradeModel ) {
            gradeModel1=MappingEntity2ModelConverter.CONVERTERFROMGRADEINFO(g);
            gradeModels.add(gradeModel1);
        }
        return gradeModels;
    }

    //根据年级班级查学生
    @Override
    public List<ClassStudentModel> getStudentByGradeId(String gradeId) {
        //根据年级id查询所有班级
        GradeInfo gradeInfo=mongoTemplate.findById(gradeId,GradeInfo.class);
        List<ClassStudentModel> classStudentModel=new ArrayList<ClassStudentModel>();
        ClassStudentModel classStudentModel1=null;
        ClassInfo classinfo=new ClassInfo();
        List<UserInfo> userInfos=new ArrayList<UserInfo>();
        for (String id:gradeInfo.getClassIds()) {
            classinfo=mongoTemplate.findById(id,ClassInfo.class);
            userInfos=getStudentIdByClassId(id);
            classStudentModel1=new ClassStudentModel(classinfo.getId(),classinfo.getName(),userInfos);
            classStudentModel.add(classStudentModel1);
        }


        return classStudentModel;
    }

    //根据班级查询学生
    public List<UserInfo> getStudentIdByClassId(String classId) {
        Criteria criteria1=Criteria.where("primaryClass.classId").is(classId);
        Criteria criteria=Criteria.where("roleInfos").elemMatch(criteria1).and("roleInfos.roleName").is("Student");
        Query query=new Query(criteria);
        return mongoTemplate.find(query,UserInfo.class);
    }


    @Override
    public List<UserInfo> getTestByGradeIdAndSubjectName(String gradeId, String subjectName) {
        Criteria criteria1=Criteria.where("primaryGrade.gradeId").is(gradeId);//roleInfos.0.primarySubject.subjectId
        Criteria criteria=Criteria.where("roleInfos").elemMatch(criteria1);
        Criteria criteria2=Criteria.where("primarySubject.subjectName").is(subjectName);
       // Criteria criteria3=Criteria.where("roleInfos").elemMatch(criteria2);
        criteria.andOperator(Criteria.where("roleInfos").elemMatch(criteria2));
       // Criteria criteria4=Criteria.where()
                Query query=new Query(criteria);

        return mongoTemplate.find(query,UserInfo.class);
    }
}
