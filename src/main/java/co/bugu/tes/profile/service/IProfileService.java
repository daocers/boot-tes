package co.bugu.tes.profile.service;

import co.bugu.tes.profile.domain.Profile;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-11-19 19:29
 */
public interface IProfileService {

    /**
     * 新增
     *
     * @param profile
     * @return
     */
    long add(Profile profile);

    /**
     * 通过id更新
     *
     * @param profile
     * @return
     */
    int updateById(Profile profile);

    /**
     * 条件查询
     *
     * @param profile
     * @return
     */
    List<Profile> findByCondition(Profile profile);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param profile     查询条件
     * @return
     */
    List<Profile> findByCondition(Integer pageNum, Integer pageSize, Profile profile);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param profile     查询条件
     * @return
     */
    PageInfo<Profile> findByConditionWithPage(Integer pageNum, Integer pageSize, Profile profile);

    /**
     * 通过id查询
     *
     * @param profileId
     * @return
     */
    Profile findById(Long profileId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param profileId
     * @return
     */
    int deleteById(Long profileId, Long operatorId);

}
