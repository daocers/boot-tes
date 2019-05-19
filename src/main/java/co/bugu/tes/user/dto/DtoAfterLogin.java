package co.bugu.tes.user.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author daocers
 * @Date 2019/5/19:18:01
 * @Description:
 */
@Data
public class DtoAfterLogin {
    private List<String> menuUrlList;
    private String token;
}
