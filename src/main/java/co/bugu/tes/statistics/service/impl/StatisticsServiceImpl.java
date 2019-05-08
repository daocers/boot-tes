package co.bugu.tes.statistics.service.impl;

import co.bugu.tes.statistics.dao.StatisticsDao;
import co.bugu.tes.statistics.dto.SceneQuestionStatDto;
import co.bugu.tes.statistics.dto.Stat;
import co.bugu.tes.statistics.dto.UserStatDto;
import co.bugu.tes.statistics.enums.StatTypeEnum;
import co.bugu.tes.statistics.service.IStatisticsService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
    public List<UserStatDto> getJoinUserCount(Integer type, Integer size) {
        List<UserStatDto> res = new ArrayList<>();
        PageHelper.startPage(1, size, "begin_time desc");

        List<Stat> list = new ArrayList<>();
        if (type == StatTypeEnum.DAILY.getCode()) {
            list = statisticsDao.getDailyJoinUser();
        } else if (type == StatTypeEnum.WEEKLY.getCode()) {
//            list = statisticsDao.getWeeklyJoinUser();
        } else if (type == StatTypeEnum.MONTHLY.getCode()) {
//            list = statisticsDao.getMonthlyJoinUser();
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
    public List<SceneQuestionStatDto> getSceneQuestionStat(Integer size) {
        if (null == size) {
            size = 5;
        }
        List<SceneQuestionStatDto> res = new ArrayList<>();

        PageHelper.startPage(1, size, "create_time desc");
        List<SceneQuestionStatDto> list = statisticsDao.getSceneQuestionCountList();

        List<SceneQuestionStatDto> wrongList = statisticsDao.getSceneQuestionWrongCountList();

        if(CollectionUtils.isEmpty(list)){
            logger.warn("还没有考试信息");
            return res;
        }
        Map<Long, Integer> sceneWrongMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(wrongList)){
            for(SceneQuestionStatDto wrong: wrongList){
                sceneWrongMap.put(wrong.getSceneId(), wrong.getWrong());
            }
        }

        for(SceneQuestionStatDto total : list){
            Long sceneId = total.getSceneId();
            total.setWrong(sceneWrongMap.containsKey(sceneId) ? sceneWrongMap.get(sceneId) : 0);
        }

        res = Lists.transform(list, item -> {
            Long sceneId = item.getSceneId();
            int wrong = sceneWrongMap.containsKey(sceneId) ? sceneWrongMap.get(sceneId) : 0;
            item.setWrong(wrong);
            item.setRightRate((item.getTotal() - wrong) / item.getTotal() * 100);
            return item;
        });
        return res;
    }


    private List<UserStatDto> getUserCountList(List<Stat> list, Integer size) {
        List<UserStatDto> res = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Map<Integer, Integer> map = new HashMap<>();
        if (CollectionUtils.isEmpty(list)) {
            logger.warn("还没有参考人员信息");
        } else {
            for (Stat item : list) {
                map.put(item.getNo(), item.getCount());
            }
        }

        calendar.add(Calendar.DAY_OF_YEAR, 0 - size);

        SimpleDateFormat format = new SimpleDateFormat("mm-dd");
        for (int i = 0; i < size; i++) {
            UserStatDto dto = new UserStatDto();
            int day = calendar.get(Calendar.DAY_OF_YEAR);
            dto.setDate(calendar.getTime());
            dto.setJoinCount(map.containsKey(day) ? map.get(day) : 0);
            dto.setDateStr(format.format(dto.getDate()));
            res.add(dto);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        return res;
    }
}
