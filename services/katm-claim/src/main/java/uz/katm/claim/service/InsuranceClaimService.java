package uz.katm.claim.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.claim.domain.ProcedureResult;
import uz.katm.claim.domain.insurance.AddClaimRequest;
import uz.katm.claim.domain.insurance.ClaimResult;
import uz.katm.claim.domain.insurance.InitiateClaimRequest;
import uz.katm.claim.exception.ClaimServiceException;
import uz.katm.claim.repository.InsuranceClaimRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsuranceClaimService {

    private final InsuranceClaimRepository repository;

    public ClaimResult initiateClaim(String code, InitiateClaimRequest request) {
        log.info("Initiating insurance claim: {}", request.claimId());
        ClaimResult result = repository.initiateClaim(code, request);
        assertSuccess(result.code(), result.message());
        return result;
    }

    public ProcedureResult addClaim(String code, AddClaimRequest request) {
        log.info("Adding insurance claim: {}", request.claimId());
        ProcedureResult result = repository.addClaim(code, request);
        assertSuccess(result.code(), result.message());
        return result;
    }

    public ProcedureResult rejectClaim(String claimId, String code, String reason) {
        log.info("Rejecting insurance claim: {}", claimId);
        ProcedureResult result = repository.rejectClaim(claimId, code, reason);
        assertSuccess(result.code(), result.message());
        return result;
    }

    private void assertSuccess(String code, String message) {
        if (!"0".equals(code)) {
            throw new ClaimServiceException(code, message);
        }
    }
}
