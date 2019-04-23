package com.jichuangsi.school.statistics.service.impl;

import com.jichuangsi.microservice.common.constant.ResultCode;
import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.entity.StudentAddCourseEntity;
import com.jichuangsi.school.statistics.entity.StudentStatisticsEntity;
import com.jichuangsi.school.statistics.entity.performance.student.CoursePerformanceEntity;
import com.jichuangsi.school.statistics.exception.QuestionResultException;
import com.jichuangsi.school.statistics.feign.ICourseFeignService;
import com.jichuangsi.school.statistics.feign.IUserFeignService;
import com.jichuangsi.school.statistics.feign.model.ClassDetailModel;
import com.jichuangsi.school.statistics.feign.model.TransferStudent;
import com.jichuangsi.school.statistics.model.classType.ClassStatisticsModel;
import com.jichuangsi.school.statistics.model.classType.SearchStudentKnowledgeModel;
import com.jichuangsi.school.statistics.model.classType.StudentKnowledgeModel;
import com.jichuangsi.school.statistics.repository.StudentAddCourseRepository;
import com.jichuangsi.school.statistics.service.IClassStatisticsService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
@CacheConfig(cacheNames = {"statisticsCache"})
public class ClassStatisticsServiceImpl implements IClassStatisticsService {

    @Resource
    private IUserFeignService userFeignService;
    @Resource
    private ICourseFeignService courseFeignService;
    @Resource
    private StudentAddCourseRepository studentAddCourseRepository;
	@Resource
    private MongoTemplate mongoTemplate;
	
    @Override
    @Cacheable(unless = "#result.empty",keyGenerator = "classStatisticsKeyGenerator")
    public List<ClassStatisticsModel> getTeachClassStatistics(UserInfoForToken userInfo,String subject) throws QuestionResultException {
        if (StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(subject)) {
            throw new QuestionResultException(ResultCode.PARAM_MISS_MSG);
        }
        ResponseModel<List<String>> responseModel = userFeignService.getTeachClassIds(userInfo.getUserId());
        if (!ResultCode.SUCESS.equals(responseModel.getCode())) {
            throw new QuestionResultException(responseModel.getMsg());
        }
        ResponseModel<List<ClassDetailModel>> response1 = userFeignService.getClassDetailByIds(responseModel.getData());
        if (!ResultCode.SUCESS.equals(response1.getCode())) {
            throw new QuestionResultException(response1.getMsg());
        }
        for (ClassDetailModel model : response1.getData()){
            model.setSubject(subject);
            model.setTeacherId(userInfo.getUserId());
        }
        ResponseModel<List<ClassStatisticsModel>> response = courseFeignService.getClassStatisticsByClassIdsOnMonth(response1.getData());
        if (!ResultCode.SUCESS.equals(response.getCode())) {
            throw new QuestionResultException(response.getMsg());
        }
        List<ClassStatisticsModel> classStatisticsModels = response.getData();
        return classStatisticsModels;
    }

    @Override
    @Cacheable(unless = "#result.empty",keyGenerator = "classStudentKnowledgesKeyGenerator")
    public List<StudentKnowledgeModel> getClassStudentKnowledges(UserInfoForToken userInfo, SearchStudentKnowledgeModel model) throws QuestionResultException {
        if (StringUtils.isEmpty(model.getClassId()) || !(model.getQuestionIds().size() > 0)) {
            throw new QuestionResultException(ResultCode.PARAM_MISS_MSG);
        }
        ResponseModel<List<TransferStudent>> responseModel = userFeignService.getStudentsForClass(model.getClassId());
        if (!ResultCode.SUCESS.equals(responseModel.getCode())) {
            throw new QuestionResultException(responseModel.getMsg());
        }
        model.setTransferStudents(responseModel.getData());
        ResponseModel<List<StudentKnowledgeModel>> response = courseFeignService.getStudentKnowledges(model);
        if (!ResultCode.SUCESS.equals(response.getCode())) {
            throw new QuestionResultException(response.getMsg());
        }
        return response.getData();
    }

    @Override
/*    @Cacheable(unless = "#result.isEmpty()",keyGenerator = "getCourseSignKeyGenerator")*/
    public List<TransferStudent> getCourseSign(UserInfoForToken userInfo, String courseId,String classId) throws QuestionResultException {
        if (StringUtils.isEmpty(courseId) || StringUtils.isEmpty(classId)) {
            throw new QuestionResultException(ResultCode.PARAM_MISS_MSG);
        }
        return getSignStudents(courseId, classId);
    }

	@Override
    /*@Cacheable(unless = "#result.empty",key = "T(String).valueOf(#courseId).concat('-').concat(#classId)")*/
    public List<TransferStudent> getSignStudents(String courseId,String classId) throws QuestionResultException{
        List<StudentAddCourseEntity> studentAddCourseEntitys = studentAddCourseRepository
                .findByCourseId(courseId);
        ResponseModel<List<TransferStudent>> responseModel = userFeignService.getStudentsForClass(classId);
        if (!ResultCode.SUCESS.equals(responseModel.getCode())){
            throw new QuestionResultException(responseModel.getMsg());
        }
        for (TransferStudent transferStudent : responseModel.getData()){
            transferStudent.setCommendFlag(this.getCommendInCourse(courseId, transferStudent.getStudentId()));
            for (StudentAddCourseEntity studentAddCourseEntity : studentAddCourseEntitys) {
                if (studentAddCourseEntity.getUserId().equals(transferStudent.getStudentId())){
					transferStudent.setSignFlag("1");
                }
            }
        }
        return responseModel.getData();
    }
	
	private int getCommendInCourse(String courseId, String studentId){
        Query query = new Query();
        query.addCriteria(Criteria.where("studentId").is(studentId).and("coursePerformance.courseId").is(courseId));
        StudentStatisticsEntity studentStatisticsEntity = mongoTemplate.findOne(query, StudentStatisticsEntity.class);
        if(studentStatisticsEntity==null){
            return 0;
        }else{
            CoursePerformanceEntity o = studentStatisticsEntity.getCoursePerformance()
                    .stream().filter(p->p.getCourseId().equalsIgnoreCase(courseId)).findFirst().get();
            if(o == null) return -1;
            else return o.getCommend();
        }
    }
}
