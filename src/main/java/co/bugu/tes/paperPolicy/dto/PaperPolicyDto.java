package co.bugu.tes.paperPolicy.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaperPolicyDto {
    private Long id;

    private String name;

    private String code;

    private String memo;

//    private String singleInfo;
//
//    private String multiInfo;
//
//    private String judgeInfo;

    private Integer receiptCount;

    private Integer numberLength;

    private Long branchId;

    private Long stationId;

    private Long departmentId;

    private List<ItemDto> singleInfo;
    private List<ItemDto> multiInfo;
    private List<ItemDto> judgeInfo;


}