package com.jichuangsi.school.questionsrepository.importword.util;




import com.jichuangsi.school.questionsrepository.entity.SelfQuestions;
import com.jichuangsi.school.questionsrepository.importword.model.ImgModel;
import com.jichuangsi.school.questionsrepository.importword.model.MatcherIndex;
import com.jichuangsi.school.questionsrepository.model.self.SelfQuestion;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

import org.springframework.data.mongodb.core.mapping.TextScore;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Author LaiJX
 * @Date 11:36 2019/4/16
 * @Param
 * @What? 拆分word文档试卷
        * @return
        **/

public class ImportWord {

    public static List<SelfQuestion> breakUpWord(File word) throws IOException, TranscoderException {
        InputStream in = new FileInputStream(word);
        XWPFDocument xwpfDocument = new XWPFDocument(in);
        List<XWPFParagraph> paragraphList = xwpfDocument.getParagraphs();
        //切割文本
//        String text="";
//        for (int i = 0; i <paragraphList.size() ; i++) {
//            text+=" ";
//            text+=paragraphList.get(i).getParagraphText();
//        }
//        String[] haha=text.split("[1-9]\\d*\\.{1}\\s|\\s[1-9]\\d*\\.|\\s25．");
//
//        int n=1;
//        for (String s:haha
//        ) {
//            System.out.println(s);
//            System.out.println("==================="+n+++"===========");
//        }
        //修改完的文本
        List<String> base64TestList = new ArrayList<>();
        //作为正则的文本
        List<String> runTextList =new ArrayList<>();
        //遍历
        for (int i = 0; i < paragraphList.size(); i++) {
            //获取
            List<ImgModel> imgModelList = new ArrayList<>();
            List<String> imageBundleList = XWPFUtils.readImageInParagraph(paragraphList.get(i));
            if (CollectionUtils.isNotEmpty(imageBundleList)) {
//                String text2 = paragraphList.get(i).getParagraphText();

                //循环拿图片，文本
                for (String pictureId : imageBundleList) {
                    XWPFPictureData pictureData = xwpfDocument.getPictureDataByID(pictureId);
                    ImgModel imgModel = new ImgModel();
                    imgModel.setImageName(pictureData.getFileName());
                    imgModel.setParagraphText(paragraphList.get(i).getParagraphText());
                    imgModel.setRegexText(paragraphList.get(i).getParagraphText());
                    imgModel.setImgData(pictureData.getData());
                    imgModelList.add(imgModel);
//                    System.out.println(pictureId +"\t|" + imageName + "\t|" + lastParagraphText);
//                    System.out.println("---------------以下为图片数据-----------------");
//                    BASE64Encoder encoder = new BASE64Encoder();
//                    System.out.println(encoder.encode(pictureData.getData()));
//                    System.out.println(encoder.encode(PngUtils.convert2(pictureData.getData())));
//                    System.out.println("---------------------------------------"+(n++)+"---");
//                    System.out.println(Base64Util.encode(pictureData.getData()));
//                    if (imageName.split("\\.")[1].equalsIgnoreCase("wmf")){
//                            byte[] data = pictureData.getData();
////                        byte[] bytes = PngUtils.convert2(data);
////                        Bety2wenjian.getFile(bytes,"D:\\test",(n++)+".jpg");
//                            Bety2wenjian.getFile(data,"D:\\test",(n++)+".wmf");
//                    }else {
//                        Bety2wenjian.getFile(pictureData.getData(),"D:\\test",(n++)+".png");
//                    }
//                    for (byte b:pictureData.getData()
//                    ) {
//                        System.out.println(b);
//                    }
                }
                //加base64进所在位置并加入文本中
                //每次加进base64后的索引偏移量
                int base64length=0;
                //待完善的带图片文本
                StringBuilder addBase64Test=new StringBuilder(imgModelList.get(0).getParagraphText());
                //标记ABCD是否有图，0为无图，1为有图
                int a=0;
                int b=0;
                int c=0;
                int d=0;
                //关联题目属性
                for (ImgModel imgModel : imgModelList
                ) {
                    BASE64Encoder encoder = new BASE64Encoder();
                    String imageName = imgModel.getImageName();
                    String paragraphText = imgModel.getParagraphText();
                    String regexText = imgModel.getRegexText();
                    byte[] imgData = imgModel.getImgData();
                    //原文本备份
                    runTextList.add(regexText);
                    //判断图片后缀并转base64
                    String base64="";
                    if (imageName.split("\\.")[1].equalsIgnoreCase("wmf")) {
                        byte[] bytes = PngUtils.convert2(imgData);
                        base64 += "data:image/jpeg;base64,";
                        base64 += encoder.encode(bytes);
                        base64 += " ";
                    } else if (imageName.split("\\.")[1].equalsIgnoreCase("png")) {
                        base64 += "data:image/png;base64,";
                        base64 += encoder.encode(imgData);
                        base64 += " ";
                    } else if (imageName.split("\\.")[1].equalsIgnoreCase("jpg")) {
                        base64 += "data:image/jpeg;base64,";
                        base64 += encoder.encode(imgData);
                        base64 += " ";
                    }
                    if (Pattern.matches(".*[A-Z]\\.\\s.*|.*[A-Z]\\．\\s.*|.*\\sD\\．$|.*\\sD\\.$|.*\\sD\\．\\s.*|.*\\sD\\.\\s.*",paragraphText)) {
                        List<MatcherIndex> allIndex = StringUtils.findAllIndex(addBase64Test.toString(), "[A-Z]\\.\\s|[A-Z]\\．\\s|\\sD\\．$|\\sD\\.$|\\sD\\．\\s|\\sD\\.\\s");
                        if (Pattern.compile("A\\．\\s|A\\.\\s").matcher(addBase64Test.toString()).find()){
                            addBase64Test.insert(allIndex.get(0).getEnd()-1,base64);
                            base64length+=base64.length();
                        } else if (Pattern.compile("B\\．\\s|B\\.\\s").matcher(addBase64Test.toString()).find()){
                            addBase64Test.insert(allIndex.get(0).getEnd()-1,base64);
                            base64length+=base64.length();
                        } else if (Pattern.compile("C\\．\\s|C\\.\\s").matcher(addBase64Test.toString()).find()){
                            addBase64Test.insert(allIndex.get(0).getEnd()-1,base64);
                            base64length+=base64.length();
                        } else {
                            addBase64Test.append(base64);
                        }
                    }else {
                        addBase64Test.append(base64);
                    }
//                StringUtils.findAllIndex(paragraphText,"[A-Z]\\.\\s\\s|[A-Z]\\．\\s\\s|\\s\\sD\\．$|\\s\\sD\\.$|\\sD\\．\\s|\\sD\\.\\s")
//                String[] wakaka=regexText.split("[A-Z]\\.\\s\\s|[A-Z]\\．\\s\\s|\\s\\sD\\．$|\\s\\sD\\.$|\\sD\\．\\s|\\sD\\.\\s");
                }
                base64TestList.add(addBase64Test.toString());
            }
        }
        String text="";
        for (int i = 0; i <paragraphList.size() ; i++) {
            text+=" ";
            text+=paragraphList.get(i).getParagraphText();
        }
        for (int i = 0; i <base64TestList.size() ; i++) {
//            List<MatcherIndex> allindex = StringUtils.findAllIndex(text, runTextList.get(i).toString());
            text=text.replaceFirst(R2Text.r2Text(runTextList.get(i).toString()),base64TestList.get(i).toString());
//            text=text.replace(R2Text.r2Text(runTextList.get(i).toString()),base64TestList.get(i).toString());
        }
        String[] question=text.split("[1-9]\\d*\\.{1}\\s|\\s[1-9]\\d*\\.|\\s25．");

        //切选项装对象
        List<SelfQuestion> selfQuestionsList =new ArrayList<>();
        int n=1;
        for (String qt:question
        ) {
            String[] qtSplit = qt.split("[A-Z]\\.\\s|[A-Z]\\．\\s|\\sD\\．$|\\sD\\.$|\\sD\\．\\s|\\sD\\.\\s");
            SelfQuestions questions = new SelfQuestions();
            if (qtSplit.length==1){
                questions.setContent(qtSplit[0]);
            }else{
                List<String> options =new ArrayList<>();
                for (int i = 0; i <qtSplit.length ; i++) {
                    if (i==0){
                        questions.setContent(qtSplit[i]);
                    }else {
                        options.add(qtSplit[i]);
                    }
                }
                questions.setOptions(options);
            }
        }

        in.close();
        return selfQuestionsList;
    }
}