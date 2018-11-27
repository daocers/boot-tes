package co.bugu.tes.judge.dao;

import co.bugu.common.dao.BaseDao;
import co.bugu.tes.answer.dto.AnswerDto4GenPaper;
import co.bugu.tes.judge.domain.Judge;

import java.util.List;

public interface JudgeDao extends BaseDao<Judge>{
    void batchAdd(List<Judge> judges);

    List<AnswerDto4GenPaper> getAllIds(Judge judge);
}
