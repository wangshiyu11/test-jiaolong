package com.test.entity;

import com.test.base.BaseAuditable;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "base_role")
public class RoleInfo extends BaseAuditable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "roleName")
    private String roleName;

    @Column(name = "miaoShu")
    private String miaoShu;

    @Column(name = "leval")
    int leval;

    @Column(name = "parentId")
    int parentId;

    @Transient
    List<Long> menuMids;

}
