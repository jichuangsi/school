package com.jichuangsi.school.courseservice.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.Exception.StudentCourseServiceException;
import com.jichuangsi.school.courseservice.constant.ResultCode;
import com.jichuangsi.school.courseservice.model.*;
import com.jichuangsi.school.courseservice.model.repository.QuestionQueryModel;
import com.jichuangsi.school.courseservice.service.IStudentCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/student")
@Api("StudentCourseController相关的api")
public class StudentCourseController {

	@Resource
	private IStudentCourseService studentCourseService;

	//获取学生课程列表
	@ApiOperation(value = "根据班级id获取学生课堂列表信息", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
	@GetMapping("/getList")
	public ResponseModel<List<CourseForStudent>> getList(@ModelAttribute UserInfoForToken userInfo) throws StudentCourseServiceException {

		return ResponseModel.sucess("", studentCourseService.getCoursesList(userInfo));
	}

	//获取学生历史课程列表
	@ApiOperation(value = "根据班级id获取历史学生课堂列表信息", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
	@PostMapping("/getHistory")
	public ResponseModel<PageHolder<CourseForStudent>> getHistory(@ModelAttribute UserInfoForToken userInfo, @RequestBody CourseForStudent pageInform) throws StudentCourseServiceException {

		return ResponseModel.sucess("", studentCourseService.getHistoryCoursesList(userInfo, pageInform));
	}

	//获取指定课堂基本信息
	@ApiOperation(value = "根据课堂id查询课堂信息", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "path", name = "courseId", value = "课堂ID", required = true, dataType = "String")})
	@GetMapping("/getCourse/{courseId}")
	public ResponseModel<CourseForStudent> getCourse(@ModelAttribute UserInfoForToken userInfo, @PathVariable String courseId) throws StudentCourseServiceException {
		;
		return ResponseModel.sucess("", studentCourseService.getParticularCourse(userInfo, courseId));
	}

