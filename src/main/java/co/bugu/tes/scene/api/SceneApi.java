package co.bugu.tes.scene.api;

import co.bugu.common.RespDto;
import co.bugu.common.enums.DelFlagEnum;
import co.bugu.exception.UserException;
import co.bugu.tes.paper.domain.Paper;
import co.bugu.tes.paper.service.IPaperService;
import co.bugu.tes.questionBank.domain.QuestionBank;
import co.bugu.tes.questionBank.service.IQuestionBankService;
import co.bugu.tes.scene.agent.SceneAgent;
import co.bugu.tes.scene.domain.Scene;
import co.bugu.tes.scene.dto.MyJoinDto;
import co.bugu.tes.scene.dto.MyOpenDto;
import co.bugu.tes.scene.dto.SceneDto;
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
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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


    @RequestMapping("/myOpen")
    public RespDto<PageInfo<MyOpenDto>> findMyOpen(Integer pageNum, Integer pageSize, Integer status) {
        try {
            User user = UserUtil.getCurrentUser();
            Long userId = user.getId();
            Scene query = new Scene();
            query.setStatus(status);
            query.setCreateUserId(userId);
            query.setIsDel(DelFlagEnum.NO.getCode());
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
//                    todo 策略名称待处理，当前暂时未开通策略模式

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
        Paper query = new Paper();
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
            Scene scene = new Scene();
            BeanUtils.copyProperties(sceneDto, scene);
            List<Long> branchIds = sceneDto.getBranchIds();
            List<Long> departmentIds = sceneDto.getDepartmentIds();
            List<Long> stationIds = sceneDto.getStationIds();


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
}

