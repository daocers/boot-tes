package co.bugu.tes.questionBank.api;

import co.bugu.common.RespDto;
import co.bugu.common.enums.BaseStatusEnum;
import co.bugu.exception.UserException;
import co.bugu.tes.branch.domain.Branch;
import co.bugu.tes.branch.service.IBranchService;
import co.bugu.tes.department.domain.Department;
import co.bugu.tes.department.service.IDepartmentService;
import co.bugu.tes.question.agent.QuestionAgent;
import co.bugu.tes.question.dto.QuestionCountDto;
import co.bugu.tes.questionBank.domain.QuestionBank;
import co.bugu.tes.questionBank.dto.QuestionBankDto;
import co.bugu.tes.questionBank.service.IQuestionBankService;
import co.bugu.tes.single.service.ISingleService;
import co.bugu.tes.station.domain.Station;
import co.bugu.tes.station.service.IStationService;
import co.bugu.tes.user.domain.User;
import co.bugu.tes.user.service.IUserService;
import co.bugu.util.UserUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.checkerframework.checker.nullness.qual.Nullable;
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
 * @create 2018-11-20 17:15
 */
@RestController
@RequestMapping("/questionBank/api")
public class QuestionBankApi {
    private Logger logger = LoggerFactory.getLogger(QuestionBankApi.class);

    @Autowired
    IQuestionBankService questionBankService;


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
    QuestionAgent questionAgent;

    @RequestMapping(value = "/findAll")
    public RespDto<List<QuestionBank>> findAll() throws UserException {
//        todo 获取用户信息
        User user = UserUtil.getCurrentUser();
        QuestionBank query = new QuestionBank();
//        todo 筛选权限
        List<QuestionBank> banks = questionBankService.findByCondition(query);
        return RespDto.success(banks);
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
    public RespDto<PageInfo<QuestionBankDto>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody QuestionBank questionBank) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(questionBank, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            PageInfo<QuestionBank> pageInfo = questionBankService.findByConditionWithPage(pageNum, pageSize, questionBank);
            PageInfo<QuestionBankDto> res = new PageInfo<>();
            BeanUtils.copyProperties(pageInfo, res);
            List<QuestionBankDto> list = Lists.transform(pageInfo.getList(), new Function<QuestionBank, QuestionBankDto>() {
                @Override
                public QuestionBankDto apply(@Nullable QuestionBank questionBank) {
                    QuestionBankDto dto = new QuestionBankDto();
                    BeanUtils.copyProperties(questionBank, dto);
                    Branch branch = branchService.findById(questionBank.getBranchId());
                    if (null != branch) {
                        dto.setBranchName(branch.getName());
                    }

                    Department department = departmentService.findById(questionBank.getDepartmentId());
                    if (null != department) {
                        dto.setDepartmentName(department.getName());
                    }

                    Station station = stationService.findById(questionBank.getStationId());
                    if (null != station) {
                        dto.setStationName(station.getName());
                    }
                    User user = userService.findById(questionBank.getCreateUserId());
                    if (null != user) {
                        dto.setCreateUserName(user.getName());
                    }
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
     * @param questionBank
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveQuestionBank(@RequestBody QuestionBank questionBank) {
        try {
            User user = UserUtil.getCurrentUser();
            Long userId = user.getId();
            questionBank.setUpdateUserId(userId);
            Long questionBankId = questionBank.getId();
            if (null == questionBankId) {
                questionBank.setCreateUserId(userId);
                questionBank.setStatus(BaseStatusEnum.ENABLE.getCode());
                questionBank.setBranchId(user.getBranchId());
                questionBank.setDepartmentId(user.getDepartmentId());
                questionBank.setStationId(user.getStationId());
                logger.debug("保存， saveQuestionBank, 参数： {}", JSON.toJSONString(questionBank, true));
                questionBankId = questionBankService.add(questionBank);
                logger.info("新增 成功， id: {}", questionBankId);
            } else {
                questionBankService.updateById(questionBank);
                logger.debug("更新成功", JSON.toJSONString(questionBank, true));
            }
            return RespDto.success(questionBankId != null);
        } catch (Exception e) {
            logger.error("保存 questionBank 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.questionBank.domain.QuestionBank>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findById")
    public RespDto<QuestionBank> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            QuestionBank questionBank = questionBankService.findById(id);
            return RespDto.success(questionBank);
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
    public RespDto<Boolean> delete(Long id) throws UserException, Exception {
        logger.debug("准备删除， 参数: {}", id);
        Preconditions.checkArgument(id != null, "id不能为空");

        Long userId = UserUtil.getCurrentUser().getId();

        QuestionCountDto dto = questionAgent.getQuestionCount(id);
        if (dto.getTotal() > 0) {
            throw new Exception("本题库已有试题，不能删除");
        }
        int count = questionBankService.deleteById(id, userId);

        return RespDto.success(count == 1);
    }
}

