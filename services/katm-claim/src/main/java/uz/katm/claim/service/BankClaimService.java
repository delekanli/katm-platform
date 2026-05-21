package uz.katm.claim.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.claim.domain.ProcedureResult;
import uz.katm.claim.domain.bank.*;
import uz.katm.claim.exception.ClaimServiceException;
import uz.katm.claim.repository.BankClaimRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankClaimService {

    private final BankClaimRepository repository;

    public ProcedureResult declineClaim(String head, String code, DeclineClaimRequest request) {
        validate(request.claimId(), "claimId");
        log.info("Declining claim: {}", request.claimId());
        ProcedureResult result = repository.declineClaim(head, code, request);
        log.info("Decline claim result: code={}", result.code());
        return result;
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

    private void validate(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new ClaimServiceException("Field '" + field + "' must not be blank");
        }
    }
}
