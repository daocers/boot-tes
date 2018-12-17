package co.bugu.tes.scene.service;

import co.bugu.exception.UserException;
import co.bugu.tes.scene.domain.Scene;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-11-20 17:15
 */
public interface ISceneService {

    /**
     * 新增
     *
     * @param scene
     * @return
     */
    long add(Scene scene);

    /**
     * 保存场次信息，并添加对应的考生范围
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/12/17 10:06
     */
    long add(Scene scene, List<Long> branchIds, List<Long> departmentIds, List<Long> stationIds) throws UserException;

    /**
     * 更新场次信息，并更新对应的考生范围
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/12/17 10:06
     */
    long updateById(Scene scene, List<Long> branchIds, List<Long> departmentIds, List<Long> stationIds) throws UserException;

    /**
     * 通过id更新
     *
     * @param scene
     * @return
     */
    int updateById(Scene scene);

    /**
     * 条件查询
     *
     * @param scene
     * @return
     */
    List<Scene> findByCondition(Scene scene);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param scene    查询条件
     * @return
     */
    List<Scene> findByCondition(Integer pageNum, Integer pageSize, Scene scene);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param scene    查询条件
     * @return
     */
    PageInfo<Scene> findByConditionWithPage(Integer pageNum, Integer pageSize, Scene scene);

    /**
     * 通过id查询
     *
     * @param sceneId
     * @return
     */
    Scene findById(Long sceneId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param sceneId
     * @return
     */
    int deleteById(Long sceneId, Long operatorId);

}
