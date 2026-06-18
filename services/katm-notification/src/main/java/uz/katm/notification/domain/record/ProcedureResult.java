package uz.katm.notification.domain.record;

/** Результат процедуры/функции уведомлений: код + сообщение (перенос CommonResult монолита). */
public record ProcedureResult(String code, String message) {
    private static final String SUCCESS_CODE = "0";

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }
}
