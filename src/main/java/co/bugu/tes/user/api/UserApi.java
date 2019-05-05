package co.bugu.tes.user.api;

import co.bugu.common.RespDto;
import co.bugu.common.enums.DelFlagEnum;
import co.bugu.exception.UserException;
import co.bugu.tes.branch.domain.Branch;
import co.bugu.tes.branch.service.IBranchService;
import co.bugu.tes.department.domain.Department;
import co.bugu.tes.department.service.IDepartmentService;
import co.bugu.tes.loginLog.domain.LoginLog;
import co.bugu.tes.loginLog.service.ILoginLogService;
import co.bugu.tes.station.domain.Station;
import co.bugu.tes.station.service.IStationService;
import co.bugu.tes.user.domain.User;
import co.bugu.tes.user.dto.UserDto;
import co.bugu.tes.user.service.IUserService;
import co.bugu.tes.userRoleX.domain.UserRoleX;
import co.bugu.tes.userRoleX.service.IUserRoleXService;
import co.bugu.util.ExcelUtil;
import co.bugu.util.TokenUtil;
import co.bugu.util.UserUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据api
 *
 * @author daocers
 * @create 2018-11-19 17:51
 */
@RestController
@RequestMapping("/user/api")
public class UserApi {
    private Logger logger = LoggerFactory.getLogger(UserApi.class);

    @Autowired
    IUserService userService;

    @Autowired
    IUserRoleXService userRoleXService;

    @Autowired
    IBranchService branchService;
    @Autowired
    IDepartmentService departmentService;
    @Autowired
    IStationService stationService;
    @Autowired
    ILoginLogService loginLogService;


