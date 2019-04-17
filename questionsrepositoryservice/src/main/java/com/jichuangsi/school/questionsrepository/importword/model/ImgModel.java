package com.jichuangsi.school.questionsrepository.importword.model;

/**
 * Created by 窝里横 on 2019/4/11.
 */
public class ImgModel {
    private String imageName;
    private String paragraphText;
    private String regexText;
    private byte[] imgData;

    public byte[] getImgData() {
        return imgData;
    }

    public void setImgData(byte[] imgData) {
        this.imgData = imgData;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getParagraphText() {
        return paragraphText;
    }

    public void setParagraphText(String paragraphText) {
        this.paragraphText = paragraphText;
    }

    public String getRegexText() {
        return regexText;
    }

    public void setRegexText(String regexText) {
        this.regexText = regexText;
    }
}
