package co.bugu;

import java.io.IOException;
import java.util.Calendar;

public class Test {
    public static void main(String[] args) throws IOException {
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
//        test();
//
//        DecimalFormat format = new DecimalFormat("###,###.00");
//        System.out.println(format.format((float)9910 / 1000));


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 9999);
        System.out.println(calendar.getTime().getTime());

//        File a = new File("a.txt");
//        File b = new File("b.txt");
//        FileWriter writerA = new FileWriter(a);
//        FileWriter writerB = new FileWriter(b);
//        writerA.append("good");
//        writerB.append("morning");
//
//        writerA.close();
//        writerB.close();
//
//        BufferedInputStream bis = new BufferedInputStream(new FileInputStream("a.txt"));
//
//
//        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File("yasuo.zip")));
//        ZipEntry entry = new ZipEntry("a.txt");
//        zos.putNextEntry(entry);
//        zos.putNextEntry(new ZipEntry("b.txt"));
//        int count;
//        byte[] buf = new byte[1024];
//        while ((count = bis.read(buf)) != -1) {
//            zos.write(buf, 0, count);
//        }
//        bis.close();
//        zos.close();
//
//        Integer num = null;
//        System.out.println(1 == num);

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
