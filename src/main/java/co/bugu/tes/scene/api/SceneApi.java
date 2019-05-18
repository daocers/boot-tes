package co.bugu.tes.scene.api;

import co.bugu.common.RespDto;
import co.bugu.common.enums.DelFlagEnum;
import co.bugu.exception.UserException;
import co.bugu.tes.paper.domain.Paper;
import co.bugu.tes.paper.dto.PaperDto;
import co.bugu.tes.paper.service.IPaperService;
import co.bugu.tes.paperPolicy.agent.PaperPolicyAgent;
import co.bugu.tes.paperPolicy.domain.PaperPolicy;
import co.bugu.tes.paperPolicy.dto.PaperPolicyCheckDto;
import co.bugu.tes.paperPolicy.service.IPaperPolicyService;
import co.bugu.tes.questionBank.domain.QuestionBank;
import co.bugu.tes.questionBank.service.IQuestionBankService;
import co.bugu.tes.receipt.domain.Receipt;
import co.bugu.tes.receipt.service.IReceiptService;
import co.bugu.tes.scene.agent.SceneAgent;
import co.bugu.tes.scene.domain.Scene;
import co.bugu.tes.scene.dto.MyJoinDto;
import co.bugu.tes.scene.dto.MyOpenDto;
import co.bugu.tes.scene.dto.SceneDto;
import co.bugu.tes.scene.dto.SceneMonitorDto;
import co.bugu.tes.scene.enums.SceneStatusEnum;
import co.bugu.tes.scene.service.ISceneService;
import co.bugu.tes.user.domain.User;
import co.bugu.util.CodeUtil;
import co.bugu.util.UserUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据api
 *
 * @author daocers
 * @create 2018-11-20 17:15
 */
@RestController
@RequestMapping("/scene/api")
public class SceneApi {
    private Logger logger = LoggerFactory.getLogger(SceneApi.class);

    @Autowired
    ISceneService sceneService;
    @Autowired
    IPaperService paperService;
    @Autowired
    IQuestionBankService bankService;
    @Autowired
    SceneAgent sceneAgent;
    @Autowired
    IReceiptService receiptService;

    @Autowired
    IPaperPolicyService paperPolicyService;

    @Autowired
    PaperPolicyAgent paperPolicyAgent;

    //    凭条小数点位数，默认2
    @Value("${tes.receipt.decimalLength:2}")
    private Integer decimalLength;
    //    凭条数字的长度，默认6
    @Value("${tes.receipt.numberLength:6}")
    private Integer numberLength;


    @RequestMapping("/myOpen")
    public RespDto<PageInfo<MyOpenDto>> findMyOpen(Integer pageNum, Integer pageSize, Integer status, String name) {
        try {
            User user = UserUtil.getCurrentUser();
            Long userId = user.getId();
            Scene query = new Scene();
            query.setStatus(status);
            query.setCreateUserId(userId);
            query.setIsDel(DelFlagEnum.NO.getCode());
            if (StringUtils.isNotEmpty(name)) {
                query.setName("%" + name + "%");
            }
            PageInfo<Scene> pageInfo = sceneService.findByConditionWithPage(pageNum, pageSize, query);
            PageInfo<MyOpenDto> res = new PageInfo<>();
            BeanUtils.copyProperties(pageInfo, res);
            List<MyOpenDto> list = Lists.transform(pageInfo.getList(), new Function<Scene, MyOpenDto>() {
                @Override
                public MyOpenDto apply(@Nullable Scene scene) {
                    MyOpenDto dto = new MyOpenDto();
                    BeanUtils.copyProperties(scene, dto);
                    QuestionBank bank = bankService.findById(scene.getQuestionBankId());
                    if (null != bank) {
                        dto.setQuestionBankName(bank.getName());
                    }
                    PaperPolicy paperPolicy = paperPolicyService.findById(scene.getPaperPolicyId());
                    if (null != paperPolicy) {
                        dto.setPaperPolicyName(paperPolicy.getName());
                    }

                    return dto;
                }
            });
            res.setList(list);
            return RespDto.success(res);
        } catch (Exception e) {
            logger.error("获取我开场的信息失败", e);
            return RespDto.fail("获取场次信息失败");
        }
    }


