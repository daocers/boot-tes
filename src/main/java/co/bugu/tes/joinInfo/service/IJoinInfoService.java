package co.bugu.tes.joinInfo.service;

import co.bugu.tes.joinInfo.domain.JoinInfo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-12-16 23:21
 */
public interface IJoinInfoService {

    /**
     * 新增
     *
     * @param joinInfo
     * @return
     */
    long add(JoinInfo joinInfo);

    /**
     * 通过id更新
     *
     * @param joinInfo
     * @return
     */
    int updateById(JoinInfo joinInfo);

    /**
     * 条件查询
     *
     * @param joinInfo
     * @return
     */
    List<JoinInfo> findByCondition(JoinInfo joinInfo);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param joinInfo     查询条件
     * @return
     */
    List<JoinInfo> findByCondition(Integer pageNum, Integer pageSize, JoinInfo joinInfo);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param joinInfo     查询条件
     * @return
     */
    PageInfo<JoinInfo> findByConditionWithPage(Integer pageNum, Integer pageSize, JoinInfo joinInfo);

    /**
     * 通过id查询
     *
     * @param joinInfoId
     * @return
     */
    JoinInfo findById(Long joinInfoId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param joinInfoId
     * @return
     */
    int deleteById(Long joinInfoId, Long operatorId);

}
