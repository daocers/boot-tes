package co.bugu.tes.multi.dao;

import co.bugu.common.dao.BaseDao;
import co.bugu.tes.answer.dto.AnswerDto4GenPaper;
import co.bugu.tes.multi.domain.Multi;

import java.util.List;

public interface MultiDao extends BaseDao<Multi>{
    void batchAdd(List<Multi> multis);

    List<AnswerDto4GenPaper> getAllIds(Multi multi);
}
