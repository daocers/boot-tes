package co.bugu.tes.statistics.dao;

import co.bugu.tes.statistics.dto.AnswerStatDto;

import java.util.List;
import java.util.Map;

/**
 * @Author daocers
 * @Date 2019/5/7:16:34
 * @Description:
 */

public interface StatisticsDao {
    List<Map<String, Integer>> getDailyJoinUser();

    List<Map<String, Integer>> getWeeklyJoinUser();

    List<Map<String, Integer>> getMonthlyJoinUser();

    List<Map<String, Long>> getSceneQuestionCountList();

    List<Map<String, Long>> getSceneQuestionWrongCountList();

    List<AnswerStatDto> getQuestionAnswerInfo(Long questionId);
}
