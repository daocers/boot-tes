package co.bugu.tes.scene.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author daocers
 * @Date 2018/12/2:23:26
 * @Description:
 */
@Data
public class MyOpenDto {
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

    private Integer receiptScore;

    private Integer numberLength;

    private Integer decimalLength;


    private Long paperPolicyId;

    private String paperPolicyName;

    private Integer paperGenerateType;

    private String remark;

    private Long questionBankId;

    private String questionBankName;

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

}
