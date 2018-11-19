package co.bugu.common.dao;

import java.util.List;

/**
* @author daocers
* @date 2018-11-19 15:26
*/
public interface BaseDao<T> {
    /**
    * 通过主键删除
    * @param id
    * @return
    */
    int deleteById(Long id);

    /**
    * insert，如果字段为空，略去空字段
    * @param record
    * @return
    */
    int insert(T record);

    /**
    * 通过主键查询
    * @param id
    * @return
    */
    T selectById(Long id);

    /**
    * 通过主键更新单条数据
    * @param record
    * @return
    */
    int updateById(T record);

    /**
    * 通过条件查询
    * @param record
    * @return
    */
    List<T> findByObject(T record);

}
