package co.bugu.tes.answer.dto;

import lombok.Data;

/**
 * 统计需求使用
 * @Author daocers
 * @Date 2019/5/9:10:36
 * @Description:
 */
@Data
public class AnswerStatDto {
    private Integer count;
    private Integer questionType;
    private Long questionId;
    private String rightAnswer;
    private String answer;
    private Long id;
}
