package co.bugu.tes.statistics.dto;

import lombok.Data;

import java.util.Map;

/**
 * @Author daocers
 * @Date 2019/5/6:11:12
 * @Description:
 */
@Data
public class QuestionStatDto {
    private Long questionId;
    private int wrongCount;
    private int totalCount;
    private Float wrongRate;
    // 记录每个选项的数量

    private Map<String, Integer> choiceInfo;
}
