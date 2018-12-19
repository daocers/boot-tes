package co.bugu.tes.joinInfo.dao;

import co.bugu.common.dao.BaseDao;
import co.bugu.tes.joinInfo.domain.JoinInfo;
import co.bugu.tes.joinInfo.dto.JoinInfoQueryDto;

import java.util.List;

public interface JoinInfoDao extends BaseDao<JoinInfo> {
    void batchAdd(List<JoinInfo> list);

    int deleteBySceneId(Long sceneId);

    /**
     * 查询，查看哪些人能参加
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/12/19 16:01
     */
    List<JoinInfo> findByUserInfo(JoinInfoQueryDto queryDto);
}
