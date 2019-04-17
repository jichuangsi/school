package com.jichuangsi.school.questionsrepository.importword.util;
import com.microsoft.schemas.vml.CTShape;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;

import java.util.ArrayList;
import java.util.List;


public class XWPFUtils {

    //获取某一个段落中的所有图片索引
    public static List<String> readImageInParagraph(XWPFParagraph paragraph) {
        //图片索引List
        List<String> imageBundleList = new ArrayList<String>();

        //段落中所有XWPFRun
        List<XWPFRun> runList = paragraph.getRuns();
        for (XWPFRun run : runList) {
            //XWPFRun是POI对xml元素解析后生成的自己的属性，无法通过xml解析，需要先转化成CTR
            CTR ctr = run.getCTR();

            //对子元素进行遍历
            XmlCursor c = ctr.newCursor();

            //这个就是拿到所有的子元素：
            c.selectPath("./*");

            while (c.toNextSelection()) {
                int n=0;

                XmlObject o = c.getObject();
                //如果子元素是<w:drawing>这样的形式，使用CTDrawing保存图片
                if (o instanceof CTDrawing) {
//                    XmlCursor t=c;
//                    t.removeXml();
//                    t.getTextValue();


                    CTDrawing drawing = (CTDrawing) o;
                    CTInline[] ctInlines = drawing.getInlineArray();
                    for (CTInline ctInline : ctInlines) {
                        CTGraphicalObject graphic = ctInline.getGraphic();
                        //
                        XmlCursor cursor = graphic.getGraphicData().newCursor();
                        cursor.selectPath("./*");
                        while (cursor.toNextSelection()) {
                            XmlObject xmlObject = cursor.getObject();
// 如果子元素是<pic:pic>这样的形式
                            if (xmlObject instanceof CTPicture) {
                                CTPicture picture = (CTPicture) xmlObject;
                                //拿到元素的属性
                                imageBundleList.add(picture.getBlipFill().getBlip().getEmbed());
                            }
                        }
                    }

                }
                //使用CTObject保存图片
//<w:object>形式
                if (o instanceof CTObject) {
                    CTObject object = (CTObject) o;
                    System.out.println(object);
                    XmlCursor w = object.newCursor();
                    w.selectPath("./*");
                    while (w.toNextSelection()) {
                        XmlObject xmlObject = w.getObject();
                        if (xmlObject instanceof CTShape) {
                            CTShape shape = (CTShape) xmlObject;
                            imageBundleList.add(shape.getImagedataArray()[0].getId2());
                        }
                    }
                }
            }
        }
        return imageBundleList;
    }

}