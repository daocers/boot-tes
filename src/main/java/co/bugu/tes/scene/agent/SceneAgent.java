package co.bugu.tes.scene.agent;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.branch.domain.Branch;
import co.bugu.tes.branch.service.IBranchService;
import co.bugu.tes.joinInfo.domain.JoinInfo;
import co.bugu.tes.joinInfo.dto.JoinInfoQueryDto;
import co.bugu.tes.joinInfo.service.IJoinInfoService;
import co.bugu.tes.manager.enums.ManagerTypeEnum;
import co.bugu.tes.manager.service.IManagerService;
import co.bugu.tes.paper.agent.PaperAgent;
import co.bugu.tes.paper.domain.Paper;
import co.bugu.tes.paper.enums.PaperStatusEnum;
import co.bugu.tes.paper.service.IPaperService;
import co.bugu.tes.receipt.service.IReceiptService;
import co.bugu.tes.scene.domain.Scene;
import co.bugu.tes.scene.dto.SceneDto;
import co.bugu.tes.scene.enums.SceneStatusEnum;
import co.bugu.tes.scene.service.ISceneService;
import co.bugu.tes.user.domain.User;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author daocers
 * @Date 2018/11/28:11:57
 * @Description:
 */
@Service
public class SceneAgent {
    private Logger logger = LoggerFactory.getLogger(SceneAgent.class);

    @Value("${bugu.init.auto-close-scene-delay}")
    Integer autoCloseSceneDelay;

    @Autowired
    ISceneService sceneService;
    @Autowired
    IPaperService paperService;
    @Autowired
    PaperAgent paperAgent;
    @Autowired
    IManagerService managerService;
    @Autowired
    IJoinInfoService joinInfoService;
    @Autowired
    IBranchService branchService;
    @Autowired
    IReceiptService receiptService;


    public SceneDto findById(Long sceneId) {
        Scene scene = sceneService.findById(sceneId);
        if (null == scene) {
            return null;
        }
        SceneDto dto = new SceneDto();
        BeanUtils.copyProperties(scene, dto);

        JoinInfo query = new JoinInfo();
        query.setIsDel(DelFlagEnum.NO.getCode());
        query.setSceneId(sceneId);
        List<JoinInfo> list = joinInfoService.findByCondition(query);
        List<Long> branchIds = new ArrayList<>();
        List<Long> departmentIds = new ArrayList<>();
        List<Long> stationIds = new ArrayList<>();
        for (JoinInfo info : list) {
            if (info.getType() == ManagerTypeEnum.DEPARTMENT.getCode()) {
                departmentIds.add(info.getTargetId());
            } else if (info.getType() == ManagerTypeEnum.BRANCH.getCode()) {
                branchIds.add(info.getTargetId());
            } else {
                stationIds.add(info.getTargetId());
            }
        }
        dto.setBranchIds(branchIds);
        dto.setDepartmentIds(departmentIds);
        dto.setStationIds(stationIds);
        return dto;

    }

    /**
     * 封场
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/28 14:14
     */
    public boolean closeAllScene() throws Exception {
        Scene query = new Scene();
        query.setStatus(SceneStatusEnum.ON.getCode());
        query.setIsDel(DelFlagEnum.NO.getCode());
        List<Scene> scenes = sceneService.findByCondition(query);
        if (CollectionUtils.isNotEmpty(scenes)) {
            Date now = new Date();
            for (Scene scene : scenes) {
//                正在进行的考试，结束3分钟之后，自动封场,时间可配置
                if (autoCloseSceneDelay == null) {
                    autoCloseSceneDelay = 3;
                }
                if (now.getTime() > scene.getCloseTime().getTime() + autoCloseSceneDelay * 60000 && scene.getStatus() == SceneStatusEnum.ON.getCode()) {
                    closeScene(scene);
                }
            }
        }
        return true;
    }

