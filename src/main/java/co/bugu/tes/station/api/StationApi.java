package co.bugu.tes.station.api;

import co.bugu.common.RespDto;
import co.bugu.common.enums.BaseStatusEnum;
import co.bugu.common.enums.DelFlagEnum;
import co.bugu.exception.UserException;
import co.bugu.tes.manager.domain.Manager;
import co.bugu.tes.manager.enums.ManagerTypeEnum;
import co.bugu.tes.manager.service.IManagerService;
import co.bugu.tes.station.domain.Station;
import co.bugu.tes.station.dto.StationDto;
import co.bugu.tes.station.service.IStationService;
import co.bugu.tes.user.domain.User;
import co.bugu.tes.user.service.IUserService;
import co.bugu.util.CodeUtil;
import co.bugu.util.UserUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
 * @create 2018-11-20 17:15
 */
@RestController
@RequestMapping("/station/api")
public class StationApi {
    private Logger logger = LoggerFactory.getLogger(StationApi.class);

    @Autowired
    IStationService stationService;
    @Autowired
    IUserService userService;
    @Autowired
    IManagerService managerService;


    /**
     * 设置管理员
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/12/14 12:00
     */
    @RequestMapping(value = "/setManager")
    public RespDto<Boolean> setManger(String userIds, Long targetId) {
        try {
            List<Long> ids = JSON.parseArray(userIds, Long.class);
            for (Long userId : ids) {
                managerService.setManager(ManagerTypeEnum.STATION.getCode(), userId, targetId);
            }
            return RespDto.success(true);
        } catch (Exception e) {
            logger.error("设置部门管理员失败", e);
            return RespDto.fail("设置管理员失败");
        }
    }


    @RequestMapping(value = "/removeManager")
    public RespDto<Boolean> removeManager(Long userId, Long stationId) {
        try {
            Manager query = new Manager();
            query.setUserId(userId);
            query.setTargetId(stationId);
            query.setType(ManagerTypeEnum.STATION.getCode());
            List<Manager> managers = managerService.findByCondition(query);
            Long currentUserId = UserUtil.getCurrentUser().getId();
            Manager manager = new Manager();
            manager.setId(managers.get(0).getId());
            manager.setIsDel(DelFlagEnum.YES.getCode());
            manager.setTargetId(stationId);
            manager.setUserId(userId);
            manager.setUpdateUserId(currentUserId);
            managerService.updateById(manager);
            return RespDto.success(true);
        } catch (Exception e) {
            logger.error("删除管理员失败", e);
            return RespDto.fail("删除管理员失败");
        }
    }

    @RequestMapping(value = "/getUnderManager")
    public RespDto<List<Station>> getUnderManager() throws UserException {
        Long userId = UserUtil.getCurrentUser().getId();
        Manager query = new Manager();
        query.setUserId(userId);
        query.setType(ManagerTypeEnum.STATION.getCode());
        query.setIsDel(DelFlagEnum.NO.getCode());
        List<Manager> list = managerService.findByCondition(query);
        if (CollectionUtils.isNotEmpty(list)) {
            List<Station> stations = Lists.transform(list, new Function<Manager, Station>() {
                @Override
                public Station apply(@Nullable Manager manager) {
                    Long stationId = manager.getTargetId();
                    Station station = stationService.findById(stationId);
                    logger.info("stationId: {}, {}", new String[]{"" + stationId, JSON.toJSONString(station, true)});

                    return station;
                }
            });
            return RespDto.success(stations);
        } else {
            return RespDto.success();
        }
    }

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<StationDto>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Station station) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(station, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            PageInfo<Station> pageInfo = stationService.findByConditionWithPage(pageNum, pageSize, station);
            PageInfo<StationDto> res = new PageInfo<>();
            BeanUtils.copyProperties(pageInfo, res);
            List<StationDto> list = Lists.transform(pageInfo.getList(), new Function<Station, StationDto>() {
                @Override
                public StationDto apply(@Nullable Station station) {
                    StationDto dto = new StationDto();
                    BeanUtils.copyProperties(station, dto);
                    User user = userService.findById(station.getCreateUserId());
                    if (user != null) {
                        dto.setCreateUserName(user.getName());
                    }
                    dto.setUserList(managerService.getManager(ManagerTypeEnum.STATION.getCode(), station.getId()));
                    return dto;
                }
            });
            res.setList(list);
            logger.info("查询到数据： {}", JSON.toJSONString(pageInfo, true));
            return RespDto.success(res);
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
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveStation(@RequestBody Station station) {
        try {
            Long userId = UserUtil.getCurrentUser().getId();
            station.setUpdateUserId(userId);
            Long stationId = station.getId();

            if (station.getStatus() == null) {
                station.setStatus(BaseStatusEnum.ENABLE.getCode());
            }
            if (null == stationId) {
                station.setCode(CodeUtil.getStationCode());
                station.setCreateUserId(userId);
                logger.debug("保存， saveStation, 参数： {}", JSON.toJSONString(station, true));
                stationId = stationService.add(station);
                logger.info("新增 成功， id: {}", stationId);
            } else {
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
     * @date 2018-11-20 17:15
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
     * @date 2018-11-20 17:15
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

    @RequestMapping(value = "/findAll")
    public RespDto<List<Station>> findAll() {
        List<Station> stations = stationService.findByCondition(null);
        return RespDto.success(stations);
    }
}

