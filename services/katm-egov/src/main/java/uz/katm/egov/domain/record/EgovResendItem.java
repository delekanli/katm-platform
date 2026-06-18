package uz.katm.egov.domain.record;

/** Отчёт для повторной отправки в E-GOV Epigu: claim_id + HTML-содержимое отчёта. */
public record EgovResendItem(String claimId, String report) {
}
