package co.bugu.tes.receiptRecord.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ReceiptRecord {
    private Long id;

    private Long sceneId;

    private Long userId;

    private Integer count;

    private Integer seconds;

    private Integer rightCount;

    private Integer falseCount;

    private Double rate;

    private Integer isDel;

    private Date updateTime;

    private Long updateUserId;

    private Long createUserId;

    private Date createTime;

}