    /**
     * 封场 记录得分，更新场次状态
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/28 11:58
     */
    public boolean closeScene(Scene scene) throws Exception {
        Long sceneId = scene.getId();

//        获取已经提交 状态的试卷信息
        Paper query = new Paper();
        query.setSceneId(sceneId);
        query.setIsDel(DelFlagEnum.NO.getCode());
        query.setStatus(PaperStatusEnum.COMMITED.getCode());
        List<Paper> papers = paperService.findByCondition(query);
        if (CollectionUtils.isNotEmpty(papers)) {
            for (Paper paper : papers) {
                if (paper.getStatus() == PaperStatusEnum.COMMITED.getCode()) {
                    Long paperId = paper.getId();
                    double score = paperAgent.computeScore(scene, paper);
                    logger.info("得分， {}， 试卷id: {}", new Object[]{score, paperId});
                }
            }
        }
//        计算完得分，更新场次信息
        scene.setStatus(SceneStatusEnum.CLOSED.getCode());
        scene.setUpdateTime(new Date());
        sceneService.updateById(scene);
        return true;
    }

    /**
     * 修改场次状态
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/28 14:28
     */
    public void changeReadyToOn() {
        Scene query = new Scene();
        query.setIsDel(DelFlagEnum.NO.getCode());
        query.setStatus(SceneStatusEnum.READY.getCode());

        List<Scene> scenes = sceneService.findByCondition(query);
        if (CollectionUtils.isNotEmpty(scenes)) {
            Date now = new Date();
            for (Scene scene : scenes) {

//                开场时间早于当前时间 且 当前时间早于结束时间，设置为开场状态
                if (scene.getOpenTime().before(now) && now.before(scene.getCloseTime())) {
                    scene.setUpdateTime(now);
                    scene.setStatus(SceneStatusEnum.ON.getCode());
                    sceneService.updateById(scene);
                }
            }
        }
    }


    /**
     * 为考试用户查找就绪的场次
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/12/19 16:18
     */
    public PageInfo<Scene> findReadySceneForUser(Integer pageNum, Integer pageSize, User user, Date beginDate, Date endDate) {
        if (user == null) {
            return new PageInfo<>();
        }
        Long branchId = user.getBranchId();
        Long departmentId = user.getDepartmentId();
        Long stationId = user.getStationId();

        JoinInfoQueryDto query = new JoinInfoQueryDto();
        query.setBeginDate(beginDate);
        query.setEndDate(endDate);
        query.setIsDel(DelFlagEnum.NO.getCode());


//        List<JoinInfo> infos = new ArrayList<>();
        Set<Long> sceneIds = new TreeSet<>();
        if (departmentId != null && departmentId > 0) {
            query.setTargetId(departmentId);
            query.setType(ManagerTypeEnum.DEPARTMENT.getCode());
            List<JoinInfo> list = joinInfoService.findByUserInfo(query);
//            infos.addAll(list);
            sceneIds.addAll(Lists.transform(list, item -> item.getSceneId()));
        }

        if (stationId != null && stationId > 0) {
            query.setTargetId(stationId);
            query.setType(ManagerTypeEnum.STATION.getCode());
            List<JoinInfo> list = joinInfoService.findByUserInfo(query);
            sceneIds.addAll(Lists.transform(list, item -> item.getSceneId()));
        }

        if (branchId != null && branchId > 0) {
            Branch branch = branchService.findById(branchId);
            String likeCode = branch.getCode() + "%";
            query.setTargetCode(likeCode);
            query.setTargetId(null);
            query.setType(ManagerTypeEnum.BRANCH.getCode());
            List<JoinInfo> list = joinInfoService.findByUserInfo(query);
            sceneIds.addAll(Lists.transform(list, item -> item.getSceneId()));
        }


//        todo 查找通用的，只输入授权码就可以登录的考试
        query = new JoinInfoQueryDto();
        query.setTargetId(-1L);
        query.setType(-1);
        List<JoinInfo> listOfAuthCode = joinInfoService.findByUserInfo(query);
        if (CollectionUtils.isNotEmpty(listOfAuthCode)) {
            sceneIds.addAll(Lists.transform(listOfAuthCode, item -> item.getSceneId()));
        }

        List<Scene> scenes = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(sceneIds)) {
            for (Long sceneId : sceneIds) {
                Scene scene = sceneService.findById(sceneId);
                if (null != scene) {
                    scenes.add(scene);
                }
            }
        }

        PageInfo<Scene> pageInfo = new PageInfo<>();
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        int index = (pageNum - 1) * pageSize;
        if (scenes.size() <= index) {
            pageInfo.setList(new ArrayList<>());
        } else {
            int toIndex = pageNum * pageSize;
            if (scenes.size() < toIndex) {
                toIndex = scenes.size();
            }
            List<Scene> list = scenes.subList(index, toIndex);
            pageInfo.setList(list);
        }
        return pageInfo;
    }
}
