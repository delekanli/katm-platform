package uz.katm.claim.domain.retailer;

public record OperationResult(
        String result,
        String resultMessage,
        String claimId
) {}
