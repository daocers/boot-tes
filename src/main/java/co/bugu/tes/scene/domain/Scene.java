package co.bugu.tes.scene.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Scene {
    private Long id;

    private String code;

    private String name;

    private Long branchId;

    private Long departmentId;

    private Long stationId;

    private String authCode;

    private Integer changePaper;

    private Integer delayMinute;

    private Integer duration;

    private String cancelReason;

    private Integer singleCount;

    private Double singleScore;

    private Integer multiCount;

    private Double multiScore;

    private Integer judgeCount;

    private Double judgeScore;

    private Integer receiptCount;

    private Integer numberLength;

    private Integer decimalLength;

    private Long paperPolicyId;

    private Integer paperGenerateType;

    private String remark;

    private Long questionBankId;

    private Integer userChoiceType;

    private Double totalScore;

    private Integer percentable;

    private String metaScoreInfo;

    private Integer status;

    private Date openTime;

    private Date closeTime;

    private Integer isDel;

    private Date createTime;

    private Long createUserId;

    private Date updateTime;

    private Long updateUserId;

}