package uz.katm.contract.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.contract.domain.bank.*;
import uz.katm.contract.exception.ContractServiceException;
import uz.katm.contract.repository.BankContractRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankContractService {

    private final BankContractRepository repository;

    public ProcedureResult declineClaim(String head, String code, DeclineClaimRequest request) {
        validate(request.claimId(), "claimId");
        log.info("Declining claim: {}", request.claimId());
        return repository.declineClaim(head, code, request);
    }

    public ClaimInfoResponse getClaim(String head, String code, GetClaimRequest request) {
        validate(request.claimId(), "claimId");
        log.info("Getting claim: {}", request.claimId());
        return repository.getClaim(head, code, request);
    }

    public String inquiryIndividual(InquiryIndividualRequest request) {
        validate(request.mipPin(), "mipPin");
        log.info("Inquiry individual: pin={}", request.mipPin());
        return repository.inquiryIndividual(request);
    }

    public String inquiryEntity(InquiryEntityRequest request) {
        validate(request.mipInn(), "mipInn");
        log.info("Inquiry entity: inn={}", request.mipInn());
        return repository.inquiryEntity(request);
    }

    public ProcedureResult registerLoan(String head, String code, RegisterLoanRequest request) {
        validate(request.contractId(), "contractId");
        log.info("Registering loan contract: {}", request.contractId());
        return repository.registerLoan(head, code, request);
    }

    public ProcedureResult registerLease(String head, String code, RegisterLeaseRequest request) {
        validate(request.leasingId(), "leasingId");
        log.info("Registering lease contract: {}", request.leasingId());
        return repository.registerLease(head, code, request);
    }

    public ProcedureResult registerFactoring(String head, String code, RegisterFactoringRequest request) {
        validate(request.factoringId(), "factoringId");
        log.info("Registering factoring contract: {}", request.factoringId());
        return repository.registerFactoring(head, code, request);
    }

    public ProcedureResult addContractRepayment(String head, String code, ContractRepaymentRequest request) {
        validate(request.contractId(), "contractId");
        if (request.repaymentArray() == null || request.repaymentArray().isEmpty()) {
            throw new ContractServiceException("Repayment array must not be empty");
        }
        log.info("Adding repayment for contract: {}", request.contractId());
        return repository.addContractRepayment(head, code, request);
    }

    public ProcedureResult addContractRepaymentDetails(String head, String code, ContractRepaymentDetailsRequest request) {
        validate(request.contractId(), "contractId");
        if (request.repaymentDetArray() == null || request.repaymentDetArray().isEmpty()) {
            throw new ContractServiceException("Repayment details array must not be empty");
        }
        log.info("Adding repayment details for contract: {}", request.contractId());
        return repository.addContractRepaymentDetails(head, code, request);
    }

    public ProcedureResult addSubjectRepaymentDetails(String head, String code, SubjectRepaymentDetailsRequest request) {
        validate(request.contractId(), "contractId");
        if (request.lomRepayDetailArray() == null || request.lomRepayDetailArray().isEmpty()) {
            throw new ContractServiceException("Subject repayment details array must not be empty");
        }
        log.info("Adding subject repayment details for contract: {}", request.contractId());
        return repository.addSubjectRepaymentDetails(head, code, request);
    }

    public ProcedureResult addContractSchedule(String head, String code, ContractScheduleRequest request) {
        validate(request.contractId(), "contractId");
        if (request.scheduleArray() == null || request.scheduleArray().isEmpty()) {
            throw new ContractServiceException("Schedule array must not be empty");
        }
        log.info("Adding schedule for contract: {}", request.contractId());
        return repository.addContractSchedule(head, code, request);
    }

    public ProcedureResult addAccountStatusInfo(String head, String code, AccountStatusInfoRequest request) {
        validate(request.contractId(), "contractId");
        if (request.accStatusDetailArray() == null || request.accStatusDetailArray().isEmpty()) {
            throw new ContractServiceException("Account status array must not be empty");
        }
        log.info("Adding account status info for contract: {}", request.contractId());
        return repository.addAccountStatusInfo(head, code, request);
    }

    public ProcedureResult addRelatedEntitiesRegistration(String head, String code, RelatedEntityRequest request) {
        validate(request.claimId(), "claimId");
        log.info("Adding related entities for claim: {}", request.claimId());
        return repository.addRelatedEntitiesRegistration(head, code, request);
    }

    public ProcedureResult addPledgeOwnerRegistration(String head, String code, PledgeOwnerRequest request) {
        validate(request.ownerId(), "ownerId");
        log.info("Adding pledge owner: {}", request.ownerId());
        return repository.addPledgeOwnerRegistration(head, code, request);
    }

    public ProcedureResult addLoanSecurityRegistration(String head, String code, LoanSecurityRequest request) {
        validate(request.guaranteeId(), "guaranteeId");
        log.info("Adding loan security: {}", request.guaranteeId());
        return repository.addLoanSecurityRegistration(head, code, request);
    }

    public ProcedureResult addLizContractSchedule(String head, String code, ContractLeaseScheduleRequest request) {
        validate(request.leasingId(), "leasingId");
        if (request.scheduleArray() == null || request.scheduleArray().isEmpty()) {
            throw new ContractServiceException("Lease schedule array must not be empty");
        }
        log.info("Adding lease schedule for contract: {}", request.leasingId());
        return repository.addLizContractSchedule(head, code, request);
    }

    public ProcedureResult addContractLeaseRepayment(String head, String code, ContractLeaseRepaymentRequest request) {
        validate(request.leasingId(), "leasingId");
        if (request.repaymentArray() == null || request.repaymentArray().isEmpty()) {
            throw new ContractServiceException("Lease repayment array must not be empty");
        }
        log.info("Adding lease repayment for contract: {}", request.leasingId());
        return repository.addContractLeaseRepayment(head, code, request);
    }

    private void validate(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new ContractServiceException("Field '" + field + "' must not be blank");
        }
    }
}
