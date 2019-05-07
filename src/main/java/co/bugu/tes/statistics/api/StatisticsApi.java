package co.bugu.tes.statistics.api;

import co.bugu.common.RespDto;
import co.bugu.tes.statistics.enums.StatTypeEnum;
import co.bugu.tes.statistics.service.IStatisticsService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author daocers
 * @Date 2019/5/6:10:56
 * @Description:
 */
@RestController
@RequestMapping("/statistics/api")
public class StatisticsApi {
    private Logger logger = LoggerFactory.getLogger(StatisticsApi.class);


    @Autowired
    IStatisticsService statisticsService;

    /**
     * 获取指定场次错题数量列表
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/6 10:59
     */
    @RequestMapping("/getWrongCountList")
    public RespDto<PageInfo<Integer>> getQuestionWrongCountOfScene(Long sceneId, Integer pageNum, Integer pageSize) {
        return RespDto.success();
    }


    /**
     * 获取指定场次的试题错误率列表
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/6 11:02
     */
    @RequestMapping("/getWrongRateList")
    public RespDto<PageInfo<Float>> getWrongRateOfScene(Long sceneId, Integer pageNum, Integer pageSize) {
        return RespDto.success();
    }


    /**
     * 获取指定试题每场考试的错误率
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/6 11:11
     */
    @RequestMapping("/getQuestionFollow")
    public RespDto<PageInfo<Integer>> getQuestionWrongCountWithScene(Long questionId, Integer pageNum, Integer pageSize) {
        return RespDto.success();
    }


    /**
     * 每周考试人数，取最近10周
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/7 14:27
     */
    @RequestMapping("/getJoinUserCountByWeek")
    public RespDto<List<Integer>> getJoinUserCountByWeek(Integer size) {
        if (size == null) {
            size = 10;
        }

        List<Integer> list = statisticsService.getJoinUserCount(StatTypeEnum.WEEKLY.getCode(), size);
        return RespDto.success(list);
    }

    /**
     * 每月考试人数，取最近五个月
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/7 14:28
     */
    @RequestMapping("/getJoinUserCountByMonth")
    public RespDto<List<Integer>> getJoinUserCountByMonth(Integer size) {
        if (null == size) {
            size = 5;
        }
        List<Integer> list = statisticsService.getJoinUserCount(StatTypeEnum.MONTHLY.getCode(), size);
        return RespDto.success(list);
    }

    /**
     * 每日考试人数，取最近15天
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/7 14:29
     */
    @RequestMapping("/getJoinUserCountByDay")
    public RespDto<List<Integer>> getJoinUserCountByDay(Integer size) {
        if (null == size) {
            size = 15;
        }
        List<Integer> list = statisticsService.getJoinUserCount(StatTypeEnum.DAILY.getCode(), size);
        return RespDto.success(list);
    }


    /**
     * 场次 答题信息统计
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/7 17:56
     */
    @RequestMapping("/getSceneQuestionStat")
    public RespDto<Map<String, List<Long>>> getSceneQuestionStat(Integer size) {
        Map<String, List<Long>> res = statisticsService.getSceneQuestionStat(size);
        return RespDto.success(res);
    }


    /**
     * 试题信息统计
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/7 17:57
     */
    @RequestMapping("/getQuestionStat")
    public RespDto getQuestionStat(int size) {

        return RespDto.success();
    }


}
