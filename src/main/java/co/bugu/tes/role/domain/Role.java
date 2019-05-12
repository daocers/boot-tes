package co.bugu.tes.role.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Role {
    private Long id;

    private String name;

    private String code;

    private String memo;

    private Integer status;

    private Integer type;

    private Integer isDel;

    private Long createUserId;

    private Date createTime;

    private Long updateUserId;

    private Date updateTime;
}