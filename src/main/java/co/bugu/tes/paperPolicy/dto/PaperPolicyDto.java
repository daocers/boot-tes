package co.bugu.tes.paperPolicy.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaperPolicyDto {
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

    private List<ItemDto> singleList;
    private List<ItemDto> multiList;
    private List<ItemDto> judgeList;


    private String branchName;
    private String stationName;
    private String departmentName;

    private String createUserName;


}