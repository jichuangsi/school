package com.jichuangsi.school.questionsrepository.importword.util;

import com.jichuangsi.school.questionsrepository.importword.model.MatcherIndex;
import com.jichuangsi.school.questionsrepository.model.self.SelfQuestion;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HtmlRegex {
    public static void main(String[] args) throws IOException {

    }

    public static List<SelfQuestion> regexKillHtml(String filePath,String folderWebPath) throws IOException {

        File f = new File(filePath);

        String fileContent = "";
        InputStreamReader read = new InputStreamReader(
                new FileInputStream(f), "GBK");
        BufferedReader reader = new BufferedReader(read);
        String line;
        while ((line = reader.readLine()) != null) {
            fileContent += (line+" ");
        }
        read.close();

        fileContent=fileContent.replaceAll("</span>","");
        fileContent=fileContent.replaceAll("</body>","");
        fileContent=fileContent.replaceAll("</html>","");
        fileContent=fileContent.replaceAll("</div>","");
        fileContent=fileContent.replaceAll("</p>","");
        fileContent=fileContent.replaceAll("</a>","");
        fileContent=fileContent.replaceAll("(<span|<p|<a).*?>","");
        fileContent=fileContent.replaceAll("&nbsp;"," ");

        //给img插路径
        StringBuilder stringBuilder = new StringBuilder(fileContent);
        List<MatcherIndex> allIndex = StringUtils.findAllIndex(fileContent, "src=\"");
        int n=0;
        for (MatcherIndex m:allIndex
                ) {
            stringBuilder.insert(m.getEnd() + n, folderWebPath);
            n+=folderWebPath.length();
        }
        //切答案
        String[] split = stringBuilder.toString().split("、、、、、、、、、");
        //切一切真快乐
        String[] question=split[0].toString().split("[1-9]\\d*\\.{1}\\s|\\s[1-9]\\d*\\.\\s|[1-9]\\d*\\．{1}\\s|\\s[1-9]\\d*\\．\\s");
        //切选项装对象
        List<SelfQuestion> selfQuestionsList =new ArrayList<>();
        for (String qt:question
                ) {
            String[] qtSplit = qt.split("[A-Z]\\.|[A-Z]\\．|\\sD\\．$|\\sD\\.$|\\sD\\．\\s|\\sD\\.\\s");
            SelfQuestion questions = new SelfQuestion();
            if (qtSplit.length==1){
                questions.setQuestionContent(qtSplit[0]);
            }else{
                List<String> options =new ArrayList<>();
                for (int i = 0; i <qtSplit.length ; i++) {
                    if (i==0){
                        questions.setQuestionContent(qtSplit[i]);
                    }else {
                        options.add(qtSplit[i]);
                    }
                }
                questions.setOptions(options);
            }
            selfQuestionsList.add(questions);
        }

//        切答案
        if (split.length>1){
            String[] answer=split[1].split("[1-9]\\d*\\.{1}\\s|\\s[1-9]\\d*\\.\\s|[1-9]\\d*\\．{1}\\s|\\s[1-9]\\d*\\．\\s");
            for (int i = 1; i <answer.length ; i++) {
                int a=i;
                String[] answerSplit = answer[i].split("!!!!!|！！！！！");

                    SelfQuestion selfQuestioni = selfQuestionsList.get(a);
                    selfQuestioni.setAnswer(answerSplit[0]);
                    selfQuestionsList.set(a,selfQuestioni);

                if (answerSplit.length>1){

                        SelfQuestion selfQuestionii = selfQuestionsList.get(a);
                        selfQuestionii.setParse(answerSplit[1]);
                        selfQuestionsList.set(a,selfQuestionii);

                }
                if (answerSplit.length>2){

                    SelfQuestion selfQuestioniii = selfQuestionsList.get(a);
                    selfQuestioniii.setQuesetionType(answerSplit[2].replace(" ",""));
                    selfQuestionsList.set(a,selfQuestioniii);

                }

            }
        }
        return selfQuestionsList;
    }
}
