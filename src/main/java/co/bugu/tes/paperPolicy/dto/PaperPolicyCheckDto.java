package co.bugu.tes.paperPolicy.dto;

import lombok.Data;

import java.util.List;

/**
 * 试卷策略校验dto
 * @Author daocers
 * @Date 2019/5/6:14:48
 * @Description:
 */
@Data
public class PaperPolicyCheckDto {
    private List<ItemDto> singles;
    private List<ItemDto> multies;
    private List<ItemDto> judges;

    private Integer singleCount;
    private Integer realSingleCount;
    private Integer multiCount;
    private Integer realMultiCount;
    private Integer judgeCount;
    private Integer realJudgeCount;

    private Boolean valid;
    private Long id;
    private String name;
    private Long bankId;
}
