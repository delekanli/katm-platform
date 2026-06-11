package uz.katm.client.domain.record;

import java.time.LocalDateTime;

/** Элемент истории кредитного запрета (datas.credit_ban + credit_ban_log). */
public record CreditBanHistoryItem(
        String head,
        String code,
        String hash,
        LocalDateTime actionDate,
        Integer isActive
) {
}
