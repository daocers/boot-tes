package co.bugu.tes.paperPolicy.api;

import co.bugu.common.RespDto;
import co.bugu.common.enums.BaseStatusEnum;
import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.branch.service.IBranchService;
import co.bugu.tes.department.service.IDepartmentService;
import co.bugu.tes.paperPolicy.agent.PaperPolicyAgent;
import co.bugu.tes.paperPolicy.domain.PaperPolicy;
import co.bugu.tes.paperPolicy.dto.ItemDto;
import co.bugu.tes.paperPolicy.dto.PaperPolicyCheckDto;
import co.bugu.tes.paperPolicy.dto.PaperPolicyDto;
import co.bugu.tes.paperPolicy.service.IPaperPolicyService;
import co.bugu.tes.paperPolicyCondition.domain.PaperPolicyCondition;
import co.bugu.tes.paperPolicyCondition.service.IPaperPolicyConditionService;
import co.bugu.tes.scene.domain.Scene;
import co.bugu.tes.scene.enums.SceneStatusEnum;
import co.bugu.tes.scene.service.ISceneService;
import co.bugu.tes.station.service.IStationService;
import co.bugu.tes.user.domain.User;
import co.bugu.tes.user.service.IUserService;
import co.bugu.util.CodeUtil;
import co.bugu.util.UserUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 数据api
 *
 * @author daocers
 * @create 2019-04-28 17:08
 */
@RestController
@RequestMapping("/paperPolicy/api")
public class PaperPolicyApi {
    private static Logger logger = LoggerFactory.getLogger(PaperPolicyApi.class);

    @Autowired
    IPaperPolicyService paperPolicyService;

    @Autowired
    IBranchService branchService;
    @Autowired
    IStationService stationService;
    @Autowired
    IDepartmentService departmentService;
    @Autowired
    IUserService userService;

    @Autowired
    PaperPolicyAgent paperPolicyAgent;

    @Autowired
    IPaperPolicyConditionService paperPolicyConditionService;

