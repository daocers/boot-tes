package co.bugu.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author daocers
 * @Date 2018/11/28:16:14
 * @Description:
 */
public class CodeUtil {

    private static volatile String dateInfo;

    private static AtomicLong sceneIndex = new AtomicLong(1L);
    private static AtomicLong departIndex = new AtomicLong(1L);
    private static AtomicLong stationIndex = new AtomicLong(1L);
    private static AtomicLong paperIndex = new AtomicLong(1L);
    private static AtomicLong questionIndex = new AtomicLong(1L);
    private static AtomicLong paperPolciyIndex = new AtomicLong(1L);


    static {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        String info = now.format(formatter);

        dateInfo = info;
    }

    public static void setDateInfo(String dateInfo) {
        CodeUtil.dateInfo = dateInfo;
    }

    public static void setSceneIndex(AtomicLong sceneIndex) {
        CodeUtil.sceneIndex = sceneIndex;
    }

    public static void setPaperIndex(AtomicLong paperIndex) {
        CodeUtil.paperIndex = paperIndex;
    }

    public static void setQuestionIndex(AtomicLong questionIndex) {
        CodeUtil.questionIndex = questionIndex;
    }

    public static void setPaperPolciyIndex(AtomicLong paperPolicyIndex){
        CodeUtil.questionIndex = paperPolicyIndex;
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


    /**
     * 将指定的数字转化为特定位数的字符串
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/29 9:54
     */
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
        Long index = paperIndex.getAndIncrement();
        return "P" + dateInfo + getSameLengthInfo(index, 5);
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
        Long index = questionIndex.getAndIncrement();

        return "Q" + getSameLengthInfo(questionType, 2) + getSameLengthInfo(index, 6);
    }

    /**
     * 获取岗位编号
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/12/1 11:06
     */
    public static String getStationCode() {
        Long index = stationIndex.getAndIncrement();
        return "ST" + dateInfo + getSameLengthInfo(index, 3);
    }

    public static String getDepartmentCode() {
        Long index = departIndex.getAndIncrement();
        return "D" + dateInfo + getSameLengthInfo(index, 3);
    }

    public static String getPaperPolicyCode(){
        Long index = paperPolciyIndex.getAndIncrement();
        return "PP" + dateInfo + getSameLengthInfo(index, 3);
    }
}
