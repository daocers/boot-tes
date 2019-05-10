package co.bugu;

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
//        PageInfo<String> res = new PageInfo<>();
//        List<String> list = new ArrayList<>();
//        list.add("ab");
//        list.add("dfd");
//        res.setList(list);
//        System.out.println(JSON.toJSONString(res, true));
//        System.out.println(JSON.toJSONString(RespDto.success(res), true));
        test();

    }


    public static  void test(String... names){
        System.out.println(names.length);
    }

}

/**
 * select * from user where user_id = #{userId}
 * <p>
 * BlockingQueue queue = new BlockingQueue
 */
