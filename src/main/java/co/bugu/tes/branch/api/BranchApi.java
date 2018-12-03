package co.bugu.tes.branch.api;

import co.bugu.common.RespDto;
import co.bugu.common.enums.BaseStatusEnum;
import co.bugu.exception.UserException;
import co.bugu.tes.branch.agent.BranchAgent;
import co.bugu.tes.branch.domain.Branch;
import co.bugu.tes.branch.dto.BranchTreeDto;
import co.bugu.tes.branch.service.IBranchService;
import co.bugu.tes.single.api.SingleApi;
import co.bugu.util.ExcelUtil;
import co.bugu.util.UserUtil;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
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

    @Autowired
    BranchAgent branchAgent;


    /***
     * 获取机构树 数据
     * @Time 2017/11/25 17:53
     * @Author daocers
     * @return co.bugu.common.RespDto<java.util.List   <   co.bugu.tes.branch.BranchTreeVo>>
     */
    @RequestMapping(value = "/getBranchTree")
    public RespDto<List<BranchTreeDto>> getBranchTree() {
        try {
            List<BranchTreeDto> treeDtos = branchAgent.getBranchTree();
            return RespDto.success(treeDtos);
        } catch (Exception e) {
            logger.error("获取机构树失败", e);
            return RespDto.fail();
        }
    }

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
    public RespDto<Long> saveBranch(@RequestBody Branch branch) {
        try {
            Long userId = UserUtil.getCurrentUser().getId();
            Long branchId = branch.getId();
            branch.setUpdateUserId(userId);
            if (branch.getStatus() == null) {
                branch.setStatus(BaseStatusEnum.ENABLE.getCode());
            }
            if (null == branchId) {
                branch.setCreateUserId(userId);
                logger.debug("保存， saveBranch, 参数： {}", JSON.toJSONString(branch, true));
                branchId = branchService.add(branch);
                logger.info("新增 成功， id: {}", branchId);
            } else {
                branchService.updateById(branch);
                logger.debug("更新成功", JSON.toJSONString(branch, true));
            }
            return RespDto.success(branchId);
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

    /**
     * 保存树结构1
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/12/1 15:21
     */
    @RequestMapping(value = "/saveTree", method = RequestMethod.POST)
    public RespDto<Boolean> saveTree(@RequestBody List<BranchTreeDto> list) throws UserException {
        Long userId = UserUtil.getCurrentUser().getId();
        branchService.saveTree(list, userId);
        return RespDto.success(true);
    }

    /**
     * 下载多选题模板
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/21 9:56
     */
    @RequestMapping(value = "/downloadModel")
    public void downloadModel(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(("机构信息模板.xlsx").getBytes(), "iso-8859-1"));
            OutputStream os = response.getOutputStream();
            String rootPath = request.getServletContext().getRealPath("/");

            InputStream is = new BufferedInputStream(SingleApi.class.getClassLoader().getResourceAsStream("models/机构信息模板.xlsx"));
            byte[] buffer = new byte[1024];

            while (is.read(buffer) != -1) {
                os.write(buffer);
            }
            os.close();
            is.close();
        } catch (Exception e) {
            logger.error("下载机构信息模板失败", e);
        }
    }


    /**
     * 批量添加试题
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/21 11:39
     */
    @RequestMapping(value = "/batchAdd", method = RequestMethod.POST)
    public RespDto batchAdd(MultipartFile file, Long questionBankId) throws UserException {
//        String tmpPath = SingleApi.class.getClassLoader().getResource("models").getPath() + "/tmp";
        File target = new File("e:/test.xlsx");
        Long userId = UserUtil.getCurrentUser().getId();
        try {
            file.transferTo(target);
            List<List<String>> data = ExcelUtil.getData(target);
            logger.info("批量导入机构，", JSON.toJSONString(data, true));
            data.remove(0);
            List<Branch> branches = branchService.batchAdd(data, userId);
            return RespDto.success();
        } catch (Exception e) {
            logger.error("批量添加机构信息失败", e);
            return RespDto.fail("批量添加机构失败");
        }

    }
}

