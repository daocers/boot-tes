package co.bugu.tes.index.api;

import co.bugu.common.RespDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author daocers
 * @Date 2019/5/18:17:27
 * @Description:
 */
@RestController
@RequestMapping("/index/api")
public class IndexApi {


    /**
     * 检查token是否有效，没有实际业务
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/18 17:28
     */
    @RequestMapping("/checkToken")
    public RespDto<Boolean> checkToken(){
        return RespDto.success(true);
    }


}
