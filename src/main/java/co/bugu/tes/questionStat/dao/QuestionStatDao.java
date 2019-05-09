package co.bugu.tes.questionStat.dao;

import co.bugu.common.dao.BaseDao;
import co.bugu.tes.questionStat.domain.QuestionStat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionStatDao extends BaseDao<QuestionStat> {

    /**
     * 获取答案统计
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/9 11:00
     */
    List<QuestionStat> getAnswerStat(@Param("lastAnswerId") Long lastAnswerId, @Param("endAnswerId") Long endAnswerId);
}
