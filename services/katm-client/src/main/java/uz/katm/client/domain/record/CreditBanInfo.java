package uz.katm.client.domain.record;

import java.time.LocalDateTime;

/** Информация о кредитном запрете по хэшу записи лога. */
public record CreditBanInfo(
        String identifier,
        String fullName,
        LocalDateTime actionDate,
        Integer status
) {
}
