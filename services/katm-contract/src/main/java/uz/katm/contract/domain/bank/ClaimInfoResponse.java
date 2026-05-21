package uz.katm.contract.domain.bank;

public record ClaimInfoResponse(
        String code,
        String claimId,
        String claimDate,
        String claimStatus,
        String agreementNumber,
        String agreementDate,
        String clientId,
        String passportSerial,
        String passportNumber,
        String pin,
        String phone,
        String creditAmount,
        String creditCurrency,
        String creditEnd,
        String region,
        String localRegion,
        String address,
        String surname,
        String name,
        String middleName,
        String fullName,
        String male,
        String birthDate,
        String tin
) {}
