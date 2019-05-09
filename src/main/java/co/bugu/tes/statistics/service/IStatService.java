package co.bugu.tes.statistics.service;

import co.bugu.tes.statistics.dto.*;

import java.util.List;

/**
 * @Author daocers
 * @Date 2019/5/8:15:11
 * @Description:
 */
public interface IStatService {
    /**
     * 场次的试题数量统计信息，
     *
     * @param type 统计类型，天，周， 月
     * @param size 统计多少个单位的数据，
     * @return
     * @auther daocers
     * @date 2019/5/8 15:13
     */
    List<SceneQuestionStatDto> getSceneQuestionStat(Integer type, Integer size);

    /**
     * 获取题库数据统计
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/8 15:14
     */
    List<QuestionBankStatDto> getQuestionBankStat();


    /**
     * 获取试题业务类型分布
     *
     * @param bankId 题库id，如果不指定，查询全部
     * @return
     * @auther daocers
     * @date 2019/5/8 15:16
     */
    QuestionDistributeDto getQuestionPropStat(Long bankId);


    /**
     * 获取试题难度分布
     *
     * @param bankId 题库id，不指定，查询全部
     * @return
     * @auther daocers
     * @date 2019/5/8 15:17
     */
    List<DifficultyStatDto> getDifficultyStat(Long bankId);


    List<QuestionDistributeStat> getSingleStat(Long bankId);

    List<QuestionDistributeStat> getMultiStat(Long bankId);

    List<QuestionDistributeStat> getJudgeStat(Long bankId);
}
