package co.bugu.tes.statistics.dao;

import co.bugu.tes.statistics.dto.AnswerStatDto;
import co.bugu.tes.statistics.dto.QuestionStat;
import co.bugu.tes.statistics.dto.SceneQuestionStatDto;
import co.bugu.tes.statistics.dto.Stat;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author daocers
 * @Date 2019/5/7:16:34
 * @Description:
 */

public interface StatisticsDao {
    List<Stat> getDailyJoinUser();

    List<Map<String, Integer>> getWeeklyJoinUser();

    List<Map<String, Integer>> getMonthlyJoinUser();

    List<SceneQuestionStatDto> getSceneQuestionCountList();

    List<SceneQuestionStatDto> getSceneQuestionWrongCountList();

    List<AnswerStatDto> getQuestionAnswerInfo(Long questionId);

    List<QuestionStat> getSingleStat(@Param("bankId") Long bankId);

    List<QuestionStat> getMultiStat(@Param("bankId") Long bankId);

    List<QuestionStat> getJudgeStat(@Param("bankId") Long bankId);
}
