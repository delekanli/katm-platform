package uz.katm.report.domain.record;

/**
 * Данные субъекта по заявке (DATAS.PKG_ONLINE.GET_DATA_BY_CLAIM). Дублирует биндинг из katm-client
 * (shared-DB паттерн). Поля ФИО/паспорт/телефон/дата рождения нужны телеком-шлюзам (Beeline by-doc).
 */
public record ClaimData(
        String code,
        String message,
        String pinfl,
        String tin,
        Integer isLegal,
        String phone,
        String docSeries,
        String docNumber,
        String firstName,
        String lastName,
        String birthDate
) {
    public boolean isSuccess() {
        return "0".equals(code);
    }
}
