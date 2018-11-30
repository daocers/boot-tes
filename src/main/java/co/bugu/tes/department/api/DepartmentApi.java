package co.bugu.tes.department.api;

import co.bugu.common.RespDto;
import co.bugu.tes.department.domain.Department;
import co.bugu.tes.department.service.IDepartmentService;
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
@RequestMapping("/department/api")
public class DepartmentApi {
    private Logger logger = LoggerFactory.getLogger(DepartmentApi.class);

    @Autowired
    IDepartmentService departmentService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<Department>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Department department) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(department, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<Department> list = departmentService.findByCondition(pageNum, pageSize, department);
            PageInfo<Department> pageInfo = new PageInfo<>(list);
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
     * @param department
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveDepartment(@RequestBody Department department) {
        try {
            Long departmentId = department.getId();
            if (null == departmentId) {
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

