package com.test.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * @author ：WSY
 * @description :TODO
 * @date ：Created in 2019/8/5 11:46
 */
@Entity
@Table(name = "base_user")
@Data
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "userName")
    String userName;

    @Column(name = "loginName")
    String loginName;

    @Column(name = "password")
    String password;

    @Column(name = "tel")
    String tel;

    @Column(name = "sex")
    int sex;

    @Column(name = "parentId")
    Long parentId;

    @Column(name = "buMen")
    String buMen;

    @Column(name = "url")
    String url;

    @Transient
    List<MenuInfo> menuInfoList;

    @Transient
    RoleInfo roleInfo;

    @Transient
    private Map<String,String> authmap;

    @Transient
    String roleName;
}
