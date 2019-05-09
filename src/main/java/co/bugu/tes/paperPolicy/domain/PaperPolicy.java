package co.bugu.tes.paperPolicy.domain;

import lombok.Data;

import java.util.Date;

@Data
public class PaperPolicy {
    private Long id;

    private String name;

    private String code;

    private String memo;

    private String singleInfo;

    private String multiInfo;

    private String judgeInfo;

    private Double singleScore;
    private Double multiScore;
    private Double judgeScore;

    private Integer singleCount;
    private Integer multiCount;
    private Integer judgeCount;

    private Integer receiptScore;
    private Integer receiptCount;

    private Integer numberLength;

    private Long branchId;

    private Long stationId;

    private Long departmentId;

    private Integer status;

    private Integer isDel;

    private Long createUserId;

    private Date createTime;

    private Long updateUserId;

    private Date updateTime;


}