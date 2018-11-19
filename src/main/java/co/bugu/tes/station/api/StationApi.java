package co.bugu.tes.station.api;

import co.bugu.common.RespDto;
import co.bugu.tes.station.domain.Station;
import co.bugu.tes.station.service.IStationService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据api
 *
 * @author daocers
 * @create 2018-11-19 17:51
 */
@RestController
@RequestMapping("/station/api")
public class StationApi {
    private Logger logger = LoggerFactory.getLogger(StationApi.class);

    @Autowired
    IStationService stationService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-19 17:51
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<Station>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Station station) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(station, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<Station> list = stationService.findByCondition(pageNum, pageSize, station);
            PageInfo<Station> pageInfo = new PageInfo<>(list);
            logger.info("查询到数据： {}", JSON.toJSONString(pageInfo, true));
            return RespDto.success(pageInfo);
        } catch (Exception e) {
            logger.error("findByCondition  失败", e);
            return RespDto.fail();
        }
    }

    /**
     * 保存
     *
     * @param station
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-19 17:51
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveStation(@RequestBody Station station) {
        try {
            Long stationId = station.getId();
            if(null == stationId){
                logger.debug("保存， saveStation, 参数： {}", JSON.toJSONString(station, true));
                stationId = stationService.add(station);
                logger.info("新增 成功， id: {}", stationId);
            }else{
                stationService.updateById(station);
                logger.debug("更新成功", JSON.toJSONString(station, true));
            }
            return RespDto.success(stationId != null);
        } catch (Exception e) {
            logger.error("保存 station 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.station.domain.Station>
     * @author daocers
     * @date 2018-11-19 17:51
     */
    @RequestMapping(value = "/findById")
    public RespDto<Station> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            Station station = stationService.findById(id);
            return RespDto.success(station);
        } catch (Exception e) {
            logger.error("获取详情失败", e);
            return RespDto.fail();
        }
    }

    /**
     * 删除，软删除，更新数据库删除标志
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-19 17:51
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id, Long operatorId) {
        try {
            logger.debug("准备删除， 参数: {}", id);
            Preconditions.checkArgument(id != null, "id不能为空");
            int count = stationService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

