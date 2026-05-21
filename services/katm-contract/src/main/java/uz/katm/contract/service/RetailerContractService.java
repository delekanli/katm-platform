package uz.katm.contract.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.katm.contract.domain.retailer.*;
import uz.katm.contract.exception.ContractServiceException;
import uz.katm.contract.repository.RetailerContractRepository;

import java.util.List;
import java.util.Map;

@Service
public class RetailerContractService {

    private static final Logger log = LoggerFactory.getLogger(RetailerContractService.class);

    private final RetailerContractRepository repository;

    public RetailerContractService(RetailerContractRepository repository) {
        this.repository = repository;
    }

    public OperationResult registerContract(String code, ContractRegistrationRequest request) {
        log.info("registerContract: code={}, contractId={}", code, request.contractId());
        return repository.addContract(code, request);
    }

    public OperationResult addContractSchedule(String code, ScheduleAddRequest request) {
        if (request.planArray() == null || request.planArray().isEmpty()) {
            throw new ContractServiceException("planArray must not be empty");
        }
        log.info("addContractSchedule: code={}, contractId={}", code, request.contractId());
        return repository.addContractPlan(code, request);
    }

    public OperationResult addContractRepayment(String code, RepaymentAddRequest request) {
        if (request.repaymentArray() == null || request.repaymentArray().isEmpty()) {
            throw new ContractServiceException("repaymentArray must not be empty");
        }
        log.info("addContractRepayment: code={}, contractId={}", code, request.contractId());
        return repository.addContractRepayment(code, request);
    }

    public List<Map<String, Object>> getRepayments(String head, String code, ContractQueryRequest request) {
        log.info("getRepayments: code={}, contractId={}", code, request.contractId());
        return repository.getRepayments(head, code, request);
    }

    public List<Map<String, Object>> getRepaymentsSchedule(String head, String code, ContractQueryRequest request) {
        log.info("getRepaymentsSchedule: code={}, contractId={}", code, request.contractId());
        return repository.getRepaymentsSchedule(head, code, request);
    }

    public Map<String, Object> getContractInfo(String head, String code, ContractQueryRequest request) {
        log.info("getContractInfo: code={}, contractId={}", code, request.contractId());
        return repository.getContractInfo(head, code, request);
    }
}
