package co.bugu.tes.multi.api;

import co.bugu.common.RespDto;
import co.bugu.tes.multi.domain.Multi;
import co.bugu.tes.multi.service.IMultiService;
import co.bugu.tes.question.agent.QuestionAgent;
import co.bugu.tes.question.dto.QuestionListDto;
import co.bugu.tes.single.api.SingleApi;
import co.bugu.tes.user.domain.User;
import co.bugu.util.ExcelUtil;
import co.bugu.util.UserUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
@RequestMapping("/multi/api")
public class MultiApi {
    private Logger logger = LoggerFactory.getLogger(MultiApi.class);

    @Autowired
    IMultiService multiService;
    @Autowired
    QuestionAgent questionAgent;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<QuestionListDto>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Multi multi) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(multi, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            PageInfo<Multi> pageInfo = multiService.findByConditionWithPage(pageNum, pageSize, multi);
            PageInfo<QuestionListDto> res = new PageInfo<>();
            BeanUtils.copyProperties(pageInfo, res);
            List<QuestionListDto> list = Lists.transform(pageInfo.getList(), new Function<Multi, QuestionListDto>() {
                @Override
                public QuestionListDto apply(@Nullable Multi multi) {
                    QuestionListDto dto = new QuestionListDto();
                    BeanUtils.copyProperties(multi, dto);
                    questionAgent.processName(dto);
                    return dto;
                }
            });
            res.setList(list);
            logger.info("查询到数据： {}", JSON.toJSONString(pageInfo, true));
            return RespDto.success(res);
        } catch (Exception e) {
            logger.error("findByCondition  失败", e);
            return RespDto.fail();
        }
    }

    /**
     * 保存
     *
     * @param multi
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveMulti(@RequestBody Multi multi) {
        try {
            Long multiId = multi.getId();
            User user = UserUtil.getCurrentUser();

            multi.setUpdateUserId(user.getId());
            if (null == multiId) {
                multi.setCreateUserId(user.getId());
                multi.setBranchId(user.getBranchId());
                multi.setStationId(user.getStationId());
                multi.setDepartmentId(user.getDepartmentId());
                logger.debug("保存， saveMulti, 参数： {}", JSON.toJSONString(multi, true));
                multiId = multiService.add(multi);
                logger.info("新增 成功， id: {}", multiId);
            } else {
                multiService.updateById(multi);
                logger.debug("更新成功", JSON.toJSONString(multi, true));
            }
            return RespDto.success(multiId != null);
        } catch (Exception e) {
            logger.error("保存 multi 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.multi.domain.Multi>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findById")
    public RespDto<Multi> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            Multi multi = multiService.findById(id);
            return RespDto.success(multi);
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
            int count = multiService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
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
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(("多选题模板.xlsx").getBytes(), "iso-8859-1"));
            OutputStream os = response.getOutputStream();
            String rootPath = request.getServletContext().getRealPath("/");

            InputStream is = new BufferedInputStream(SingleApi.class.getClassLoader().getResourceAsStream("models/多选题模板.xlsx"));
            byte[] buffer = new byte[1024];

            while (is.read(buffer) != -1) {
                os.write(buffer);
            }
            os.close();
            is.close();
        } catch (Exception e) {
            logger.error("下载多选题模板失败", e);
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
    public RespDto<Boolean> batchAdd(MultipartFile file, Long questionBankId) {
//        String tmpPath = SingleApi.class.getClassLoader().getResource("models").getPath() + "/tmp";
        File target = new File("e:/test.xlsx");
        try {
            User user = UserUtil.getCurrentUser();
            file.transferTo(target);
            List<List<String>> data = ExcelUtil.getData(target);
            logger.info("批量导入试题，", JSON.toJSONString(data, true));
            data.remove(0);
            List<Multi> multis = multiService.batchAdd(data, user.getId(), questionBankId, user.getStationId(), user.getBranchId(), user.getDepartmentId(), 1);
            return RespDto.success(true);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (StringUtils.isEmpty(msg)) {
                msg = "批量添加多选题失败";
            }
            logger.error("批量添加多选题失败", e);
            return RespDto.fail(msg);
        } finally {
            target.delete();
        }

    }
}

