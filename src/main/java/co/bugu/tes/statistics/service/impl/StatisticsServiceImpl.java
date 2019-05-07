package co.bugu.tes.statistics.service.impl;

import co.bugu.tes.statistics.dao.StatisticsDao;
import co.bugu.tes.statistics.enums.StatTypeEnum;
import co.bugu.tes.statistics.service.IStatisticsService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author daocers
 * @Date 2019/5/7:14:37
 * @Description:
 */
@Service
public class StatisticsServiceImpl implements IStatisticsService {
    private Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    @Autowired
    StatisticsDao statisticsDao;

    @Override
    public List<Integer> getJoinUserCount(Integer type, Integer size) {
        List<Integer> res = new ArrayList<>();
        PageHelper.startPage(1, size, "begin_time desc");

        List<Map<String, Integer>> list = new ArrayList<>();
        if (type == StatTypeEnum.DAILY.getCode()) {
            list = statisticsDao.getDailyJoinUser();
        } else if (type == StatTypeEnum.WEEKLY.getCode()) {
            list = statisticsDao.getWeeklyJoinUser();
        } else if (type == StatTypeEnum.MONTHLY.getCode()) {
            list = statisticsDao.getMonthlyJoinUser();
        }
        res = getUserCountList(list, size);

        return res;
    }

    /**
     * 场次统计数据   总答题次数  错误次数， 正确率
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/7 17:53
     */
    @Override
    public Map<String, List<Long>> getSceneQuestionStat(Integer size) {
        if (null == size) {
            size = 5;
        }

        PageHelper.startPage(1, size, "create_time desc");
        List<Map<String, Long>> list = statisticsDao.getSceneQuestionCountList();
        Map<Long, Long> map = new HashMap<>();
        if (CollectionUtils.isEmpty(list)) {
            logger.warn("还没有响应的场次考试数据");
        } else {
            for (Map<String, Long> item : list) {
                map.put(item.get("sceneId"), item.get("count"));
            }
        }


        List<Map<String, Long>> wrongList = statisticsDao.getSceneQuestionWrongCountList();
        Map<Long, Long> wrongMap = new HashMap<>();
        if (CollectionUtils.isEmpty(list)) {
            logger.warn("没有答错的题，这不正常");
        } else {
            for (Map<String, Long> item : wrongList) {
                wrongMap.put(item.get("sceneId"), item.get("count"));
            }
        }

        List<Long> total = new ArrayList<>();
        List<Long> wrong = new ArrayList<>();
        List<Long> wRate = new ArrayList<>();
        for (Map<String, Long> item : list) {
            Long sceneId = item.get("sceneId");
            Long tCount = map.get(sceneId);
            Long wCount = wrongMap.containsKey(sceneId) ? wrongMap.get(sceneId) : 0L;
            Long rCount = tCount - wCount;
            total.add(tCount);
            wrong.add(wCount);
            wRate.add(rCount * 100 / tCount);
        }

        Map<String, List<Long>> res = new HashMap<>();
        res.put("total", total);
        res.put("wrong", wrong);
        res.put("rightRate", wRate);

        return res;
    }


    private List<Integer> getUserCountList(List<Map<String, Integer>> list, Integer size) {
        List<Integer> res = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Map<Integer, Integer> map = new HashMap<>();
        if (CollectionUtils.isEmpty(list)) {
            logger.warn("还没有参考人员信息");
        } else {
            for (Map<String, Integer> item : list) {
                map.put(item.get("no"), item.get("count"));
            }
        }

        calendar.add(Calendar.DAY_OF_YEAR, 0 - size);

        for (int i = 0; i < size; i++) {
            int day = calendar.get(Calendar.DAY_OF_YEAR);
            res.add(map.containsKey(day) ? map.get(day) : 0);
        }
        return res;
    }
}
