package com.jichuangsi.school.questionsrepository.importword.util;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import java.io.File;
import java.io.FileNotFoundException;

public class Office2HtmlUtil {

    public static final int WORD_HTML = 10;
    public static final int EXCEL_HTML = 44;
    public static final int HTML_WORD = 1;

    /**
     * 将word转化为html
     *
     * @param sourcePath
     *            源文件
     * @param targetPath
     *            转换后的html文件，传入的要是.html结尾的
     * @throws FileNotFoundException
     */
    public static void wordToHtml(String sourcePath, String targetPath) throws FileNotFoundException {
        // 判断文件是否存在
        File file = new File(sourcePath);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        // 启动word应用程序(Microsoft Office Word 2003)
        ActiveXComponent app = null;
        System.out.println("*******************word转换html开始**********************");
        try {
            app = new ActiveXComponent("Word.Application");
            // 设置word应用程序不可见
            app.setProperty("Visible", new Variant(false));
            // documents表示word程序的所有文档窗口，（word是多文档应用程序）
            Dispatch docs = app.getProperty("Documents").toDispatch();
            // 打开要转换的word文件
            Dispatch doc = Dispatch
                    .invoke(docs, "Open", Dispatch.Method,
                            new Object[] { sourcePath, new Variant(false), new Variant(true) }, new int[1])
                    .toDispatch();
            // 作为html格式保存到临时文件
            Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] { targetPath, new Variant(WORD_HTML) },
                    new int[1]);
            // 关闭word文件
            Dispatch.call(doc, "Close", new Variant(false));
            System.out.println("*******************转换html结束**********************");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("wordToHtml is err");
        } finally {
            // 关闭word应用程序
            app.invoke("Quit", new Variant[] {});
        }
    }

    /**
     * 将excel转化为html
     *
     * @param sourcePath
     *            源文件
     * @param targetPath
     *            转换后的html文件，传入的要是.html结尾的
     * @throws FileNotFoundException
     */
    public static void excelToHtml(String sourcePath, String targetPath) throws FileNotFoundException {

        // 判断文件是否存在
        File file = new File(sourcePath);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        // 启动word应用程序(Microsoft Office Word 2003)
        ComThread.InitSTA();
        ActiveXComponent app = null;
        System.out.println("*******************excel转换html开始**********************");
        try {
            app = new ActiveXComponent("Excel.Application");
            app.setProperty("Visible", new Variant(false));
            Dispatch excels = app.getProperty("Workbooks").toDispatch();
            Dispatch excel = Dispatch
                    .invoke(excels, "Open", Dispatch.Method,
                            new Object[] { sourcePath, new Variant(false), new Variant(true) }, new int[1])
                    .toDispatch();
            Dispatch.invoke(excel, "SaveAs", Dispatch.Method, new Object[] { targetPath, new Variant(EXCEL_HTML) },
                    new int[1]);
            Dispatch.call(excel, "Close", new Variant(false));
            System.out.println("*******************excel转换html结束**********************");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("excelToHtml is err");
        } finally {
            ComThread.Release();
        }
    }

    public static void pptToHtml() {

    }

    public static void main(String[] args) throws FileNotFoundException {

            final String sourcePath = "E:/JCS/2222222222222222222.doc";
            final String targetPath = "E:/JCS/22222222222222222.html";
        wordToHtml(sourcePath, targetPath);


    }
}
