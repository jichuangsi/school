package com.jichuangsi.school.parents.service.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.parents.commons.ResultCode;
import com.jichuangsi.school.parents.exception.ParentsException;
import com.jichuangsi.school.parents.feign.ICourseFeignService;
import com.jichuangsi.school.parents.feign.ICourseStatisticsFeignService;
import com.jichuangsi.school.parents.feign.ITestFeignService;
import com.jichuangsi.school.parents.feign.IUserFeignService;
import com.jichuangsi.school.parents.feign.model.*;
import com.jichuangsi.school.parents.model.common.PageHolder;
import com.jichuangsi.school.parents.service.ISchoolPerformanceService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SchoolPerformanceServiceImpl implements ISchoolPerformanceService{
    @Resource
    private ICourseFeignService courseFeignService;
    @Resource
    private ITestFeignService testFeignService;
    @Resource
    private IUserFeignService userFeignService;
    @Resource
    private ICourseStatisticsFeignService courseStatisticsFeignService;
    @Override
    public ResponseModel<PageHolder<CourseForStudent>> getHistory(CourseForStudentId pageInform) throws ParentsException {
        if (StringUtils.isEmpty(pageInform.getStudentId())){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ResponseModel<PageHolder<CourseForStudent>> responseModel = courseFeignService.getHistory(pageInform);
        if (!ResultCode.SUCESS.equals(responseModel.getCode())){
            throw new ParentsException(responseModel.getMsg());
        }
        return responseModel;
    }
    @Override
    public ResponseModel<PageHolder<CourseForStudent>> getHistoryTime(CourseForStudentIdTime pageInform) throws ParentsException {
        if (StringUtils.isEmpty(pageInform.getStudentId())){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ResponseModel<PageHolder<CourseForStudent>> responseModel = courseFeignService.getHistoryTime(pageInform);
        if (!ResultCode.SUCESS.equals(responseModel.getCode())){
            throw new ParentsException(responseModel.getMsg());
        }
        return responseModel;
    }
    @Override
    public ResponseModel<PageHolder<TestModelForStudent>> getHistory(SearchTestModelId searchTestModel) throws ParentsException {
        if (StringUtils.isEmpty(searchTestModel.getStudentId())){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ResponseModel<PageHolder<TestModelForStudent>> responseModel = testFeignService.getHistory(searchTestModel);
        if (!ResultCode.SUCESS.equals(responseModel.getCode())){
            throw new ParentsException(responseModel.getMsg());
        }
        return responseModel;
    }

    @Override
    public ResponseModel<List<CourseForStudent>> getCourseCommend(String studentId,String pageSize,String pageNum) throws ParentsException {
        ResponseModel<String> studentClass = userFeignService.findStudentClass(studentId);
        String classId = studentClass.getData();
        CourseForStudentId courseForStudentId = new CourseForStudentId();
        courseForStudentId.setStudentId(studentId);
        courseForStudentId.setPageSize(9999);
        courseForStudentId.setPageNum(1);
        ResponseModel<PageHolder<CourseForStudent>> pageHolderResponseModel = getHistory(courseForStudentId);
        List<CourseForStudent> courseForStudents = pageHolderResponseModel.getData().getContent();
        List<CourseForStudent> coursesCommend = new ArrayList<>();
        for (CourseForStudent xxx:courseForStudents
             ) {
            ResponseModel<List<TransferStudent>> courseSignFeign = courseStatisticsFeignService.getCourseSignFeign(xxx.getCourseId(), classId);
            for (TransferStudent student:courseSignFeign.getData()
                 ) {
                if (student.getStudentId()==studentId&&student.getCommendFlag()==1){
                    coursesCommend.add(xxx);
                }
            }
        }
        int a = Integer.parseInt(pageNum);
        int b =Integer.parseInt(pageSize);
        List<CourseForStudent> commend = new ArrayList<>();
        if (a*b<=coursesCommend.size()){
            for (int i = 1; i <=b ; i++) {
                commend.add(coursesCommend.get(a*b-b+i-1));
            }
        }else if (coursesCommend.size()==0){
            return null;
        } else {
            for (int i = 1; i <=coursesCommend.size()-(a-1)*b ; i++) {
                commend.add(coursesCommend.get((a-1)*b+i));
            }
        }
        ResponseModel<List<CourseForStudent>> model = new ResponseModel<>();
        model.setData(commend);
        return model;
    }

    @Override
    public ResponseModel<List<CourseForStudent>> getCourseCommendTime(String studentId, List<Long> statisticsTimes) throws ParentsException {
        ResponseModel<String> studentClass = userFeignService.findStudentClass(studentId);
        String classId = studentClass.getData();

        CourseForStudentIdTime courseForStudentId = new CourseForStudentIdTime();
        courseForStudentId.setStudentId(studentId);
        courseForStudentId.setStatisticsTimes(statisticsTimes);//时间
        ResponseModel<PageHolder<CourseForStudent>> pageHolderResponseModel = getHistoryTime(courseForStudentId);
        List<CourseForStudent> courseForStudents = pageHolderResponseModel.getData().getContent();

        List<CourseForStudent> coursesCommend = new ArrayList<>();
        for (CourseForStudent xxx:courseForStudents
                ) {
            ResponseModel<List<TransferStudent>> courseSignFeign = courseStatisticsFeignService.getCourseSignFeign(xxx.getCourseId(), classId);

            for (TransferStudent student:courseSignFeign.getData()) {

                if (student.getStudentId()!=null && student.getStudentId().equals(studentId) && student.getCommendFlag()==1){
                    coursesCommend.add(xxx);
                }

            }
        }

        ResponseModel<List<CourseForStudent>> model = new ResponseModel<>();
        model.setData(coursesCommend);
        return model;
    }

}
