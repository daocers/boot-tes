package co.bugu.tes.paper.service;

import co.bugu.tes.answer.dto.AnswerDto4GenPaper;
import co.bugu.tes.paper.domain.Paper;
import co.bugu.tes.scene.dto.SceneMonitorDto;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-11-20 17:15
 */
public interface IPaperService {

    /**
     * 新增
     *
     * @param paper
     * @return
     */
    long add(Paper paper);

    /**
     * 通过id更新
     *
     * @param paper
     * @return
     */
    int updateById(Paper paper);

    /**
     * 条件查询
     *
     * @param paper
     * @return
     */
    List<Paper> findByCondition(Paper paper);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param paper     查询条件
     * @return
     */
    List<Paper> findByCondition(Integer pageNum, Integer pageSize, Paper paper);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param paper     查询条件
     * @return
     */
    PageInfo<Paper> findByConditionWithPage(Integer pageNum, Integer pageSize, Paper paper);

    /**
     * 通过id查询
     *
     * @param paperId
     * @return
     */
    Paper findById(Long paperId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param paperId
     * @return
     */
    int deleteById(Long paperId, Long operatorId);

    /**
     * 
     *
     * @param 
     * @return 
     * @auther daocers
     * @date 2018/11/25 22:11
     */
    Long createPaper(Long sceneId, Long userId, List<AnswerDto4GenPaper> sIds, List<AnswerDto4GenPaper> mIds, List<AnswerDto4GenPaper> jIds);


    /**
     * 考试成绩统计，只取score和sceneId字段
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/9 15:56
     */
    List<Paper> getSceneScoreStat(int size);


    /**
     * 获取场次监控信息
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/11 12:21
     */
    SceneMonitorDto getSceneMonitor(Long sceneId);
}
