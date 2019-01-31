package com.jichuangsi.school.homeworkservice.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.homeworkservice.constant.ResultCode;
import com.jichuangsi.school.homeworkservice.exception.FileServiceException;
import com.jichuangsi.school.homeworkservice.model.Attachment;
import com.jichuangsi.school.homeworkservice.model.common.Base64TransferFile;
import com.jichuangsi.school.homeworkservice.model.common.DeleteQueryModel;
import com.jichuangsi.school.homeworkservice.model.common.HomeworkFile;
import com.jichuangsi.school.homeworkservice.service.IFileTransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RequestMapping("/file")
@RestController
@Api("FileController测试开发相关的api")
public class FileController {

    @Resource
    private IFileTransferService fileService;

    @ApiOperation(value = "上传homework的附件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveAttachment")
    public ResponseModel<Attachment> saveAttachment(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo) throws FileServiceException{
        try {
            return ResponseModel.sucess("", fileService.uploadAttachment(userInfo, new HomeworkFile(file.getOriginalFilename(), file.getContentType(), file.getBytes())));
        } catch (FileServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        } catch (IOException e) {
            return ResponseModel.fail("", ResultCode.FILE_UPLOAD_ERROR);
        }
    }

    @ApiOperation(value = "删除homework的附件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping("/removeAttachment")
    public ResponseModel removeAttachment(@ModelAttribute UserInfoForToken userInfo, @RequestBody DeleteQueryModel deleteQueryModel) throws FileServiceException{
        try {
            fileService.removeFile(userInfo, deleteQueryModel);
            return ResponseModel.sucessWithEmptyData("");
        } catch (FileServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "根据老师id和文件名下载指定的上傳附件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getAttachment")
    public void getAttachment(@ModelAttribute UserInfoForToken userInfo, @RequestBody Attachment attachment, HttpServletResponse resp) throws FileServiceException{
        try {
            HomeworkFile file = fileService.downloadFile(userInfo, attachment.getSub());
            resp.setHeader("content-type", "application/octet-stream");
            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(file.getContent());
            resp.setHeader("content-length", byteArrayOutputStream.size() + "");
            resp.getOutputStream().write(byteArrayOutputStream.toByteArray());
        }catch (Exception e) {
            throw new FileServiceException(e.getMessage());
        }
    }

    @ApiOperation(value = "上传文件内容为字节流", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/sendByFile")
    public ResponseModel<String> sendByFile(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo) throws FileServiceException {
        try {
            return ResponseModel.sucess("", fileService.uploadPic(userInfo, new HomeworkFile(file.getOriginalFilename(), file.getContentType(), file.getBytes())));
        } catch (IOException ioExp) {
            throw new FileServiceException(ResultCode.FILE_UPLOAD_ERROR);
        }
    }

    @ApiOperation(value = "上传文件内容为字符串", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/sendByString")
    public ResponseModel<String> sendByString(@ModelAttribute UserInfoForToken userInfo, @RequestBody Base64TransferFile file) throws FileServiceException {
        return ResponseModel.sucess("", fileService.uploadPic(userInfo, new HomeworkFile(file.getName(), file.getContentType(), file.getContent().getBytes())));
    }

    @ApiOperation(value = "根据存根获取文件内容为字符串", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getByString")
    public ResponseModel<Base64TransferFile> getByString(@ModelAttribute UserInfoForToken userInfo, @RequestBody String sub) throws FileServiceException {
        Base64TransferFile base64TransferFile = new Base64TransferFile();
        HomeworkFile courseFile = fileService.downloadFile(userInfo, sub);
        base64TransferFile.setName(courseFile.getName());
        base64TransferFile.setContentType(courseFile.getContentType());
        base64TransferFile.setContent(new String(courseFile.getContent()));
        return ResponseModel.sucess("", base64TransferFile);
    }

    //获取指定文件名图片
    @ApiOperation(value = "根据存根获取文件内容为字节流", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getByFile")
    public ResponseModel<HomeworkFile> getByFile(@ModelAttribute UserInfoForToken userInfo, @RequestBody String sub) throws FileServiceException {
        return ResponseModel.sucess("", fileService.downloadFile(userInfo, sub));
    }

    //删除指定文件名图片
    @ApiOperation(value = "根据存根删除文件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @DeleteMapping("/remove")
    public ResponseModel removeFile(@ModelAttribute UserInfoForToken userInfo, @RequestBody DeleteQueryModel deleteQueryModel) throws FileServiceException {
        fileService.removeFile(userInfo, deleteQueryModel);
        return ResponseModel.sucessWithEmptyData("");
    }
}
