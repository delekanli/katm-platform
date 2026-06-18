package uz.katm.notification.domain.record;

/** Статус подписки FREEZE с датой (перенос StatusDto монолита: status + statusDate). */
public record FreezeStatus(Integer status, String statusDate) {
}
