package co.bugu.tes.paperPolicyCondition.dao;

import co.bugu.common.dao.BaseDao;
import co.bugu.tes.paperPolicyCondition.domain.PaperPolicyCondition;

import java.util.List;

public interface PaperPolicyConditionDao extends BaseDao<PaperPolicyCondition> {
    /**
     * 批量添加
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/7 10:57
     */
    void batchAdd(List<PaperPolicyCondition> conditionList);

    /**
     * 删除全部，硬删除
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/7 11:11
     */
    int deleteAll();
}
