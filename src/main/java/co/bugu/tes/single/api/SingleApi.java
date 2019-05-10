package co.bugu.tes.single.api;

import co.bugu.common.RespDto;
import co.bugu.tes.branch.service.IBranchService;
import co.bugu.tes.department.service.IDepartmentService;
import co.bugu.tes.question.agent.QuestionAgent;
import co.bugu.tes.question.dto.QuestionListDto;
import co.bugu.tes.questionBank.service.IQuestionBankService;
import co.bugu.tes.single.domain.Single;
import co.bugu.tes.single.service.ISingleService;
import co.bugu.tes.station.service.IStationService;
import co.bugu.tes.user.domain.User;
import co.bugu.tes.user.service.IUserService;
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
@RequestMapping("/single/api")
public class SingleApi {
    private Logger logger = LoggerFactory.getLogger(SingleApi.class);

    @Autowired
    ISingleService singleService;
    @Autowired
    IUserService userService;
    @Autowired
    IDepartmentService departmentService;
    @Autowired
    IBranchService branchService;
    @Autowired
    IStationService stationService;
    @Autowired
    IQuestionBankService questionBankService;

    @Autowired
    QuestionAgent questionAgent;


    /**
     * 下载单选题模板
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
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(("单选题模板.xlsx").getBytes(), "iso-8859-1"));
            OutputStream os = response.getOutputStream();
            String rootPath = request.getServletContext().getRealPath("/");

            InputStream is = new BufferedInputStream(SingleApi.class.getClassLoader().getResourceAsStream("models/单选题模板.xlsx"));
            byte[] buffer = new byte[1024];

            while (is.read(buffer) != -1) {
                os.write(buffer);
            }
            os.close();
            is.close();
        } catch (Exception e) {
            logger.error("下载单选题模板失败", e);
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
        String tmpPath = SingleApi.class.getClassLoader().getResource("models").getPath() + "/tmp";
        File tmpDir = new File(tmpPath);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        File target = new File(tmpDir, file.getOriginalFilename());
        try {
            file.transferTo(target);
            List<List<String>> data = ExcelUtil.getData(target);
            logger.info("批量导入试题，", JSON.toJSONString(data, true));
            data.remove(0);
            User user = UserUtil.getCurrentUser();
            List<Single> singles = singleService.batchAdd(data, user.getId(), questionBankId, user.getStationId(), user.getBranchId(), user.getDepartmentId(), 1);
            return RespDto.success(true);
        } catch (Exception e) {
            logger.error("批量添加单选题失败", e);
            String msg = e.getMessage();
            if (StringUtils.isEmpty(msg)) {
                msg = "批量添加单选题失败";
            }
            return RespDto.fail(msg);
        } finally {
            target.delete();
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
    public RespDto<PageInfo<QuestionListDto>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Single single) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(single, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            PageInfo<Single> pageInfo = singleService.findByConditionWithPage(pageNum, pageSize, single);
            PageInfo<QuestionListDto> res = new PageInfo<>();
            BeanUtils.copyProperties(pageInfo, res);
            List<QuestionListDto> list = Lists.transform(pageInfo.getList(), new Function<Single, QuestionListDto>() {
                @Override
                public QuestionListDto apply(@Nullable Single single) {
                    QuestionListDto dto = new QuestionListDto();
                    BeanUtils.copyProperties(single, dto);
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
     * @param single
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveSingle(@RequestBody Single single) {
        try {
            User user = UserUtil.getCurrentUser();
            single.setUpdateUserId(user.getId());

            Long singleId = single.getId();
            if (null == singleId) {
                single.setCreateUserId(user.getId());
                single.setStationId(user.getStationId());
                single.setBranchId(user.getBranchId());
                single.setDepartmentId(user.getDepartmentId());
                logger.debug("保存， saveSingle, 参数： {}", JSON.toJSONString(single, true));
                singleId = singleService.add(single);
                logger.info("新增 成功， id: {}", singleId);
            } else {
                singleService.updateById(single);
                logger.debug("更新成功", JSON.toJSONString(single, true));
            }
            return RespDto.success(singleId != null);
        } catch (Exception e) {
            logger.error("保存 single 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.single.domain.Single>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findById")
    public RespDto<Single> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            Single single = singleService.findById(id);
            return RespDto.success(single);
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
            int count = singleService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