    /**
     * 我参加的考试，通过试卷判断
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/12/2 22:31
     */
    @RequestMapping("/myJoin")
    public RespDto<PageInfo<MyJoinDto>> findMyJoin(Integer pageNum, Integer pageSize) throws UserException {
        Long userId = UserUtil.getCurrentUser().getId();
        PaperDto query = new PaperDto();
        query.setUserId(userId);
        PageInfo<Paper> pageInfo = paperService.findByConditionWithPage(pageNum, pageSize, query);
        PageInfo<MyJoinDto> res = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, res);
        List<MyJoinDto> list = Lists.transform(pageInfo.getList(), new Function<Paper, MyJoinDto>() {
            @Override
            public MyJoinDto apply(@Nullable Paper paper) {
                MyJoinDto dto = new MyJoinDto();
                Long sceneId = paper.getSceneId();
                Scene scene = sceneService.findById(sceneId);
                dto.setId(sceneId);
                dto.setScene(scene);
//                dto.setCode(scene.getCode());
//                dto.setName(scene.getName());
                dto.setBeginTime(paper.getBeginTime());
                dto.setEndTime(paper.getEndTime());
                dto.setUserId(userId);
                dto.setScore(paper.getScore());
                dto.setOriginalScore(paper.getOriginalScore());
                dto.setCommonScore(paper.getCommonScore());
                dto.setReceiptScore(paper.getReceiptScore());
//                dto.setSceneStatus(scene.getStatus());
                dto.setPaperStatus(paper.getStatus());
                return dto;
            }
        });
        res.setList(list);
        return RespDto.success(res);
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
    public RespDto<PageInfo<Scene>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Scene scene) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(scene, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<Scene> list = sceneService.findByCondition(pageNum, pageSize, scene);
            PageInfo<Scene> pageInfo = new PageInfo<>(list);
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
     * @param sceneDto
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveScene(@RequestBody SceneDto sceneDto) {
        try {
            if (sceneDto.getReceiptCount() != null) {
                if (null == sceneDto.getNumberLength()) {
                    sceneDto.setNumberLength(numberLength);
                }
                if (null == sceneDto.getDecimalLength()) {
                    sceneDto.setDecimalLength(decimalLength);
                }
            }
            Scene scene = new Scene();
            BeanUtils.copyProperties(sceneDto, scene);
            List<Long> branchIds = sceneDto.getBranchIds();
            List<Long> departmentIds = sceneDto.getDepartmentIds();
            List<Long> stationIds = sceneDto.getStationIds();

            Long paperPolicyId = scene.getPaperPolicyId();
            if (null != paperPolicyId && paperPolicyId > 0) {
                PaperPolicy paperPolicy = paperPolicyService.findById(paperPolicyId);
                if (null == paperPolicy) {
                    logger.warn("不存在的试卷策略， id：{}", paperPolicyId);
                    return RespDto.fail("试卷策略不存在");
                } else {
                    scene.setSingleCount(paperPolicy.getSingleCount());
                    scene.setSingleScore(paperPolicy.getSingleScore());
                    scene.setMultiCount(paperPolicy.getMultiCount());
                    scene.setMultiScore(paperPolicy.getMultiScore());
                    scene.setJudgeCount(paperPolicy.getJudgeCount());
                    scene.setJudgeScore(paperPolicy.getJudgeScore());

//                    以前端传入为主
//                    scene.setReceiptCount(paperPolicy.getReceiptCount());
//                    scene.setNumberLength(paperPolicy.getNumberLength());
                }
            }
            PaperPolicyCheckDto checkDto = null;
            if (paperPolicyId == null || paperPolicyId <= 0) {
                checkDto = paperPolicyAgent.checkSimple(scene.getSingleCount(), scene.getMultiCount(), scene.getJudgeCount(), scene.getQuestionBankId());
            } else {
                checkDto = paperPolicyAgent.checkPolicy(paperPolicyId, scene.getQuestionBankId());

            }

            if (!checkDto.getValid()) {
                logger.warn("题库试题不满足，PaperPolicyCheckDto: {}", JSON.toJSONString(checkDto, true));
                return RespDto.fail("选择的题库中试题数量不足");
            }

            Date now = new Date();
            if (now.after(scene.getOpenTime())) {
                return RespDto.fail("开场时间不能早于当前时间");
            }
            Long sceneId = scene.getId();
            Long closeTimeStamp = scene.getOpenTime().getTime() + (scene.getDuration() + scene.getDelayMinute()) * 60000;
            Date closeTime = new Date(closeTimeStamp);
            scene.setCloseTime(closeTime);
            scene.setStatus(SceneStatusEnum.READY.getCode());
            User user = UserUtil.getCurrentUser();
            scene.setCreateUserId(user.getId());
            scene.setUpdateUserId(user.getId());
            scene.setCode(CodeUtil.getSceneCode());
            if (null == sceneId) {
                logger.debug("保存， saveScene, 参数： {}", JSON.toJSONString(scene, true));
                sceneId = sceneService.add(scene, branchIds, departmentIds, stationIds);
                logger.info("新增 成功， id: {}", sceneId);
            } else {
                Scene obj = sceneService.findById(sceneId);
                if (obj.getStatus() != SceneStatusEnum.READY.getCode()) {
                    return RespDto.fail("本场考试已经开场或取消，不能修改");
                }
                sceneService.updateById(scene, branchIds, departmentIds, stationIds);
                logger.debug("更新成功", JSON.toJSONString(scene, true));
            }
            if (null != scene.getReceiptCount()) {
                if (null == scene.getNumberLength()) {
                    scene.setNumberLength(10);
                }
                Preconditions.checkArgument(null != scene.getReceiptScore(), "凭条分数不能为空");
                receiptService.save(sceneId, scene.getReceiptCount(), scene.getNumberLength());
            }
            return RespDto.success(sceneId != null);
        } catch (Exception e) {
            logger.error("保存 scene 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.scene.domain.Scene>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findById")
    public RespDto<SceneDto> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            SceneDto dto = sceneAgent.findById(id);
            return RespDto.success(dto);
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
            int count = sceneService.deleteById(id, operatorId);
            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取相关场次的翻打凭条信息
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/4/26 14:19
     */
    @RequestMapping(value = "/getReceiptNumberList", method = RequestMethod.GET)
    public RespDto<List<Long>> getReceiptNumberList(Long sceneId) {
        logger.info("getReceiptNumber, sceneId: {}", sceneId);
        Receipt query = new Receipt();
        query.setIsDel(DelFlagEnum.NO.getCode());
        query.setSceneId(sceneId);
        List<Receipt> receipts = receiptService.findByCondition(query);
        if (CollectionUtils.isEmpty(receipts)) {
            logger.info("sceneId： {}没有找到凭条信息", sceneId);
            return RespDto.success(new ArrayList<>());
        }
        List<Long> numbers = Lists.transform(receipts, item -> item.getNumber());
        return RespDto.success(numbers);
    }


    /**
     * 校验授权码
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/4/28 15:11
     */
    @RequestMapping(value = "/checkAuthCode", method = RequestMethod.POST)
    public RespDto<Boolean> checkAuthCode(String authCode, Long sceneId) {
        logger.info("开始交验验证码， sceneId: {}, authCode: {}", sceneId, authCode);
        Scene scene = sceneService.findById(sceneId);
        if (scene.getAuthCode().equals(authCode)) {
            return RespDto.success(true);
        } else {
            return RespDto.fail("授权码错误");
        }
    }


    /**
     * 查询考试中的场次
     * 管理员专用，开场教师从自己的开场列表中查看
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/11 10:30
     */
    @RequestMapping(value = "/findSceneInExaming")
    public RespDto<PageInfo<SceneMonitorDto>> findSceneInExaming(Integer pageNum, Integer pageSize) throws UserException {
        Scene query = new Scene();
        query.setStatus(SceneStatusEnum.ON.getCode());
        query.setIsDel(DelFlagEnum.NO.getCode());

        PageInfo<Scene> scenePageInfo = sceneService.findByConditionWithPage(pageNum, pageSize, query);
        PageInfo<SceneMonitorDto> monitorDtoPageInfo = new PageInfo<SceneMonitorDto>();
        BeanUtils.copyProperties(scenePageInfo, monitorDtoPageInfo);

        if (scenePageInfo.getSize() > 0) {
            List<SceneMonitorDto> dtos = Lists.transform(scenePageInfo.getList(), item -> {
                SceneMonitorDto dto = new SceneMonitorDto();
                BeanUtils.copyProperties(item, dto);
                Long sceneId = item.getId();
                SceneMonitorDto dtoTmp = paperService.getSceneMonitor(sceneId);
                dto.setPaperCount(dtoTmp.getPaperCount());
                dto.setPaperCommitCount(dtoTmp.getPaperCommitCount());
                dto.setPaperNoneCommitCount(dtoTmp.getPaperNoneCommitCount());
                dto.setCommitRate(dtoTmp.getCommitRate());
                return dto;
            });
            monitorDtoPageInfo.setList(dtos);
        }
        return RespDto.success(monitorDtoPageInfo);
    }
}

