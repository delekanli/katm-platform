package uz.katm.system.domain.dto;

import java.time.LocalDate;

/** Лог запроса шлюза (DATAS.PKG_ONLINE.ADD_GATEWAY_REQUESTS_LOG). */
public record GatewayRequestLogRequest(
        Integer id,
        Integer source,
        String head,
        String code,
        String claimId,
        String countId,
        LocalDate requestDate,
        String request,
        String response
) {
}
