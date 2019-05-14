package co.bugu.tes.permission.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Permission {
    private Long id;

    private Long superiorId;

    private String code;

    private String name;

    private String url;

    private String icon;

    private String controller;

    private String action;

    private String httpMethod;

    private Integer status;

    private Integer no;

    private String memo;

    private Integer type;

    private Integer isDel;

    private Long createUserId;

    private Date createTime;

    private Long updateUserId;

    private Date updateTime;

}