package co.bugu.prop;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.MapUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author daocers
 * @Date 2019/5/5:11:53
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "bugu")
public class TesConfig {

    @Setter
    private HashMap<Integer, String> difficultyMap;

    @Setter
    private HashMap<Integer, String> busiTypeMap;

    @Getter
    private Map<String, Integer> busiTypeInfo;
    @Getter
    private Map<String, Integer> difficultyInfo;


    /**
     * 初始化之后执行，用于解决yml配置文件中key不能使用中文的情况
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/5 14:51
     */
    @PostConstruct
    public void postProcess() {
        busiTypeInfo = new HashMap<>();
        busiTypeInfo.put("", -1);
        busiTypeInfo.put(null, -1);

        difficultyInfo = new HashMap<>();
        difficultyInfo.put("", -1);
        difficultyInfo.put(null, -1);
        if (MapUtils.isNotEmpty(difficultyMap)) {
            Set<Integer> keySet = difficultyMap.keySet();
            for (Integer key : keySet) {
                String value = difficultyMap.get(key);
                difficultyInfo.put(value, key);
            }
        }
        if (MapUtils.isNotEmpty(busiTypeMap)) {
            Set<Integer> keySet = busiTypeMap.keySet();
            for (Integer key : keySet) {
                String value = busiTypeMap.get(key);
                busiTypeInfo.put(value, key);
            }
        }
    }
}
