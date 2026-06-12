package uz.katm.mip.domain;

import java.util.Objects;

/**
 * Коды ответов госсервисов МИП (перенесено из монолита gov.uz.wsclient.data.MipApiResponseCode).
 * Используются для классификации результата внешнего вызова до маппинга в {@code ApiResponse}.
 */
public enum MipApiResponseCode {
    SUCCESS("1", "Данные найдены"),
    NOT_AVAILABLE("0", "Сервис временно недоступен"),
    INTERNAL_ERROR("2", "Ошибка при обработке запроса"),
    DATA_NOT_FOUND("4", "Данные не найдены"),
    DATA_REQUIRED("201", "Обязательные данные отсутствуют"),
    WRONG_FORMAT("202", "Неправильный формат данных"),
    SERVER_EXCEPTION("500", "Ошибка в сервисе поставщика услуг");

    private final String code;
    private final String message;

    MipApiResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static MipApiResponseCode getByCode(String code) {
        for (MipApiResponseCode e : values()) {
            if (Objects.equals(code, e.code)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Не найден код сообщения МИП: " + code);
    }
}