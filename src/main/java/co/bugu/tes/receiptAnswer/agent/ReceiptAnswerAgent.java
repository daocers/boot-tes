package co.bugu.tes.receiptAnswer.agent;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.receipt.domain.Receipt;
import co.bugu.tes.receipt.service.IReceiptService;
import co.bugu.tes.receiptAnswer.domain.ReceiptAnswer;
import co.bugu.tes.receiptAnswer.service.IReceiptAnswerService;
import co.bugu.tes.receiptRecord.domain.ReceiptRecord;
import co.bugu.tes.receiptRecord.service.IReceiptRecordService;
import co.bugu.tes.scene.service.ISceneService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author daocers
 * @Date 2019/4/24:13:47
 * @Description:
 */
@Service
public class ReceiptAnswerAgent {
    private Logger logger = LoggerFactory.getLogger(ReceiptAnswerAgent.class);
    @Autowired
    IReceiptService receiptService;
    @Autowired
    IReceiptAnswerService receiptAnswerService;
    @Autowired
    IReceiptRecordService receiptRecordService;
    @Autowired
    ISceneService sceneService;

    public boolean commitReceiptPaper(Long sceneId, Integer receiptCount, Long userId, Integer seconds, List<Integer> answers) {
//        1获取本场的凭条信息

//        2 答案入库 (ReceiptAnswer)
//        3 考试结果入库（ReceiptRecord）

        Receipt query = new Receipt();
        query.setSceneId(sceneId);
        List<Receipt> receipts = receiptService.findByCondition(query);
        if (CollectionUtils.isEmpty(receipts)) {
            logger.warn("sceneId: {} 没有找到凭条信息", sceneId);
            return false;
        }

        int size = answers.size();
        int idx = 0;
        List<ReceiptAnswer> list = new ArrayList<>();

        int right = 0;
        for (Receipt receipt : receipts) {
            Long id = receipt.getId();
            Integer number = receipt.getNumber();
            Integer no = receipt.getNo();
            if (size > idx) {
                ReceiptAnswer item = new ReceiptAnswer();
                item.setAnswer(answers.get(idx));
                item.setCreateUserId(userId);
                item.setUpdateUserId(userId);
                item.setUserId(userId);
                item.setNo(no);
                item.setNumber(number);
                item.setSceneId(sceneId);
                item.setReceiptId(id);
                if (item.getNumber().equals(item.getAnswer())) {
                    right++;
                }
                list.add(item);
            } else {
                break;
            }

        }
        receiptAnswerService.batchAdd(list);

        ReceiptRecord record = new ReceiptRecord();
        record.setSceneId(sceneId);
        record.setIsDel(DelFlagEnum.NO.getCode());
        record.setRightCount(right);
        record.setFalseCount(receiptCount - right);
        record.setRate((double) right * 100 / receiptCount);
        record.setSeconds(seconds);
        record.setCount(receiptCount);
        record.setUserId(userId);
        record.setUpdateUserId(userId);
        record.setCreateUserId(userId);
        receiptRecordService.add(record);
        return true;

    }
}
