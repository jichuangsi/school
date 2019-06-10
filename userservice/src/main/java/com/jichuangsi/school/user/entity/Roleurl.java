package com.jichuangsi.school.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.*;

@Entity
@Table(name = "roleurl")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Roleurl {
    @Id
    /*@GeneratedValue(strategy = GenerationType.IDENTITY)*/
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String name;
    private String url;

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
}
