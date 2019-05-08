package co.bugu.tes.statistics.service;

import co.bugu.tes.statistics.dto.SceneQuestionStatDto;
import co.bugu.tes.statistics.dto.UserStatDto;

import java.util.List;

/**
 * @Author daocers
 * @Date 2019/5/7:14:34
 * @Description:
 */
public interface IStatisticsService {
    List<UserStatDto> getJoinUserCount(Integer type, Integer size);

    /**
     * 场次 答题信息统计
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/7 17:54
     */
    List<SceneQuestionStatDto> getSceneQuestionStat(Integer size);


}
