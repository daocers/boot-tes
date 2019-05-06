package co.bugu;

import co.bugu.common.RespDto;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        System.out.println("good");

//        Set<Long> userIds = new HashSet<>();
//        for(long i = 0; i < 100;i++){
//            userIds.add(i);
//        }
//
//        ForkJoinPool pool = new ForkJoinPool(10);
//        pool.submit(() ->{
//           userIds.parallelStream().forEach(userId -> {
//               System.out.println(userId + " " + Thread.currentThread().getName());
//           });
//        });
//
//        if(!pool.isShutdown()){
//            pool.shutdown();
//
//        }
        PageInfo<String> res = new PageInfo<>();
        List<String> list = new ArrayList<>();
        list.add("ab");
        list.add("dfd");
        res.setList(list);
        System.out.println(JSON.toJSONString(res, true));
        System.out.println(JSON.toJSONString(RespDto.success(res), true));

    }


}

/**
 * select * from user where user_id = #{userId}
 * <p>
 * BlockingQueue queue = new BlockingQueue
 */
