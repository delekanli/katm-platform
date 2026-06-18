package uz.katm.client.domain.record;

/**
 * Данные субъекта по заявке (выборка DATAS.PKG_ONLINE.GET_DATA_BY_CLAIM), используемые ИНПС-потоком.
 * Из множества OUT-параметров процедуры здесь только то, что нужно для запроса в Халк банк:
 * ПИНФЛ и паспортные серия/номер. code/message — результат процедуры.
 */
public record ClientClaimData(
        String code,
        String message,
        String pinfl,
        String docSeries,
        String docNumber
) {
    public boolean isSuccess() {
        return "0".equals(code);
    }
}
