package com.jichuangsi.school.questionsrepository.importword.test;

import com.jichuangsi.school.questionsrepository.importword.util.ImportWord;
import com.jichuangsi.school.questionsrepository.importword.util.XWPFUtils2;
import org.apache.batik.transcoder.TranscoderException;

import java.io.IOException;


/**
 * Created by 窝里横 on 2019/4/15.
 */
public class testont {
    public static void main(String[] args) throws IOException, TranscoderException {
//        XWPFUtils2.readImageInParagraph();

//        ImportWord.readImageInParagraph();

        String[] b = ("A．data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDABALDA4MChAODQ4SERATGCgaGBYWGDEjJR0oOjM9PDkz\n" +
                "ODdASFxOQERXRTc4UG1RV19iZ2hnPk1xeXBkeFxlZ2P/2wBDARESEhgVGC8aGi9jQjhCY2NjY2Nj\n" +
                "Y2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2P/wAARCAApAA8DASIA\n" +
                "AhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA\n" +
                "AAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3\n" +
                "ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm\n" +
                "p6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEA\n" +
                "AwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx\n" +
                "BhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK\n" +
                "U1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3\n" +
                "uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDttRuz\n" +
                "Y2hnEXmYdVI3Y6sBn9atVQ1qKafTXjgiaWQuh2ggcBwT1I7Crw5HTFAEMr3QfEMMLp6vKVP5bTTo\n" +
                "WmYHz440PbY5b+YFNuoppowkNw1vzy6KpbHoNwI/SoNGluZtOR7txJJucCQDHmKGIVsdsjB/GgB+\n" +
                "oQXNzCI7aeOHJ+fzIi4YenDLUlpHPHDtuZklkz1jj2KB6AZP86nooA//2Q==                B．﹣2             C．±2              D．2"
        ).split("[A-Z]\\.|[A-Z]\\．|\\sD\\．$|\\sD\\.$|\\sD\\．");
        for (String bb:b
             ) {
            System.out.println(bb);
            System.out.println("========================================================");
        }
    }
}
