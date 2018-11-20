package co.bugu.tes.branch.api;

import co.bugu.common.RespDto;
import co.bugu.tes.branch.domain.Branch;
import co.bugu.tes.branch.service.IBranchService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据api
 *
 * @author daocers
 * @create 2018-11-20 17:15
 */
@RestController
@RequestMapping("/branch/api")
public class BranchApi {
    private Logger logger = LoggerFactory.getLogger(BranchApi.class);

    @Autowired
    IBranchService branchService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<Branch>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Branch branch) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(branch, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<Branch> list = branchService.findByCondition(pageNum, pageSize, branch);
            PageInfo<Branch> pageInfo = new PageInfo<>(list);
            logger.info("查询到数据： {}", JSON.toJSONString(pageInfo, true));
            return RespDto.success(pageInfo);
        } catch (Exception e) {
            logger.error("findByCondition  失败", e);
            return RespDto.fail();
        }
    }

    /**
     * 保存
     *
     * @param branch
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveBranch(@RequestBody Branch branch) {
        try {
            Long branchId = branch.getId();
            if(null == branchId){
                logger.debug("保存， saveBranch, 参数： {}", JSON.toJSONString(branch, true));
                branchId = branchService.add(branch);
                logger.info("新增 成功， id: {}", branchId);
            }else{
                branchService.updateById(branch);
                logger.debug("更新成功", JSON.toJSONString(branch, true));
            }
            return RespDto.success(branchId != null);
        } catch (Exception e) {
            logger.error("保存 branch 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.branch.domain.Branch>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findById")
    public RespDto<Branch> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            Branch branch = branchService.findById(id);
            return RespDto.success(branch);
        } catch (Exception e) {
            logger.error("获取详情失败", e);
            return RespDto.fail();
        }
    }

    /**
     * 删除，软删除，更新数据库删除标志
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id, Long operatorId) {
        try {
            logger.debug("准备删除， 参数: {}", id);
            Preconditions.checkArgument(id != null, "id不能为空");
            int count = branchService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

