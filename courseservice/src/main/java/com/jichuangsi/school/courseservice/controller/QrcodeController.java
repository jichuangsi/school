package com.jichuangsi.school.courseservice.controller;

import com.google.zxing.Result;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.util.QRCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 二维码调用前端控制器
 */

@RestController
@RequestMapping("/code")
@Api("QrcodeController相关的api")
public class QrcodeController {
    @Value("${custom.login.content}")
    private String content;
    @Value("${custom.login.increaseTime}")
    private int increaseTime;
    @Value("${custom.login.QRsize}")
    private int QRsize;
    @Value("${custom.login.logoPath}")
    private String logoPath;
    /**
     * 生成二维码
     */
    @ApiOperation(value = "生成二维码", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @CrossOrigin
    @PostMapping("/createQR")
    public String productcode(@ModelAttribute UserInfoForToken userInfo,@RequestParam(value = "code") String c) throws IOException {
        Date date=new Date();
 /*       Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,increaseTime);
        long time1 = calendar.getTime().getTime();*/
        long time = date.getTime()+increaseTime;
        String s = String.valueOf(time);//?a="123"&code=
        String url=c+"&id="+userInfo.getUserId()+"&t="+s;
        BufferedImage bufferedImage = QRCodeUtil.zxingCodeCreate(content+url,null,QRsize,null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
        ImageIO.write(bufferedImage, "jpg", baos);//写入流中
        byte[] bytes = baos.toByteArray();//转换成字节
        BASE64Encoder encoder = new BASE64Encoder();
        String png_base64 =  encoder.encodeBuffer(bytes).trim();//转换成base64串
       // png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
        return png_base64;
    }
    /**
     * 生成二维码
     */
    @ApiOperation(value = "生成带公司logo的二维码", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @CrossOrigin
    @PostMapping("/createLogoQR")
    public String productcodewithLogo(@ModelAttribute UserInfoForToken userInfo,@RequestParam(value = "code") String c) throws IOException {
        Date date=new Date();
        long time = date.getTime()+increaseTime;
        String s = String.valueOf(time);//?a="123"&code=
        String url=c+"&id="+userInfo.getUserId()+"&t="+s;
        BufferedImage bufferedImage = QRCodeUtil.zxingCodeCreate(content+url,null,QRsize,logoPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
        ImageIO.write(bufferedImage, "jpg", baos);//写入流中
        byte[] bytes = baos.toByteArray();//转换成字节
        BASE64Encoder encoder = new BASE64Encoder();
        String png_base64 =  encoder.encodeBuffer(bytes).trim();//转换成base64串
       // png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
        return png_base64;
    }
    /**
     * 解析二维码
     */
//    @GetMapping("/test")
//    public void analysiscode() {
//        Result result = QRCodeUtil.zxingCodeAnalyze("D:\\voice\\picture\\2018\\111.jpg");
//        System.err.println("二维码解析内容："+result.toString());
//    }



}

