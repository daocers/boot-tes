package co.bugu.tes.joinInfo.dao;

import co.bugu.common.dao.BaseDao;
import co.bugu.tes.joinInfo.domain.JoinInfo;

import java.util.List;

public interface JoinInfoDao extends BaseDao<JoinInfo>{
    void batchAdd(List<JoinInfo> list);

    int deleteBySceneId(Long sceneId);
}
