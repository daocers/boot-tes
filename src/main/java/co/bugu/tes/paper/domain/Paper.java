package co.bugu.tes.paper.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Paper {
    private Long id;

    private String code;

    private Integer answerFlag;

    private Date beginTime;

    private Date endTime;

    private Double originalScore;

    private Double score;

    private Long sceneId;

    private Long userId;

    private Integer status;

    private Integer isDel;

    private Date createTime;

    private Long createUserId;

    private Date updateTime;

    private Long updateUserId;

    private Double receiptScore;

    private Double receiptRate;

    private Double commonScore;

}