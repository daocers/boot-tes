package co.bugu.tes.statistics.service;

import java.util.List;
import java.util.Map;

/**
 * @Author daocers
 * @Date 2019/5/7:14:34
 * @Description:
 */
public interface IStatisticsService {
    List<Integer> getJoinUserCount(Integer type, Integer size);

    /**
     * 场次 答题信息统计
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/7 17:54
     */
    Map<String, List<Long>> getSceneQuestionStat(Integer size);


}
