package co.bugu.tes.receipt.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Receipt {
    private Long id;

    private Integer no;

    private Long number;

    private Long sceneId;

    private Integer status;

    private Integer isDel;

    private Long createUserId;

    private Date createTime;

    private Long updateUserId;

    private Date updateTime;
}