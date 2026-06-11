package uz.katm.ucin.domain.dto;

import java.time.LocalDate;

/** Инициализация клиента-физлица UCIN (DATAS.PKG_WEB_INDIVIDUAL.INITIATE_CLIENT). */
public record InitiateClientRequest(
        String pinfl,
        String identityCardSerial,
        String identityCardNumber,
        LocalDate identityCardDate,
        String inn,
        String region,
        String localRegion,
        String familyName,
        String name,
        String patronymic,
        LocalDate dateBirth,
        String phone,
        String postAddress,
        String liveAddress,
        String resident,
        String sex,
        String ip
) {
}
