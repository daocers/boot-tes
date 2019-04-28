package co.bugu.tes.paperPolicy.api;

import co.bugu.common.RespDto;
import co.bugu.tes.paperPolicy.domain.PaperPolicy;
import co.bugu.tes.paperPolicy.dto.PaperPolicyDto;
import co.bugu.tes.paperPolicy.service.IPaperPolicyService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
 * @create 2019-04-28 17:08
 */
@RestController
@RequestMapping("/paperPolicy/api")
public class PaperPolicyApi {
    private static Logger logger = LoggerFactory.getLogger(PaperPolicyApi.class);

    @Autowired
    IPaperPolicyService paperPolicyService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2019-04-28 17:08
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<PaperPolicy>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody PaperPolicy paperPolicy) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(paperPolicy, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<PaperPolicy> list = paperPolicyService.findByCondition(pageNum, pageSize, paperPolicy);
            PageInfo<PaperPolicy> pageInfo = new PageInfo<>(list);
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
     * @param paperPolicy
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2019-04-28 17:08
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> savePaperPolicy(@RequestBody PaperPolicyDto paperPolicyDto) {
        try {
            logger.debug("保存， savePaperPolicy, 参数： {}", JSON.toJSONString(paperPolicyDto, true));
            PaperPolicy paperPolicy = new PaperPolicy();
            BeanUtils.copyProperties(paperPolicyDto, paperPolicy);
            paperPolicy.setSingleInfo(JSON.toJSONString(paperPolicyDto.getSingleInfo()));
            paperPolicy.setMultiInfo(JSON.toJSONString(paperPolicyDto.getMultiInfo()));
            paperPolicy.setJudgeInfo(JSON.toJSONString(paperPolicyDto.getJudgeInfo()));
            Long paperPolicyId = paperPolicyService.add(paperPolicy);
            logger.info("新增 成功， id: {}", paperPolicyId);

            return RespDto.success(paperPolicyId != null);
        } catch (Exception e) {
            logger.error("保存 paperPolicy 失败", e);
            return RespDto.fail();
        }
    }

    /**
     * 更新指定id的记录
     *
     * @param paperPolicy
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2019-04-28 17:08
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RespDto<Boolean> updateById(@RequestBody PaperPolicy paperPolicy) {
        try {
            logger.debug("更新，updateById, 参数: {}", JSON.toJSONString(paperPolicy, true));
            Preconditions.checkArgument(paperPolicy != null, "paperPolicy不能为空");
            Preconditions.checkArgument(null != paperPolicy.getId(), "id不能为空");
            int res = paperPolicyService.updateById(paperPolicy);

            return RespDto.success(res == 1);

        } catch (Exception e) {
            logger.error("更新 paperPolicy 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.paperPolicy.domain.PaperPolicyDto>
     * @author daocers
     * @date 2019-04-28 17:08
     */
    @RequestMapping(value = "/getDetail")
    public RespDto<PaperPolicy> getDetail(Long id) {
        try {
            logger.info("获取详情，getDetail, id： {}", id);
            PaperPolicy paperPolicy = paperPolicyService.findById(id);
            return RespDto.success(paperPolicy);
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
     * @date 2019-04-28 17:08
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id, Long operatorId) {
        try {
            logger.debug("准备删除， 参数: {}", id);
            Preconditions.checkArgument(id != null, "id不能为空");
            int count = paperPolicyService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

