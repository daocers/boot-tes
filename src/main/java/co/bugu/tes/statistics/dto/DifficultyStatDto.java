package co.bugu.tes.statistics.dto;

import lombok.Data;

/**
 * @Author daocers
 * @Date 2019/5/8:15:10
 * @Description:
 */
@Data
public class DifficultyStatDto {
    private Long bankId;
    private Integer difficulty;
    private Integer count;
}
