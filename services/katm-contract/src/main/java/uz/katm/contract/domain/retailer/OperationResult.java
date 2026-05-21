package uz.katm.contract.domain.retailer;

public record OperationResult(
        String result,
        String resultMessage,
        String claimId
) {}
