package com.jichuangsi.school.statistics.service.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.constant.ResultCode;
import com.jichuangsi.school.statistics.exception.QuestionResultException;
import com.jichuangsi.school.statistics.feign.ICourseFeignService;
import com.jichuangsi.school.statistics.feign.IHomeWorkStaticsService;
import com.jichuangsi.school.statistics.feign.ITestStaticsService;
import com.jichuangsi.school.statistics.feign.IUserFeignService;
import com.jichuangsi.school.statistics.feign.model.HomeWorkRateModel;
import com.jichuangsi.school.statistics.feign.model.Knowledge;
import com.jichuangsi.school.statistics.feign.model.ResultKnowledgeModel;
import com.jichuangsi.school.statistics.model.Report.*;
import com.jichuangsi.school.statistics.model.result.StudentResultModel;
import com.jichuangsi.school.statistics.service.IFeignService;
import com.jichuangsi.school.statistics.service.IQuestionResultService;
import com.jichuangsi.school.statistics.service.ISchoolReportService;
import com.jichuangsi.school.statistics.exception.SchoolReportException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
@Service
public class SchoolReportServiceImpl implements ISchoolReportService {

    @Resource
    private IUserFeignService userFeignService;
    @Resource
    private IHomeWorkStaticsService homeWorkStaticsService;
    @Resource
    private ITestStaticsService testStaticsService;
    @Resource
    private ICourseFeignService courseFeignService;

    @Override
    public ResponseModel<List<Grade>> getGradeBySchoolId(String schoolId) {
        return userFeignService.getGradeBySchoolId(schoolId);
    }

   @Override
   public List<StudentTestScoreModel> getSchoolTestReport(UserInfoForToken userInfo, StudentTestModel studentTestModel) throws SchoolReportException {
       //查学生班级
       ResponseModel<List<ClassStudentModel>> classStudentModel = userFeignService.getStudentByGradeId(studentTestModel.getGradeId());
       if (!classStudentModel.getCode().equals(ResultCode.SUCESS)) {throw new SchoolReportException(ResultCode.SELECT_NULL_MSG); }
       if (classStudentModel.getData().size() == 0) { throw new SchoolReportException(ResultCode.SELECT_NULL_MSG);}
        //所以班级
       List<String> classId = new ArrayList<String>();
       for (ClassStudentModel c : classStudentModel.getData()) {
           classId.add(c.getClassId());
       }
       ResponseModel<List<TestModel>> testModels = testStaticsService.getTestBySubjectNameAndTestId(classId, studentTestModel.getSubjectName(),studentTestModel.getTime());
       if (!testModels.getCode().equals(ResultCode.SUCESS)) { throw new SchoolReportException(ResultCode.SELECT_NULL_MSG);  }
       if (testModels.getData().size() == 0) { throw new SchoolReportException(ResultCode.SELECT_NULL_MSG);}

       List<StudentTestScoreModel> studentTestScoreModels = new ArrayList<StudentTestScoreModel>();
           StudentTestScoreModel studentTestScoreModel = null;
           for (int c = 0; c < classStudentModel.getData().size(); c++) {
               for (TestModel testModel : testModels.getData()) {
                   if (classStudentModel.getData().get(c).getClassId().equals(testModel.getClassId())) {
                       int studentCount = classStudentModel.getData().get(c).getStudent().size();//班级学生总数

                       ResponseModel<TestScoreModel> tesScoreModel = testStaticsService.getTestByTestId(testModel.getId());
                       if (!tesScoreModel.getCode().equals(ResultCode.SUCESS)) {
                           throw new SchoolReportException(ResultCode.SELECT_NULL_MSG);
                       }
                       if (tesScoreModel.getData() == null) {
                           throw new SchoolReportException(ResultCode.SELECT_NULL_MSG);
                       }
                       studentTestScoreModel = new StudentTestScoreModel(classStudentModel.getData().get(c).getClassName(),
                               testModel.getSubjectName(), tesScoreModel.getData().getTestName(), tesScoreModel.getData().getAvg(),
                               tesScoreModel.getData().getMaxScore(), tesScoreModel.getData().getMinScore(),
                               tesScoreModel.getData().getActualNum(), studentCount - tesScoreModel.getData().getActualNum());
                       studentTestScoreModels.add(studentTestScoreModel);
                   }
               }
           }
       return studentTestScoreModels;
   }

