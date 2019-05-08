package co.bugu.tes.statistics.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author daocers
 * @Date 2019/5/8:16:07
 * @Description:
 *
 */
@Data
public class QuestionDistributeDto {
    private Long BankId;
    private List<BusiTypeStatDto> busiTypeStatDtoList;
    private List<DifficultyStatDto> difficultyStatDtoList;
}
