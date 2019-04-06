package cn.com.jichuangsi.school.backstage.entity.orz;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "back_promised_info")
public class PromisedInfo {
    @Id
    private String id;
    private String name;
    private List<PromisedInfo> promised = new ArrayList<PromisedInfo>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PromisedInfo> getPromised() {
        return promised;
    }

    public void setPromised(List<PromisedInfo> promised) {
        this.promised = promised;
    }
}
