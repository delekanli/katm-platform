package uz.katm.claim.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.claim.client.MipClient;
import uz.katm.claim.domain.retailer.*;
import uz.katm.claim.exception.ClaimServiceException;
import uz.katm.claim.repository.RetailerClaimRepository;
import uz.katm.claim.repository.RetailerClaimRepository.InitiateResult;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetailerClaimService {

    private final RetailerClaimRepository repository;
    private final MipClient mipClient;

    public OperationResult registerClaim(String head, String code, ClaimRegistrationRequest req) {
        log.info("registerClaim: code={}, claimId={}", code, req.claimId());
        InitiateResult init = repository.initiateClaim(head, code, req);
        checkResult(init.code(), init.message());

        MipClient.CitizenData mipData = mipClient.getCitizenInfo(req.pinfl());
        return repository.addClaim(
                init.initialId(), code, req.claimId(),
                null, null, null, null,
                null, null, null,
                req.inn(), 0,
                extractMipState(mipData), extractMipResult(mipData),
                req.docType()
        );
    }

    public OperationResult registerClaimExt(String head, String code, ClaimRegistrationExtRequest req) {
        log.info("registerClaimExt: code={}, claimId={}", code, req.claimId());
        ClaimRegistrationRequest initReq = new ClaimRegistrationRequest(
                null,
                req.docSeries(), req.docNumber(), req.docType(),
                req.claimId(), req.claimDate(),
                req.agreementId(), req.agreementDate(),
                req.region(), req.localRegion(), req.address(), req.inn()
        );
        InitiateResult init = repository.initiateIndClaim(code, initReq);
        checkResult(init.code(), init.message());

        return repository.addClaim(
                init.initialId(), code, req.claimId(),
                req.lastName(), req.firstName(), req.middleName(),
                req.male(), req.birthDate(), req.issueDocDate(), req.expiredDocDate(),
                req.inn(), 0, "MANUAL", "EXT", req.docType()
        );
    }

    public OperationResult registerClaimTrusted(String head, String code, ClaimRegistrationTrustRequest req) {
        log.info("registerClaimTrusted: code={}, claimId={}", code, req.claimId());
        ClaimRegistrationRequest initReq = new ClaimRegistrationRequest(
                req.pinfl(),
                req.docSeries(), req.docNumber(), req.docType(),
                req.claimId(), req.claimDate(),
                req.agreementId(), req.agreementDate(),
                req.region(), req.localRegion(), req.address(), null
        );
        InitiateResult init = repository.initiateClaim(head, code, initReq);
        checkResult(init.code(), init.message());

        MipClient.CitizenData mipData = mipClient.getCitizenInfo(req.pinfl());
        return repository.addClaim(
                init.initialId(), code, req.claimId(),
                null, null, null, null,
                null, null, null,
                null, 0,
                extractMipState(mipData), extractMipResult(mipData),
                req.docType()
        );
    }

    public OperationResult registerLegalClaim(String head, String code, ClaimLegalRegistrationRequest req) {
        log.info("registerLegalClaim: code={}, inn={}, claimId={}", code, req.inn(), req.claimId());
        InitiateResult init = repository.initiateLegalClaim(head, code, req);
        checkResult(init.code(), init.message());

        MipClient.LegalData legalData = mipClient.getLegalInfo(req.inn());
        String mipState = legalData != null ? "1" : "0";
        String mipResult = legalData != null ? "OK" : "NOT_FOUND";
        String fullName = legalData != null ? legalData.getLeNameUz() : null;
        return repository.addLegalClaim(init.initialId(), head, code, req, fullName, mipState, mipResult);
    }

    public OperationResult registerLegalClaimExt(String head, String code, ClaimLegalRegistrationRequest req) {
        log.info("registerLegalClaimExt (manual): code={}, inn={}", code, req.inn());
        InitiateResult init = repository.initiateLegalClaim(head, code, req);
        checkResult(init.code(), init.message());
        return repository.addLegalClaim(init.initialId(), head, code, req, null, "MANUAL", "EXT");
    }

    public OperationResult registerOrgClaim(String head, String code, ClaimLegalRegistrationRequest req) {
        log.info("registerOrgClaim: code={}, inn={}, claimId={}", code, req.inn(), req.claimId());
        InitiateResult init = repository.initiateLegalClaim(head, code, req);
        checkResult(init.code(), init.message());

        MipClient.LegalData legalData = mipClient.getLegalInfo(req.inn());
        String mipState = legalData != null ? "1" : "0";
        String mipResult = legalData != null ? "OK" : "NOT_FOUND";
        String fullName = legalData != null ? legalData.getLeNameUz() : null;
        return repository.addOrgClaim(init.initialId(), head, code, req, fullName, mipState, mipResult);
    }

    public OperationResult registerOrgClaimExt(String head, String code, ClaimLegalRegistrationRequest req) {
        log.info("registerOrgClaimExt (manual): code={}, inn={}", code, req.inn());
        InitiateResult init = repository.initiateLegalClaim(head, code, req);
        checkResult(init.code(), init.message());
        return repository.addOrgClaim(init.initialId(), head, code, req, null, "MANUAL", "EXT");
    }

    public OperationResult rejectClaim(String head, String code, RejectClaimRequest req) {
        log.info("rejectClaim: code={}, claimId={}", code, req.claimId());
        return repository.rejectClaim(code, req);
    }

    public OperationResult rejectLegalClaim(String head, String code, RejectClaimRequest req) {
        log.info("rejectLegalClaim: head={}, code={}, claimId={}", head, code, req.claimId());
        return repository.rejectLegalClaim(head, code, req);
    }

    private void checkResult(String code, String message) {
        if (!"0".equals(code)) {
            throw new ClaimServiceException(code, message);
        }
    }

    private String extractMipState(MipClient.CitizenData data) {
        if (data == null || data.getPinppAddressResult() == null) return "0";
        var res = data.getPinppAddressResult().getPResult();
        return res != null ? res.toString() : "0";
    }

    private String extractMipResult(MipClient.CitizenData data) {
        if (data == null || data.getPinppAddressResult() == null) return null;
        return data.getPinppAddressResult().getData();
    }
}
