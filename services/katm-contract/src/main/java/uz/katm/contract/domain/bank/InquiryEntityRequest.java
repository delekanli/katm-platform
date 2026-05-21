package uz.katm.contract.domain.bank;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record InquiryEntityRequest(
        @NotBlank String ip,
        @NotBlank String method,
        @NotBlank String contentType,
        @NotBlank String mipFullName,
        @NotBlank @Size(min = 9, max = 9) @Pattern(regexp = "[0-9]+") String mipInn,
        String mipOkpo,
        @NotBlank String queryRequestDateTime,
        String queryRequestContent,
        String mipState,
        String mipRequestTime,
        String mipResponseTime,
        String mipRequestContent,
        String mipResponseContent
) {}
