package com.jichuangsi.school.user.controller;

import com.github.pagehelper.PageInfo;
import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.exception.SchoolServiceException;
import com.jichuangsi.school.user.model.org.ClassModel;
import com.jichuangsi.school.user.model.school.GradeModel;
import com.jichuangsi.school.user.model.school.PhraseModel;
import com.jichuangsi.school.user.model.school.SchoolModel;
import com.jichuangsi.school.user.model.school.SubjectModel;
import com.jichuangsi.school.user.service.ISchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/school")
@Api("关于学校和年级的contoller")
@CrossOrigin
public class SchoolController {

    @Resource
    private ISchoolService schoolService;

    @ApiOperation(value = "学校的增加", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/insertSchool")
    public ResponseModel insertSchool(@ModelAttribute UserInfoForToken userInfo, @RequestBody SchoolModel model){
        try {
            schoolService.insertSchool(userInfo,model);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "学校的修改", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/updateSchool")
    public ResponseModel updateSchool(@ModelAttribute UserInfoForToken userInfo,@RequestBody SchoolModel model){
        try {
            schoolService.updateSchool(userInfo, model);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "年级的添加", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/grade/insertGrade")
    public ResponseModel insertGrade(@ModelAttribute UserInfoForToken userInfo, @RequestBody GradeModel model){
        try {
            schoolService.insertGrade(userInfo, model);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "年级的修改", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/grade/updateGrade")
    public ResponseModel updateGrade(@ModelAttribute UserInfoForToken userInfo,@RequestBody GradeModel model){
        try {
            schoolService.updateGrade(userInfo, model);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "获取系统内所有非删除的学校", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getSchools")
    public ResponseModel<List<SchoolModel>> getSchools(@ModelAttribute UserInfoForToken userInfo){
        try {
            return ResponseModel.sucess("",schoolService.getSchools());
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "获取学校内所有非删除的年级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getGrades")
    public ResponseModel<List<GradeModel>> getGrades(@ModelAttribute UserInfoForToken userInfo,@RequestParam("schoolId") String schoolId){
        try {
            return ResponseModel.sucess("",schoolService.getGrades(schoolId));
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "删除学校", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/deleteSchool/{schoolId}")
    public ResponseModel deleteSchool(@ModelAttribute UserInfoForToken userInfo,@PathVariable String schoolId){
        try {
            schoolService.deleteSchool(userInfo, schoolId);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "删除年级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/deleteGrade/{gradeId}")
    public ResponseModel deleteGrade(@ModelAttribute UserInfoForToken userInfo,@PathVariable String gradeId){
        try {
            schoolService.deleteGrade(userInfo, gradeId);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "添加学科", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/subject/insertSubject")
    public ResponseModel insertSubject(@ModelAttribute UserInfoForToken userInfo, @RequestBody SubjectModel model){
        try {
            schoolService.insertSubject(userInfo, model);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "获取学科", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/subject/getSubjects")
    public ResponseModel<List<SubjectModel>> getSubjects(@ModelAttribute UserInfoForToken userInfo){
        return ResponseModel.sucess("",schoolService.getSubjects(userInfo));
    }

    @ApiOperation(value = "添加年段", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/phrase/insertPhrase")
    public ResponseModel insertPhrase(@ModelAttribute UserInfoForToken userInfo, @RequestBody PhraseModel model){
        try {
            schoolService.insertPhrase(userInfo,model);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据学校，查询年段", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/phrase/getPhraseBySchool")
    public ResponseModel<List<PhraseModel>>  getPhraseBySchool(@ModelAttribute UserInfoForToken userInfo,@RequestParam("schoolId") String school){
        try {
            return ResponseModel.sucess("",schoolService.getPhraseBySchool(school));
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据年段，查询年级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/grade/getGradeByPhrase")
    public ResponseModel<List<GradeModel>> getGradeByPhrase(@ModelAttribute UserInfoForToken userInfo,@RequestParam("phraseId") String phraseId){
        try {
            return ResponseModel.sucess("",schoolService.getGradeByPhrase(phraseId));
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "删除科目", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/deleteSubject/{subjectId}")
    public ResponseModel deleteSubject(@ModelAttribute UserInfoForToken userInfo,@PathVariable String subjectId){
        try {
            schoolService.deleteSubject(userInfo, subjectId);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "删除年段", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/deletePhrase/{phraseId}")
    public ResponseModel deletePhrase(@ModelAttribute UserInfoForToken userInfo,@PathVariable String phraseId){
        try {
            schoolService.deletePhrase(userInfo, phraseId);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "学科的修改", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/subject/updateSubject")
    public ResponseModel updateSubject(@ModelAttribute UserInfoForToken userInfo , @RequestBody SubjectModel model){
        try {
            schoolService.updateSubject(userInfo, model);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "学段的修改", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/phrase/updatePhrase")
    public ResponseModel updatePhrase(@ModelAttribute UserInfoForToken userInfo,@RequestBody PhraseModel model){
        try {
            schoolService.updatePhrase(userInfo, model);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据学校id获取学校信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getSchoolById/{schoolId}")
    public ResponseModel<SchoolModel> getSchoolById(@ModelAttribute UserInfoForToken userInfo,@PathVariable String schoolId){
        try {
            return ResponseModel.sucess("",schoolService.getSchoolById(userInfo, schoolId));
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "查询往届年级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getPastGrades/{graduationTime}/{pharseId}")
    public ResponseModel<PageInfo<GradeModel>> getPastGrades(@ModelAttribute UserInfoForToken userInfo, @PathVariable String graduationTime, @PathVariable String pharseId,
                                                             @RequestParam("pageIndex") int pageIndex,@RequestParam("pageSize") int pageSize){
        try {
            return ResponseModel.sucess("",schoolService.findPastGrades(userInfo, pharseId, graduationTime, pageIndex, pageSize));
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "查询往届班级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getPastClass/{graduationTime}/{gradeId}")
    public ResponseModel<PageInfo<ClassModel>> getPastClass(@ModelAttribute UserInfoForToken userInfo, @PathVariable String graduationTime, @PathVariable String gradeId,
                                                             @RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize){
        try {
            return ResponseModel.sucess("",schoolService.findPastClass(userInfo, gradeId, graduationTime, pageIndex, pageSize));
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "冻结往届班级的全部学生", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/coldStudentInGraduation/{classId}/{schoolId}")
    public ResponseModel coldStudentInGraduation(@ModelAttribute UserInfoForToken userInfo,@PathVariable String classId , @PathVariable String schoolId){
        try {
            schoolService.coldStudentInGraduation(userInfo, classId, schoolId);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "年级毕业", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/graduationGrade/{gradeId}")
    public ResponseModel graduationGrade(@ModelAttribute UserInfoForToken userInfo,@PathVariable String gradeId){
        try {
            schoolService.graduationGrade(userInfo, gradeId);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }
}
