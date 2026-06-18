package uz.katm.mip.domain.dto;

/**
 * Результат запроса о судимости. status: WAIT (принято/в обработке), OK (готово), FAIL (отказ).
 * token возвращается при инициации (формат "95_&lt;requestId&gt;") для последующего опроса статуса.
 */
public record ConvictionResult(String status, String message, String token, String report) {

    public static ConvictionResult wait(String message, String token) {
        return new ConvictionResult("WAIT", message, token, null);
    }

    public static ConvictionResult ok(String report) {
        return new ConvictionResult("OK", null, null, report);
    }

    public static ConvictionResult fail(String message) {
        return new ConvictionResult("FAIL", message, null, null);
    }
}
