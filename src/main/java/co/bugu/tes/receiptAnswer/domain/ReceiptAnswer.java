package co.bugu.tes.receiptAnswer.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ReceiptAnswer {
    private Long id;

    private Long receiptId;

    private Integer no;

    private Long number;

    private Long answer;

    private Long sceneId;

    private Long userId;

    private Integer status;

    private Integer isDel;

    private Long createUserId;

    private Date createTime;

    private Long updateUserId;

    private Date updateTime;

}