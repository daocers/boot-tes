package co.bugu;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class Test {
    public static void main(String[] args) {
        System.out.println("good");

        Set<Long> userIds = new HashSet<>();
        for(long i = 0; i < 100;i++){
            userIds.add(i);
        }

        ForkJoinPool pool = new ForkJoinPool(10);
        pool.submit(() ->{
           userIds.parallelStream().forEach(userId -> {
               System.out.println(userId + " " + Thread.currentThread().getName());
           });
        });

        if(!pool.isShutdown()){
            pool.shutdown();

        }

    }


}

/**
 * select * from user where user_id = #{userId}
 *
 * BlockingQueue queue = new BlockingQueue
 *
 *
 *
 *
 */
