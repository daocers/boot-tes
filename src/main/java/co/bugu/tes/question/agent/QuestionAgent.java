package co.bugu.tes.question.agent;

import co.bugu.tes.branch.domain.Branch;
import co.bugu.tes.branch.service.IBranchService;
import co.bugu.tes.department.domain.Department;
import co.bugu.tes.department.service.IDepartmentService;
import co.bugu.tes.question.dto.QuestionListDto;
import co.bugu.tes.questionBank.domain.QuestionBank;
import co.bugu.tes.questionBank.service.IQuestionBankService;
import co.bugu.tes.station.domain.Station;
import co.bugu.tes.station.service.IStationService;
import co.bugu.tes.user.domain.User;
import co.bugu.tes.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author daocers
 * @Date 2018/12/2:09:57
 * @Description:
 */
@Service
public class QuestionAgent {

    @Autowired
    IUserService userService;
    @Autowired
    IDepartmentService departmentService;
    @Autowired
    IBranchService branchService;
    @Autowired
    IStationService stationService;
    @Autowired
    IQuestionBankService questionBankService;


    public void processName(QuestionListDto dto){
        User user = userService.findById(dto.getCreateUserId());
        if (null != user) {
            dto.setCreateUserName(user.getName());
        }
        Department department = departmentService.findById(dto.getDepartmentId());
        if (null != department) {
            dto.setDepartmentName(department.getName());
        }
        Branch branch = branchService.findById(dto.getBranchId());
        if (null != department) {
            dto.setBranchName(branch.getName());
        }
        Station station = stationService.findById(dto.getStationId());
        if (null != station) {
            dto.setStationName(station.getName());
        }
        QuestionBank bank = questionBankService.findById(dto.getBankId());
        if(null != bank){
            dto.setBankName(bank.getName());
        }
    }
}
