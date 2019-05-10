package co.bugu.exception;

import co.bugu.common.RespDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 * @Author daocers
 * @Date 2019/5/5:16:25
 * @Description:
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);



    @ExceptionHandler(value = UserException.class)
    @ResponseBody
    public RespDto handle(UserException e){
        logger.error("用户异常", e);
        return RespDto.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public RespDto handle(IllegalArgumentException e){
        logger.error("非法参数异常", e);
        String msg = e.getMessage();
        if(StringUtils.isEmpty(msg)){
            msg = "您传入的参数有误";
        }
        return RespDto.fail(-100, msg);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public RespDto handle(Exception e){
        logger.error("系统异常", e);
        String errMsg = e.getMessage();
        if(StringUtils.isEmpty(errMsg)){
            errMsg = "系统异常，请联系管理员";
        }
        return RespDto.fail(-10000, errMsg);
    }
}
