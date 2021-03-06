package co.bugu.tes.department.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.department.dao.DepartmentDao;
import co.bugu.tes.department.domain.Department;
import co.bugu.tes.department.service.IDepartmentService;
import co.bugu.tes.manager.service.IManagerService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author daocers
 * @create 2018-11-20 17:15
 */
@Service
public class DepartmentServiceImpl implements IDepartmentService {
    @Autowired
    DepartmentDao departmentDao;
    @Autowired
    IManagerService managerService;

    private Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private static String ORDER_BY = "id desc";


    private Cache<Long, Department> departmentCache =
            CacheBuilder.newBuilder()
                    .maximumSize(100)
                    .concurrencyLevel(3)
                    .expireAfterWrite(1, TimeUnit.HOURS)
                    .build();

    @Override
    public long add(Department department) {
        //todo 校验参数
        department.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        department.setCreateTime(now);
        department.setUpdateTime(now);
        departmentDao.insert(department);
        return department.getId();
    }

    @Override
    public int updateById(Department department) {
        logger.debug("department updateById, 参数： {}", JSON.toJSONString(department, true));
        Preconditions.checkNotNull(department.getId(), "id不能为空");
        departmentCache.invalidate(department.getId());
        department.setUpdateTime(new Date());
        return departmentDao.updateById(department);
    }

    @Override
    public List<Department> findByCondition(Department department) {
        logger.debug("department findByCondition, 参数： {}", JSON.toJSONString(department, true));
        PageHelper.orderBy(ORDER_BY);
        List<Department> departments = departmentDao.findByObject(department);

        logger.debug("查询结果， {}", JSON.toJSONString(departments, true));
        return departments;
    }

    @Override
    public List<Department> findByCondition(Integer pageNum, Integer pageSize, Department department) {
        logger.debug("department findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(department, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Department> departments = departmentDao.findByObject(department);

        logger.debug("查询结果， {}", JSON.toJSONString(departments, true));
        return departments;
    }

    @Override
    public PageInfo<Department> findByConditionWithPage(Integer pageNum, Integer pageSize, Department department) {
        logger.debug("department findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(department, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Department> departments = departmentDao.findByObject(department);

        logger.debug("查询结果， {}", JSON.toJSONString(departments, true));
        return new PageInfo<>(departments);
    }

    @Override
    public Department findById(Long departmentId) {
        logger.debug("department findById, 参数 departmentId: {}", departmentId);
        Department department = departmentCache.getIfPresent(departmentId);
        if (department != null) {
            return department;
        }

        department = departmentDao.selectById(departmentId);
        if(null == department){
            department = new Department();
        }
        departmentCache.put(departmentId, department);

        logger.debug("查询结果： {}", JSON.toJSONString(department, true));
        return department;
    }

    @Override
    public int deleteById(Long departmentId, Long operatorId) {
        logger.debug("department 删除， 参数 departmentId : {}", departmentId);
        departmentCache.invalidate(departmentId);
        Department department = new Department();
        department.setId(departmentId);
        department.setIsDel(DelFlagEnum.YES.getCode());
        department.setUpdateTime(new Date());
        department.setUpdateUserId(operatorId);
        int num = departmentDao.updateById(department);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }


}
