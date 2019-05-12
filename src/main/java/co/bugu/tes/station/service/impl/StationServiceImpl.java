package co.bugu.tes.station.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.station.dao.StationDao;
import co.bugu.tes.station.domain.Station;
import co.bugu.tes.station.service.IStationService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author daocers
 * @create 2018-11-20 17:15
 */
@Service
public class StationServiceImpl implements IStationService {
    @Autowired
    StationDao stationDao;

    private Logger logger = LoggerFactory.getLogger(StationServiceImpl.class);

    private static String ORDER_BY = "id desc";


    private Cache<Long, Station> stationCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .concurrencyLevel(3)
            .build();

    @Override
    public long add(Station station) {
        //todo 校验参数
        station.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        station.setCreateTime(now);
        station.setUpdateTime(now);
        stationDao.insert(station);
        return station.getId();
    }

    @Override
    public int updateById(Station station) {
        logger.debug("station updateById, 参数： {}", JSON.toJSONString(station, true));
        Preconditions.checkNotNull(station.getId(), "id不能为空");
        stationCache.invalidate(station.getId());
        station.setUpdateTime(new Date());
        return stationDao.updateById(station);
    }

    @Override
    public List<Station> findByCondition(Station station) {
        logger.debug("station findByCondition, 参数： {}", JSON.toJSONString(station, true));
        PageHelper.orderBy(ORDER_BY);
        List<Station> stations = stationDao.findByObject(station);

        logger.debug("查询结果， {}", JSON.toJSONString(stations, true));
        return stations;
    }

    @Override
    public List<Station> findByCondition(Integer pageNum, Integer pageSize, Station station) {
        logger.debug("station findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(station, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Station> stations = stationDao.findByObject(station);

        logger.debug("查询结果， {}", JSON.toJSONString(stations, true));
        return stations;
    }

    @Override
    public PageInfo<Station> findByConditionWithPage(Integer pageNum, Integer pageSize, Station station) {
        logger.debug("station findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(station, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Station> stations = stationDao.findByObject(station);

        logger.debug("查询结果， {}", JSON.toJSONString(stations, true));
        return new PageInfo<>(stations);
    }

    @Override
    public Station findById(Long stationId) {
        logger.debug("station findById, 参数 stationId: {}", stationId);
        Station station = stationCache.getIfPresent(stationId);
        if (null != station) {
            return station;
        }
        station = stationDao.selectById(stationId);
        if (station == null) {
            station = new Station();
        }
        stationCache.put(stationId, station);
        logger.debug("查询结果： {}", JSON.toJSONString(station, true));
        return station;
    }

    @Override
    public int deleteById(Long stationId, Long operatorId) {
        logger.debug("station 删除， 参数 stationId : {}", stationId);
        stationCache.invalidate(stationId);
        Station station = new Station();
        station.setId(stationId);
        station.setIsDel(DelFlagEnum.YES.getCode());
        station.setUpdateTime(new Date());
        station.setUpdateUserId(operatorId);
        int num = stationDao.updateById(station);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
