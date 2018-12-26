package com.jichuangsi.school.questionsrepository.service.impl.trans;

import com.jichuangsi.school.questionsrepository.model.translate.PicContent;
import com.jichuangsi.school.questionsrepository.service.IPicTranslationService;
import org.json.JSONArray;
import org.json.JSONObject;
import com.baidu.aip.ocr.AipOcr;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class BaiduPicTranslationService implements IPicTranslationService{
    //设置APPID/AK/SK
    public static final String APP_ID = "15272921";
    public static final String API_KEY = "s4RlpYtqInSnr38K08dkgVCY";
    public static final String SECRET_KEY = "HKil2mEleGXwmXKYF80DRUbdw6Zmjx2B";

    private static AipOcr client;
    private static AipOcr getInstance() {
        if (client == null) {
            client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        }
        return client;
    }

    public PicContent translate(byte[] content) {
        // 初始化一个AipOcr
        AipOcr client = getInstance();

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        //client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        //System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        /*String path = "test.jpg";
        JSONObject res = client.basicGeneral(path, new HashMap<String, String>());*/

        JSONObject res = client.basicGeneral(content, new HashMap<String, String>());
        //System.out.println(res.toString(2));

        return ConvertResult2Object(res);
    }

    private PicContent ConvertResult2Object(JSONObject res){
        PicContent picContent = new PicContent();
        JSONArray arr = res.getJSONArray("words_result");
        List<String> content = new ArrayList<String>(res.getInt("words_result_num"));
        for(int i = 0; i < arr.length(); i++){
            content.add(arr.getJSONObject(i).getString("words"));
        }
        picContent.getContent().addAll(content);
        return picContent;
    }
}
