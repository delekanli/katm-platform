package uz.katm.report.domain.record;

import java.math.BigDecimal;

public record FicoResult(
        String clientId,
        BigDecimal score,
        String scoreDate
) {}