	//课堂主观题图片存根
	@ApiOperation(value = "根据学生id保存上传的文件", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
	@PostMapping("/sendSubjectPicByFile")
	public ResponseModel<AnswerForStudent> sendSubjectPicByFile(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo) throws StudentCourseServiceException {
		try {
			return ResponseModel.sucess("", studentCourseService.uploadStudentSubjectPic(userInfo, new CourseFile(file.getOriginalFilename(), file.getContentType(), file.getBytes())));
		} catch (IOException ioExp) {
			throw new StudentCourseServiceException(ResultCode.FILE_UPLOAD_ERROR);
		}
	}

	@ApiOperation(value = "根据学生id保存上传的字符串", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
	@PostMapping("/sendSubjectPicByString")
	public ResponseModel<AnswerForStudent> sendSubjectPicByString(@ModelAttribute UserInfoForToken userInfo, @RequestBody Base64TransferFile file) throws StudentCourseServiceException {
		return ResponseModel.sucess("", studentCourseService.uploadStudentSubjectPic(userInfo, new CourseFile(file.getName(), file.getContentType(), file.getContent().getBytes())));
	}

	//获取指定文件名手寫板輸入图片
	@ApiOperation(value = "根据学生id和文件名下载指定的手寫板輸入图片", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
	@PostMapping("/getSubjectPic")
	public ResponseModel<Base64TransferFile> getSubjectPic(@ModelAttribute UserInfoForToken userInfo, @RequestBody AnswerForStudent answer) throws StudentCourseServiceException {
		Base64TransferFile base64TransferFile = new Base64TransferFile();
		CourseFile courseFile = studentCourseService.downloadStudentSubjectPic(userInfo, answer.getStubForSubjective());
		base64TransferFile.setName(courseFile.getName());
		base64TransferFile.setContentType(courseFile.getContentType());
		base64TransferFile.setContent(new String(courseFile.getContent()));
		return ResponseModel.sucess("", base64TransferFile);
	}

	//获取指定文件名图片
	@ApiOperation(value = "根据学生id和文件名下载指定的上傳图片", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
	@PostMapping("/getQuestionPic")
	public ResponseModel<CourseFile> getQuestionPic(@ModelAttribute UserInfoForToken userInfo, @RequestBody AnswerForStudent questionPic) throws StudentCourseServiceException {
		return ResponseModel.sucess("", studentCourseService.downloadStudentSubjectPic(userInfo, questionPic.getStubForSubjective()));
	}

	//删除指定文件名图片
	@ApiOperation(value = "根据学生id和文件名删除指定文件", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
	@DeleteMapping("/remoreSubjectPic")
	public ResponseModel<CourseFile> remoreSubjectPic(@ModelAttribute UserInfoForToken userInfo, @RequestBody AnswerForStudent answer) throws StudentCourseServiceException {
		studentCourseService.deleteStudentSubjectPic(userInfo, answer.getStubForSubjective());
		return ResponseModel.sucessWithEmptyData("");
	}

	//发送学生答案
	@ApiOperation(value = "根据学生id和问题id保存学生的答案", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String")})
	@PostMapping("/sendAnswer/{courseId}/{questionId}")
	public ResponseModel<AnswerForStudent> sendAnswer(@ModelAttribute UserInfoForToken userInfo, @PathVariable String courseId,
													  @PathVariable String questionId,
													  @RequestBody AnswerForStudent answer) throws StudentCourseServiceException {
		studentCourseService.saveStudentAnswer(userInfo, courseId, questionId, answer);
		return ResponseModel.sucessWithEmptyData("");
	}

	//获取指定课堂题目
	@ApiOperation(value = "根据问题id查询问题信息", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String")})
	@GetMapping("/getQuestion/{questionId}")
	public ResponseModel<QuestionForStudent> getQuestion(@ModelAttribute UserInfoForToken userInfo, @PathVariable String questionId) throws StudentCourseServiceException {

		return ResponseModel.sucess("", studentCourseService.getParticularQuestion(userInfo, questionId));
	}

	@ApiOperation(value = "根据问题id增加学生收藏题目", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String")})
	@PutMapping("/addFavorQuestion/{questionId}")
	public ResponseModel addFavorQuestion(@ModelAttribute UserInfoForToken userInfo, @PathVariable String questionId) throws StudentCourseServiceException {
		studentCourseService.addParticularQuestionInFavor(userInfo, questionId);
		return ResponseModel.sucessWithEmptyData("");
	}

	@ApiOperation(value = "根据问题id删除学生收藏题目", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String")})
	@DeleteMapping("/removeFavorQuestion/{questionId}")
	public ResponseModel removeFavorQuestion(@ModelAttribute UserInfoForToken userInfo, @PathVariable String questionId) throws StudentCourseServiceException {
		studentCourseService.removeParticularQuestionInFavor(userInfo, questionId);
		return ResponseModel.sucessWithEmptyData("");
	}

	@ApiOperation(value = "根据问题id列出学生收藏题目", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String")})
	@GetMapping("/listFavorQuestions")
	public ResponseModel<List<QuestionForStudent>> listFavorQuestions(@ModelAttribute UserInfoForToken userInfo) throws StudentCourseServiceException {

		return ResponseModel.sucess("", studentCourseService.getFavorQuestionsList(userInfo));
	}

	@ApiOperation(value = "根据问题id列出学生错误题目", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String")})
	@GetMapping("/collectIncorrectQuestions")
	public ResponseModel<List<QuestionForStudent>> collectIncorrectQuestions(@ModelAttribute UserInfoForToken userInfo) throws StudentCourseServiceException {

		return ResponseModel.sucess("", studentCourseService.getIncorrectQuestionList(userInfo));
	}

	@PostMapping("/aiPushQuestions")
	public ResponseModel<List<QuestionForStudent>> aiPushQuestions(@ModelAttribute UserInfoForToken userInfo, @RequestBody QuestionQueryModel questionQueryModel)throws StudentCourseServiceException {

		return ResponseModel.sucess("", studentCourseService.findSimilarQuestionsList(userInfo, questionQueryModel));
	}
}

