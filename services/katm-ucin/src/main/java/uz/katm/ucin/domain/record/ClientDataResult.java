package uz.katm.ucin.domain.record;

/** Данные клиента по clientId (GET_DATA_BY_CLIENT_ID). */
public record ClientDataResult(
        String pinfl,
        String inn,
        String docSeries,
        String docNumber,
        Integer isLegal,
        String result,
        String message
) {
}