    /**
     * 登录，成功后返回用户token
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/19 17:54
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RespDto<String> login(String username, String password, HttpServletRequest request) throws Exception {
        Preconditions.checkArgument(StringUtils.isNotEmpty(username), "用户名不能为空");
        Preconditions.checkArgument(StringUtils.isNotEmpty(password), "密码不能为空");

        String ip = request.getRemoteAddr();
        User user = new User();
        user.setUsername(username);
        List<User> users = userService.findByCondition(user);
        if (CollectionUtils.isNotEmpty(users)) {
            if (users.size() > 1) {
                logger.warn("用户名相同的用户有两个，数据异常， username: {}", username);
                throw new Exception("数据异常");
            } else {
                user = users.get(0);
//                处理登录日志
                try {
                    LoginLog log = new LoginLog();
                    String userAgent = request.getHeader("user-agent");
                    log.setUserId(user.getId());
                    log.setIp(ip);
                    log.setContent(userAgent.substring(0, 100));
                    loginLogService.add(log);
                } catch (Exception e) {
                    logger.error("保存登录日志失败", e);
                }

            }

            if (!user.getPassword().equals(password)) {
                return RespDto.fail(-1, "用户名/密码错误");
            }
//            用户名密码匹配，设置token传给服务端
            String token = TokenUtil.getToken(user);
            UserUtil.saveUserToken(user.getId(), token);
            return RespDto.success(token);
        } else {
            return RespDto.fail(-1, "用户名/密码错误");
        }

    }

    /**
     * 退出登录，退出成功前端跳回首页
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/19 17:55
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public RespDto<Boolean> logout() {
        UserUtil.invalidToken();
        return RespDto.success(true);
    }

    /**
     * 修改密码
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/12/7 16:40
     */
    @RequestMapping(value = "/changePass", method = RequestMethod.POST)
    public RespDto<Boolean> changePassword(String password, String passNew) throws UserException {
        Preconditions.checkArgument(StringUtils.isNotEmpty(password), "密码不能为空");
        Preconditions.checkArgument(StringUtils.isNotEmpty(passNew), "新密码不能为空");
        User user = UserUtil.getCurrentUser();
        if (user.getPassword().equals(password)) {
            user.setPassword(passNew);
            user.setUpdateUserId(user.getId());
            user.setUpdateTime(new Date());
            user.setId(user.getId());
            int num = userService.updateById(user);
            return RespDto.success(true);
        } else {
            return RespDto.fail("密码错误");
        }

    }


    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-19 17:51
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<UserDto>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody User user) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(user, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            PageInfo<User> pageInfo = userService.findByConditionWithPage(pageNum, pageSize, user);
            PageInfo<UserDto> res = new PageInfo<>();
            BeanUtils.copyProperties(pageInfo, res);
            List<UserDto> list = Lists.transform(pageInfo.getList(), new Function<User, UserDto>() {
                @Override
                public UserDto apply(@Nullable User user) {
                    UserDto dto = new UserDto();
                    BeanUtils.copyProperties(user, dto);
                    Branch branch = branchService.findById(user.getBranchId());
                    if (branch != null) {
                        dto.setBranchName(branch.getName());
                    }
                    Department department = departmentService.findById(user.getDepartmentId());
                    if (department != null) {
                        dto.setDepartmentName(department.getName());
                    }
                    Station station = stationService.findById(user.getStationId());
                    if (station != null) {
                        dto.setStationName(station.getName());
                    }
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
     * @param user
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-19 17:51
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Long> saveUser(@RequestBody User user) {
        try {


            Long userId = user.getId();
            user.setCreateUserId(userId);
            user.setUpdateUserId(userId);
            if (null == userId) {
                User query = new User();
                query.setUsername(user.getUsername());
                query.setIsDel(DelFlagEnum.NO.getCode());
                List<User> users = userService.findByCondition(query);
                if (CollectionUtils.isNotEmpty(users)) {
                    return RespDto.fail("该用户名已经存在，请确认");
                }

                user.setPassword("888888");
                logger.debug("保存， saveUser, 参数： {}", JSON.toJSONString(user, true));
                userId = userService.add(user);
                logger.info("新增 成功， id: {}", userId);
            } else {
                userService.updateById(user);
                logger.debug("更新成功", JSON.toJSONString(user, true));
            }
            return RespDto.success(userId);
        } catch (Exception e) {
            logger.error("保存 user 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.user.domain.User>
     * @author daocers
     * @date 2018-11-19 17:51
     */
    @RequestMapping(value = "/findById")
    public RespDto<User> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            User user = userService.findById(id);
            return RespDto.success(user);
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
     * @date 2018-11-19 17:51
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id, Long operatorId) {
        try {
            logger.debug("准备删除， 参数: {}", id);
            Preconditions.checkArgument(id != null, "id不能为空");
            int count = userService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }


    /***
     * 分配角色
     * @author daocers
     * @date 2017/12/8 9:29
     * @param userId
     * @param roleIdList
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     */
    @RequestMapping(value = "/assignRole", method = RequestMethod.POST)
    public RespDto<Boolean> assignRole(Long userId, @RequestBody List<Long> roleIdList) {
        try {
            Preconditions.checkArgument(null != userId);
            Long currentUserId = UserUtil.getCurrentUser().getId();
            UserRoleX delete = new UserRoleX();
            delete.setUpdateUserId(currentUserId);
            delete.setUserId(userId);

            List<UserRoleX> xList = new ArrayList<>();
            int index = 0;
            for (Long roleId : roleIdList) {
                UserRoleX x = new UserRoleX();
                x.setUserId(userId);
                x.setIsDel(DelFlagEnum.NO.getCode());
                x.setNo(index++);
                x.setRoleId(roleId);
                x.setCreateUserId(currentUserId);
                x.setUpdateUserId(currentUserId);
                xList.add(x);
            }
//            boolean result = userRoleXService.assignRole(delete, xList);
            userRoleXService.deleteByUserId(userId);
            userRoleXService.batchAdd(xList);
            return RespDto.success(true);

        } catch (Exception e) {
            logger.error("分配角色失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 下载用户模板
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019-01-05 11:21:00
     */
    @RequestMapping(value = "/downloadModel")
    public void downloadModel(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(("用户信息模板.xlsx").getBytes(), "iso-8859-1"));
            OutputStream os = response.getOutputStream();
            String rootPath = request.getServletContext().getRealPath("/");

            InputStream is = new BufferedInputStream(UserApi.class.getClassLoader().getResourceAsStream("models/用户信息模板.xlsx"));
            byte[] buffer = new byte[1024];

            while (is.read(buffer) != -1) {
                os.write(buffer);
            }
            os.close();
            is.close();
        } catch (Exception e) {
            logger.error("下载用户信息模板失败", e);
        }
    }


    /**
     * 批量添加用户
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019-01-05 11:21:00
     */
    @RequestMapping(value = "/batchAdd", method = RequestMethod.POST)
    public RespDto<Boolean> batchAdd(MultipartFile file, Long roleId) {
        String tmpPath = UserApi.class.getClassLoader().getResource("models").getPath() + "/tmp";
        File tmpDir = new File(tmpPath);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        File target = new File(tmpDir, file.getOriginalFilename());
        try {
            file.transferTo(target);
            List<List<String>> data = ExcelUtil.getData(target);
            logger.info("批量导入用户，", JSON.toJSONString(data, true));
            data.remove(0);
            List<User> users = userService.batchAdd(data, roleId);
            return RespDto.success(true);
        } catch (Exception e) {
            logger.error("批量添加用户失败", e);
            return RespDto.fail("批量添加用户失败");
        } finally {
            target.delete();
        }

    }
}

