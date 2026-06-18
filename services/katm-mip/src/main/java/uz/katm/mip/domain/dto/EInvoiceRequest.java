package uz.katm.mip.domain.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Запрос выручки по чекам ККМ (E-Invoice). signature — ЭЦП в формате pkcs7 (большая, поэтому POST).
 * Для физлица субъект — ПИНФЛ (или ИНН), для юрлица — ИНН. Период/страница опциональны.
 */
public record EInvoiceRequest(
        String pin,
        String tin,
        @NotBlank String signature,
        String page,
        String dateBegin,
        String dateEnd,
        String lang
) {
}
