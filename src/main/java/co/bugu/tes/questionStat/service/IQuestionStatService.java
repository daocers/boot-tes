package co.bugu.tes.questionStat.service;

import co.bugu.tes.questionStat.domain.QuestionStat;
import com.github.pagehelper.PageInfo;
import java.util.List;

/**
* service接口
*
* @author daocers
* @create 2019-05-09 10:49
*/
public interface IQuestionStatService {

    /**
    * 新增
    * @param questionStat
    * @return
    */
    long add(QuestionStat questionStat);

    /**
    * 通过id更新
    * @param questionStat
    * @return
    */
    int updateById(QuestionStat questionStat);

    /**
    * 条件查询
    * @param questionStat
    * @return
    */
    List<QuestionStat> findByCondition(QuestionStat questionStat);

    /**
    * 条件查询 分页
    * @param pageNum 页码，从1 开始
    * @param pageSize 每页多少条
    * @param questionStat 查询条件
    * @return
    */
    List<QuestionStat> findByCondition(Integer pageNum, Integer pageSize, QuestionStat questionStat);

    /**
    * 条件查询 分页
    * @param pageNum 页码，从1 开始
    * @param pageSize 每页多少条
    * @param questionStat 查询条件
    * @return
    */
    PageInfo<QuestionStat> findByConditionWithPage(Integer pageNum, Integer pageSize, QuestionStat questionStat);

    /**
    * 通过id查询
    * @param questionStatId
    * @return
    */
    QuestionStat findById(Long questionStatId);

    /**
    * 删除指定id的记录 软删除，设置删除标志
    * @param questionStatId
    * @return
    */
    int deleteById(Long questionStatId, Long operatorId);


    /**
     * 获取答案 并处理统计数据
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/9 11:02
     */
    boolean getAnswerAndProcessStat(int size);

}
