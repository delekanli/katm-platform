package uz.katm.ucin.domain.dto;

import jakarta.validation.constraints.NotBlank;

/** Инициализация клиента-юрлица UCIN (DATAS.PKG_WEB_INDIVIDUAL.INITIATE_LEGAL_CLIENT). */
public record InitiateLegalClientRequest(
        @NotBlank String inn,
        String region,
        String localRegion,
        String fullName,
        String postAddress,
        String phone,
        Integer resident,
        Integer clientTypeId,
        String ownerForm,
        Integer government,
        String okpo,
        String hbranch,
        String industry,
        String ip
) {
}
