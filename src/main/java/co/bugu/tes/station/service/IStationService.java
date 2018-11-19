package co.bugu.tes.station.service;

import co.bugu.tes.station.domain.Station;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-11-19 17:51
 */
public interface IStationService {

    /**
     * 新增
     *
     * @param station
     * @return
     */
    long add(Station station);

    /**
     * 通过id更新
     *
     * @param station
     * @return
     */
    int updateById(Station station);

    /**
     * 条件查询
     *
     * @param station
     * @return
     */
    List<Station> findByCondition(Station station);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param station     查询条件
     * @return
     */
    List<Station> findByCondition(Integer pageNum, Integer pageSize, Station station);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param station     查询条件
     * @return
     */
    PageInfo<Station> findByConditionWithPage(Integer pageNum, Integer pageSize, Station station);

    /**
     * 通过id查询
     *
     * @param stationId
     * @return
     */
    Station findById(Long stationId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param stationId
     * @return
     */
    int deleteById(Long stationId, Long operatorId);

}
