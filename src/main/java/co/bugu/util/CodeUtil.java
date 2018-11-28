package co.bugu.util;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author daocers
 * @Date 2018/11/28:16:14
 * @Description:
 */
public class CodeUtil {
    private static volatile String dateInfo;

    private static AtomicLong sceneIndex = new AtomicLong(1L);
    private static AtomicLong paperIndex = new AtomicLong(1L);
    private static AtomicLong questionIndex = new AtomicLong(1L);


    static {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String info = formatter.toFormat().format(new Date());

        dateInfo = info;
    }

    public static void setDateInfo(String dateInfo) {
        CodeUtil.dateInfo = dateInfo;
    }

    /**
     * 获取场次编码
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/28 16:17
     */
    public static String getSceneCode() {
        long index = sceneIndex.getAndIncrement();
        return "S" + dateInfo + getSameLengthInfo(index, 5);
    }

    private static String getSameLengthInfo(long num, int length) {
        StringBuffer tmp = new StringBuffer();
        tmp.append(num);
        if (tmp.length() > length) {
            return tmp.toString();
        }

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length - tmp.length(); i++) {
            buffer.append(0);
        }

        buffer.append(num);
        return buffer.toString();
    }

    /**
     * 获取试卷编码
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/28 16:17
     */
    public static String getPaperCode() {
    }

    /**
     * 获取试题编码
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/28 16:17
     */
    public static String getQuestionCode(Integer questionType) {
        return ""
    }

}
