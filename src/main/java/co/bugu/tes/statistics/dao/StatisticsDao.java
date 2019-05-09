package co.bugu.tes.statistics.dao;

import co.bugu.tes.statistics.dto.AnswerStatDto;
import co.bugu.tes.statistics.dto.QuestionDistributeStat;
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

    List<QuestionDistributeStat> getSingleStat(@Param("bankId") Long bankId);

    List<QuestionDistributeStat> getMultiStat(@Param("bankId") Long bankId);

    List<QuestionDistributeStat> getJudgeStat(@Param("bankId") Long bankId);


}
