package uz.katm.system.domain.record;

/** Отчёт для повторной отправки в E-GOV (claim_id + содержимое отчёта). */
public record EgovResendItem(
        String claimId,
        String report
) {
}
