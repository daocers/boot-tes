package co.bugu.tes.department.api;

import co.bugu.common.RespDto;
import co.bugu.common.enums.BaseStatusEnum;
import co.bugu.common.enums.DelFlagEnum;
import co.bugu.exception.UserException;
import co.bugu.tes.department.domain.Department;
import co.bugu.tes.department.dto.DepartmentDto;
import co.bugu.tes.department.service.IDepartmentService;
import co.bugu.tes.manager.domain.Manager;
import co.bugu.tes.manager.enums.ManagerTypeEnum;
import co.bugu.tes.manager.service.IManagerService;
import co.bugu.util.CodeUtil;
import co.bugu.util.UserUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
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
@RequestMapping("/department/api")
public class DepartmentApi {
    private Logger logger = LoggerFactory.getLogger(DepartmentApi.class);

    @Autowired
    IDepartmentService departmentService;
    @Autowired
    IManagerService managerService;

    /**
     * 设置部门管理员
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/12/14 9:30
     */
    @RequestMapping(value = "/setManager")
    public RespDto<Boolean> setManger(String userIds, Long targetId) {
        try {
            List<Long> ids = JSON.parseArray(userIds, Long.class);
            for (Long userId : ids) {
                managerService.setManager(ManagerTypeEnum.DEPARTMENT.getCode(), userId, targetId);
            }
            return RespDto.success(true);
        } catch (Exception e) {
            logger.error("设置部门管理员失败", e);
            return RespDto.fail("设置管理员失败");
        }
    }

    @RequestMapping(value = "/removeManager")
    public RespDto<Boolean> removeManager(Long userId, Long departmentId) {
        try {
            Manager query = new Manager();
            query.setUserId(userId);
            query.setTargetId(departmentId);
            query.setType(ManagerTypeEnum.DEPARTMENT.getCode());
            List<Manager> managers = managerService.findByCondition(query);
            Long currentUserId = UserUtil.getCurrentUser().getId();
            Manager manager = new Manager();
            manager.setId(managers.get(0).getId());
            manager.setIsDel(DelFlagEnum.YES.getCode());
            manager.setTargetId(departmentId);
            manager.setUserId(userId);
            manager.setUpdateUserId(currentUserId);
            managerService.updateById(manager);
            return RespDto.success(true);
        } catch (Exception e) {
            logger.error("删除管理员失败", e);
            return RespDto.fail("删除管理员失败");
        }
    }


    @RequestMapping(value = "/getUnderManager")
    public RespDto<List<Department>> getUnderManager() throws UserException {
        Long userId = UserUtil.getCurrentUser().getId();
        Manager query = new Manager();
        query.setUserId(userId);
        query.setType(ManagerTypeEnum.DEPARTMENT.getCode());
        query.setIsDel(DelFlagEnum.NO.getCode());
        List<Manager> list = managerService.findByCondition(query);
        if (CollectionUtils.isNotEmpty(list)) {
            List<Department> departments = Lists.transform(list, new Function<Manager, Department>() {
                @Override
                public Department apply(@Nullable Manager manager) {
                    Long departmentId = manager.getTargetId();
                    Department department = departmentService.findById(departmentId);
                    return department;
                }
            });
            return RespDto.success(departments);
        } else {
            return RespDto.success();
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
    public RespDto<PageInfo<DepartmentDto>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Department department) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(department, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            PageInfo<Department> pageInfo = departmentService.findByConditionWithPage(pageNum, pageSize, department);
            PageInfo<DepartmentDto> res = new PageInfo<>();
            BeanUtils.copyProperties(pageInfo, res);
            List<DepartmentDto> list = Lists.transform(pageInfo.getList(), new Function<Department, DepartmentDto>() {
                @Override
                public DepartmentDto apply(@Nullable Department department) {
                    DepartmentDto dto = new DepartmentDto();
                    BeanUtils.copyProperties(department, dto);
                    dto.setUserList(managerService.getManager(ManagerTypeEnum.DEPARTMENT.getCode(), department.getId()));
                    return dto;
                }
            });
            res.setList(list);
            return RespDto.success(res);
        } catch (Exception e) {
            logger.error("findByCondition  失败", e);
            return RespDto.fail();
        }
    }

    /**
     * 保存
     *
     * @param department
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveDepartment(@RequestBody Department department) {
        try {
            Long userId = UserUtil.getCurrentUser().getId();
            Long departmentId = department.getId();
            department.setUpdateUserId(userId);
            if (null == departmentId) {
                department.setCode(CodeUtil.getDepartmentCode());
                department.setCreateUserId(userId);
                department.setStatus(BaseStatusEnum.ENABLE.getCode());
                logger.debug("保存， saveDepartment, 参数： {}", JSON.toJSONString(department, true));
                departmentId = departmentService.add(department);
                logger.info("新增 成功， id: {}", departmentId);
            } else {
                departmentService.updateById(department);
                logger.debug("更新成功", JSON.toJSONString(department, true));
            }
            return RespDto.success(departmentId != null);
        } catch (Exception e) {
            logger.error("保存 department 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.department.domain.Department>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findById")
    public RespDto<Department> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            Department department = departmentService.findById(id);
            return RespDto.success(department);
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
            int count = departmentService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }

    /**
     * 获取全部岗位信息
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/30 22:09
     */
    @RequestMapping(value = "/findAll")
    public RespDto<List<Department>> findAll() {
        List<Department> departments = departmentService.findByCondition(null);
        return RespDto.success(departments);
    }
}

