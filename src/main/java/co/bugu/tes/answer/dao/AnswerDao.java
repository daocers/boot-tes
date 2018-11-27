package co.bugu.tes.answer.dao;

import co.bugu.common.dao.BaseDao;
import co.bugu.tes.answer.domain.Answer;

import java.util.List;

public interface AnswerDao extends BaseDao<Answer>{
    /**
     * 批量添加试卷的答案信息
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/26 11:10
     */
    void batchAdd(List<Answer> answers);
}
