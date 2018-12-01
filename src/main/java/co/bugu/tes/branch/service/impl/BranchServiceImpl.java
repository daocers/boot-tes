package co.bugu.tes.branch.service.impl;

import co.bugu.common.enums.BaseStatusEnum;
import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.branch.dao.BranchDao;
import co.bugu.tes.branch.domain.Branch;
import co.bugu.tes.branch.dto.BranchTreeDto;
import co.bugu.tes.branch.service.IBranchService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author daocers
 * @create 2018-11-20 17:15
 */
@Service
public class BranchServiceImpl implements IBranchService {
    @Autowired
    BranchDao branchDao;

    private Logger logger = LoggerFactory.getLogger(BranchServiceImpl.class);

    private static String ORDER_BY = "update_time";

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


    /**
     * 需要根据实际情况处理，暂时未完善
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/12/1 14:13
     */
    @Override
    public List<Branch> batchAdd(List<List<String>> data, Long userId) throws Exception {
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        data.remove(0);
        Set<String> names = new HashSet<>();
        boolean rootFlag = false;
        int idx = 1;
        for (List<String> list : data) {
            String name = list.get(0);
            if (names.contains(name)) {
                throw new Exception("机构名称重复,行号：" + idx);
            }
            String superiorName = list.get(3);
            if (StringUtils.isEmpty(superiorName)) {
                if (!rootFlag) {
                    rootFlag = true;
                } else {
                    throw new Exception("上级机构名称未指定，行号：" + idx);
                }
            }
            idx++;
        }
        idx = 1;
//        todo 需要根据实际情况处理
        List<Branch> branches = new ArrayList<>();
        for (List<String> list : data) {
            Branch branch = new Branch();
            branch.setStatus(BaseStatusEnum.ENABLE.getCode());
            branch.setName(list.get(0));
            branch.setAddress(list.get(1));
            branch.setCode(list.get(2));
            String superiorName = list.get(3);
            if (!names.contains(superiorName)) {
                throw new Exception("上级名称不存在, 行号：" + idx);
            }

        }
        return null;
    }

    @Override
    public void saveTree(List<BranchTreeDto> list, Long userId) {
        saveInRecursion(userId, list);
    }

    private void saveInRecursion(Long userId, List<BranchTreeDto> dtos){
        int idx = 1;
        for(BranchTreeDto dto: dtos){
            Branch branch = new Branch();
            branch.setSuperiorId(dto.getSuperiorId());
            branch.setId(dto.getId());
            branch.setUpdateUserId(userId);
            branch.setLevel(dto.getLevel());
            branchDao.updateById(branch);
            if(CollectionUtils.isNotEmpty(dto.getChildren())){
                saveInRecursion(userId, dto.getChildren());
            }
        }

    }

}
