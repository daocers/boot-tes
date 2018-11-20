package co.bugu.tes.branch.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.branch.dao.BranchDao;
import co.bugu.tes.branch.domain.Branch;
import co.bugu.tes.branch.service.IBranchService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author daocers
 * @create 2018-11-20 17:15
 */
@Service
public class BranchServiceImpl implements IBranchService {
    @Autowired
    BranchDao branchDao;

    private Logger logger = LoggerFactory.getLogger(BranchServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Override
    public long add(Branch branch) {
        //todo 校验参数
        branch.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        branch.setCreateTime(now);
        branch.setUpdateTime(now);
        branchDao.insert(branch);
        return branch.getId();
    }

    @Override
    public int updateById(Branch branch) {
        logger.debug("branch updateById, 参数： {}", JSON.toJSONString(branch, true));
        Preconditions.checkNotNull(branch.getId(), "id不能为空");
        branch.setUpdateTime(new Date());
        return branchDao.updateById(branch);
    }

    @Override
    public List<Branch> findByCondition(Branch branch) {
        logger.debug("branch findByCondition, 参数： {}", JSON.toJSONString(branch, true));
        PageHelper.orderBy(ORDER_BY);
        List<Branch> branchs = branchDao.findByObject(branch);

        logger.debug("查询结果， {}", JSON.toJSONString(branchs, true));
        return branchs;
    }

    @Override
    public List<Branch> findByCondition(Integer pageNum, Integer pageSize, Branch branch) {
        logger.debug("branch findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(branch, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Branch> branchs = branchDao.findByObject(branch);

        logger.debug("查询结果， {}", JSON.toJSONString(branchs, true));
        return branchs;
    }

    @Override
    public PageInfo<Branch> findByConditionWithPage(Integer pageNum, Integer pageSize, Branch branch) {
        logger.debug("branch findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(branch, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Branch> branchs = branchDao.findByObject(branch);

        logger.debug("查询结果， {}", JSON.toJSONString(branchs, true));
        return new PageInfo<>(branchs);
    }

    @Override
    public Branch findById(Long branchId) {
        logger.debug("branch findById, 参数 branchId: {}", branchId);
        Branch branch = branchDao.selectById(branchId);

        logger.debug("查询结果： {}", JSON.toJSONString(branch, true));
        return branch;
    }

    @Override
    public int deleteById(Long branchId, Long operatorId) {
        logger.debug("branch 删除， 参数 branchId : {}", branchId);
        Branch branch = new Branch();
        branch.setId(branchId);
        branch.setIsDel(DelFlagEnum.YES.getCode());
        branch.setUpdateTime(new Date());
        branch.setUpdateUserId(operatorId);
        int num = branchDao.updateById(branch);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
