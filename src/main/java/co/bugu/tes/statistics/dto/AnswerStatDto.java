package co.bugu.tes.statistics.dto;

import lombok.Data;

/**
 * @Author daocers
 * @Date 2019/5/7:18:42
 * @Description:
 */
@Data
public class AnswerStatDto {
    private Integer count;
    private String answer;
    private String rightAnswer;
    private Long questionId;
    private Long sceneId;
}
