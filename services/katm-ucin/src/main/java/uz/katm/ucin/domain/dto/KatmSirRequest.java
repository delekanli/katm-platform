package uz.katm.ucin.domain.dto;

import java.time.LocalDate;

/** Поиск клиента в КАТМ СИР по данным лица (DATAS.SMS_NOTIFICATION.GET_CLIENT_ID). */
public record KatmSirRequest(
        String identityCardSerial,
        String identityCardNumber,
        String pinfl,
        LocalDate dateBirth
) {
}
