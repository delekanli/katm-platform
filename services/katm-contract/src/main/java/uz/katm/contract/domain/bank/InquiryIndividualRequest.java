package uz.katm.contract.domain.bank;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record InquiryIndividualRequest(
        @NotBlank String ip,
        @NotBlank String method,
        @NotBlank String contentType,
        @NotBlank @Size(min = 14, max = 14) @Pattern(regexp = "[0-9]+") String mipPin,
        @NotBlank String mipSurname,
        @NotBlank String mipName,
        String mipPatronymic,
        @NotNull LocalDate mipBirthdate,
        @NotBlank String mipSex,
        @NotBlank String queryRequestDateTime,
        String queryRequestContent,
        String mipState,
        String mipRequestTime,
        String mipResponseTime,
        String mipRequestContent,
        String mipResponseContent
) {}
