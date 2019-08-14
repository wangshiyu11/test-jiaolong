package com.test.entity;

import com.test.base.BaseAuditable;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "base_menu")
public class MenuInfo extends BaseAuditable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "menuName")
    private String menuName;

    @Column(name = "parentId")
    private Long parentId;

    @Column(name = "leval")
    private int leval;

    @Column(name = "url")
    private String url;

    @Transient
    private List<MenuInfo> menuInfoList;

    @Transient
    String roleName;

}
