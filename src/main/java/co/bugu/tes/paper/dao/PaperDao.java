package co.bugu.tes.paper.dao;

import co.bugu.common.dao.BaseDao;
import co.bugu.tes.paper.domain.Paper;

import java.util.List;

public interface PaperDao extends BaseDao<Paper>{
    /**
     * 考试成绩统计，只取score和sceneId字段
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/9 15:56
     */
    List<Paper> getSceneScoreStat(int size);
}