    @Override
    public List<StudentTestScoreModel> getSchoolHomeworkReport(UserInfoForToken userInfo, StudentTestModel studentTestModel)throws SchoolReportException {
        //查学生班级
        ResponseModel<List<ClassStudentModel>> classStudentModel = userFeignService.getStudentByGradeId(studentTestModel.getGradeId());
        if (!classStudentModel.getCode().equals(ResultCode.SUCESS)) { throw new SchoolReportException(ResultCode.SELECT_NULL_MSG); }
        if (classStudentModel.getData().size() == 0) { throw new SchoolReportException(ResultCode.SELECT_NULL_MSG); }
        //所以班级
        List<String> classId = new ArrayList<String>();
        for (ClassStudentModel c : classStudentModel.getData()
                ) {
            classId.add(c.getClassId());
        }
        //查考试
        ResponseModel<List<TestModel>> testModels = homeWorkStaticsService.getHomeworkBySubjectNameAndHomeworkId(classId,studentTestModel.getSubjectName(),studentTestModel.getTime());
        if (!testModels.getCode().equals(ResultCode.SUCESS)) { throw new SchoolReportException(ResultCode.SELECT_NULL_MSG); }
        if (testModels.getData().size() == 0) { throw new SchoolReportException(ResultCode.SELECT_NULL_MSG); }
        List<StudentTestScoreModel> studentTestScoreModels = new ArrayList<StudentTestScoreModel>();
        StudentTestScoreModel studentTestScoreModel = null;
        for (int c = 0; c < classStudentModel.getData().size(); c++) {
            for (TestModel testModel : testModels.getData()) {
                if (classStudentModel.getData().get(c).getClassId().equals(testModel.getClassId())) {
                    int studentCount = classStudentModel.getData().get(c).getStudent().size();//班级学生总数
                    //ResponseModel<List<TestScoreModel>> tesScoreModel = testStaticsService.getTestByTestId(testModel.getId());
                    ResponseModel<TestScoreModel> tesScoreModel = homeWorkStaticsService.getHomeworkById(testModel.getId());
                    if (!tesScoreModel.getCode().equals(ResultCode.SUCESS)) {
                        throw new SchoolReportException(ResultCode.SELECT_NULL_MSG);
                    }
                    if (tesScoreModel.getData() == null) {
                        throw new SchoolReportException(ResultCode.SELECT_NULL_MSG);
                    }
                    studentTestScoreModel = new StudentTestScoreModel(classStudentModel.getData().get(c).getClassName(),
                            testModel.getSubjectName(), tesScoreModel.getData().getTestName(), tesScoreModel.getData().getAvg(),
                            tesScoreModel.getData().getMaxScore(), tesScoreModel.getData().getMinScore(),
                            tesScoreModel.getData().getActualNum(), studentCount - tesScoreModel.getData().getActualNum());
                    studentTestScoreModels.add(studentTestScoreModel);
                }
            }
        }
        return studentTestScoreModels;
    }


    @Override//每一次习题
    public HomworkReportRateModel getHomeReportKnowledgeRate(String homeworkId) throws SchoolReportException {
        ResponseModel<HomworkReportRateModel> homework = homeWorkStaticsService.getHomeworkByHomeworkId(homeworkId);
        if (homework.getData() == null) {
            throw new SchoolReportException(ResultCode.SELECT_NULL_MSG);
        }
        HomworkReportRateModel homworkReportRateModels = homework.getData();
        return homworkReportRateModels;
    }

    @Override//每一堂课堂
    public HomworkReportRateModel getCourseReportKnowledgeRate(String courseId) throws SchoolReportException {
        ResponseModel<HomworkReportRateModel> homework = courseFeignService.getCourseByCourseId(courseId);
        if (homework.getData() == null) {
            throw new SchoolReportException(ResultCode.SELECT_NULL_MSG);
        }
            HomworkReportRateModel homworkReportRateModels = homework.getData();
            return homworkReportRateModels;
        }

    @Override//每一次考试
    public HomworkReportRateModel getTestReportKnowledgeRate(String testId) throws SchoolReportException {
        ResponseModel<HomworkReportRateModel> homework = testStaticsService.getTestById(testId);
        if (homework.getData() == null) {
            throw new SchoolReportException(ResultCode.SELECT_NULL_MSG);
        }
        HomworkReportRateModel homworkReportRateModels = homework.getData();
        return homworkReportRateModels;
    }


}