    @Autowired
    ISceneService sceneService;
    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2019-04-28 17:08
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<PaperPolicyDto>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody PaperPolicy paperPolicy) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(paperPolicy, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }

            PageInfo<PaperPolicy> pageInfo = paperPolicyService.findByConditionWithPage(pageNum, pageSize, paperPolicy);
            PageInfo<PaperPolicyDto> res = new PageInfo<>();
            BeanUtils.copyProperties(pageInfo, res);
            if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
                List<PaperPolicyDto> list = Lists.transform(pageInfo.getList(), item -> {
                    PaperPolicyDto dto = new PaperPolicyDto();
                    BeanUtils.copyProperties(item, dto);
                    dto.setSingleList(JSON.parseArray(item.getSingleInfo(), ItemDto.class));
                    dto.setMultiList(JSON.parseArray(item.getMultiInfo(), ItemDto.class));
                    dto.setJudgeList(JSON.parseArray(item.getJudgeInfo(), ItemDto.class));
                    dto.setStationName(stationService.findById(dto.getStationId()).getName());
                    dto.setDepartmentName(departmentService.findById(dto.getDepartmentId()).getName());
                    dto.setBranchName(branchService.findById(dto.getBranchId()).getName());
                    dto.setCreateUserName(userService.findById(item.getCreateUserId()).getName());
                    if (dto.getReceiptCount() == -1) {
                        dto.setReceiptCount(null);
                    }
                    if (dto.getNumberLength() == -1) {
                        dto.setNumberLength(null);
                    }
                    return dto;
                });
                res.setList(list);
            } else {
                res.setList(new ArrayList<>());
            }
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
     * @param paperPolicyDto
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2019-04-28 17:08
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Long> savePaperPolicy(@RequestBody PaperPolicyDto paperPolicyDto) {
        try {
            logger.debug("保存， savePaperPolicy, 参数： {}", JSON.toJSONString(paperPolicyDto, true));

            User user = UserUtil.getCurrentUser();
            paperPolicyDto = processData(paperPolicyDto);
            PaperPolicy paperPolicy = new PaperPolicy();
            BeanUtils.copyProperties(paperPolicyDto, paperPolicy);

            paperPolicy.setSingleInfo(JSON.toJSONString(paperPolicyDto.getSingleList()));
            paperPolicy.setMultiInfo(JSON.toJSONString(paperPolicyDto.getMultiList()));
            paperPolicy.setJudgeInfo(JSON.toJSONString(paperPolicyDto.getJudgeList()));
            paperPolicy.setCreateUserId(user.getId());
            paperPolicy.setUpdateUserId(user.getId());
            Long paperPolicyId = paperPolicyDto.getId();
            if (null == paperPolicyId) {
//         以创建时候的归属为准，修改时候不再更换归属
                paperPolicy.setBranchId(user.getBranchId());
                paperPolicy.setStationId(user.getStationId());
                paperPolicy.setDepartmentId(user.getDepartmentId());
                paperPolicy.setCode(CodeUtil.getPaperPolicyCode());
                paperPolicyId = paperPolicyService.add(paperPolicy);

            } else {
                paperPolicyService.updateById(paperPolicy);
            }
            logger.info("保存试卷策略 成功， id: {}", paperPolicyId);

            return RespDto.success(paperPolicyId);
        } catch (Exception e) {
            logger.error("保存 paperPolicy 失败", e);
            return RespDto.fail();
        }
    }

    private PaperPolicyDto processData(PaperPolicyDto dto) {
        List<ItemDto> singleList = trimNotFull(dto.getSingleList());
        List<ItemDto> multiList = trimNotFull(dto.getMultiList());
        List<ItemDto> judgeList = trimNotFull(dto.getJudgeList());
        dto.setSingleList(singleList);
        dto.setMultiList(multiList);
        dto.setJudgeList(judgeList);

        if (CollectionUtils.isNotEmpty(singleList)) {
            int count = 0;
            for (ItemDto item : singleList) {
                count += item.getCount();
            }
            dto.setSingleCount(count);
        } else {
            dto.setSingleCount(0);
        }
        if (CollectionUtils.isNotEmpty(multiList)) {
            int count = 0;
            for (ItemDto item : multiList) {
                count += item.getCount();
            }
            dto.setMultiCount(count);
        } else {
            dto.setMultiCount(0);
        }

        if (CollectionUtils.isNotEmpty(judgeList)) {
            int count = 0;
            for (ItemDto item : judgeList) {
                count += item.getCount();
            }
            dto.setJudgeCount(count);
        } else {
            dto.setJudgeCount(0);
        }

        return dto;
    }

    //     处理空数据，合并一样的数据，返回试题数量
    private List<ItemDto> trimNotFull(List<ItemDto> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        Map<String, Integer> map = new HashMap<>();
        Iterator<ItemDto> iter = list.iterator();
        while (iter.hasNext()) {
            ItemDto dto = iter.next();

            if (dto.getCount() != null) {
                Integer diff = dto.getDifficulty();
                Integer busi = dto.getBusiType();
                if (null == diff) {
                    diff = -1;
                }
                if (null == busi) {
                    busi = -1;
                }
                String key = busi + "-" + diff;
                if (!map.containsKey(key)) {
                    map.put(key, 0);
                }
                map.put(key, map.get(key) + dto.getCount());
            } else {
                iter.remove();
            }
        }
        List<ItemDto> itemList = new ArrayList<>();
        if (MapUtils.isNotEmpty(map)) {
            Iterator<String> keyIter = map.keySet().iterator();
            while (keyIter.hasNext()) {
                String key = keyIter.next();
                String[] items = key.split("-");
                Integer busi = Integer.parseInt(items[0]);
                Integer diff = Integer.parseInt(items[1]);
                ItemDto item = new ItemDto();
                item.setBusiType(busi);
                item.setDifficulty(diff);
                item.setCount(map.get(key));
                itemList.add(item);
            }
        }
        return itemList;
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.paperPolicy.domain.PaperPolicyDto>
     * @author daocers
     * @date 2019-04-28 17:08
     */
    @RequestMapping(value = "/getDetail")
    public RespDto<PaperPolicy> getDetail(Long id) {
        try {
            logger.info("获取详情，getDetail, id： {}", id);
            PaperPolicy paperPolicy = paperPolicyService.findById(id);
            return RespDto.success(paperPolicy);
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
     * @date 2019-04-28 17:08
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id) throws Exception {
        logger.debug("准备删除， 参数: {}", id);
        Preconditions.checkArgument(id != null, "id不能为空");
        Scene scene = new Scene();
        scene.setPaperPolicyId(id);
        scene.setStatus(SceneStatusEnum.READY.getCode());
        scene.setIsDel(DelFlagEnum.NO.getCode());
        List<Scene> scenes = sceneService.findByCondition(1, 1, scene);
        if(CollectionUtils.isNotEmpty(scenes)){
            throw new Exception("该策略已被场次使用过，不能删除");
        }
        Long userId = UserUtil.getCurrentUser().getId();
        int count = paperPolicyService.deleteById(id, userId);

        return RespDto.success(count == 1);
    }


    @RequestMapping(value = "/findAll")
    public RespDto<List<PaperPolicy>> findAll() {
        logger.info("findAll ..");
        List<PaperPolicy> list = paperPolicyService.findByCondition(new PaperPolicy());
        return RespDto.success(list);
    }

    /**
     * 查找所有可用的策略
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/7 13:50
     */
    @RequestMapping(value = "/findAvailable")
    public RespDto<List<PaperPolicy>> findAvailable(Long bankId) {
        PaperPolicyCondition query = new PaperPolicyCondition();
        query.setBankId(bankId);
        query.setStatus(BaseStatusEnum.ENABLE.getCode());
        List<PaperPolicyCondition> conditions = paperPolicyConditionService.findByCondition(query);
        if (CollectionUtils.isEmpty(conditions)) {
            logger.warn("没有符合的试卷策略  ");
            return RespDto.success(new ArrayList<>());
        }
        List<PaperPolicy> list = Lists.transform(conditions, item -> {
            Long paperPolicyId = item.getPaperPolicyId();
            PaperPolicy policy = paperPolicyService.findById(paperPolicyId);
            return policy;
        });
        return RespDto.success(list);
    }

    /**
     * 校验策略是否可用
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/6 14:34
     */
    @RequestMapping(value = "/checkPolicy")
    public RespDto<PaperPolicyCheckDto> checkPolicy(Long paperPolicyId, Long bankId) throws Exception {
        PaperPolicyCheckDto dto = paperPolicyAgent.checkPolicy(paperPolicyId, bankId);
        return RespDto.success(dto);
    }
}

