package com.jichuangsi.school.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.*;

@Entity
@Table(name = "roleurl")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Roleurl {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String name;
    private String url;
    @ManyToOne
    @JoinColumn(name = "usewayid")
    private UseWay useWay;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UseWay getUseWay() {
        return useWay;
    }

    public void setUseWay(UseWay useWay) {
        this.useWay = useWay;
    }
}
