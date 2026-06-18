package uz.katm.egov.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Тело запроса в E-GOV Epigu для (пере)отправки кредитного отчёта.
 * Перенос gov.uz.ucin.katm.api.data.EgovEpiguSendReportParams / FileUploadGosFormGetCreditHistory / GosFileUpload —
 * структура JSON сохранена 1:1.
 */
public record EpiguReportRequest(
        @JsonProperty("FileUploadGosFormGetCreditHistory") FileUpload fileUploadGosFormGetCreditHistory) {

    public record FileUpload(@JsonProperty("gos_file_upload") GosFile gosFileUpload) {
    }

    public record GosFile(
            @JsonProperty("ext") String ext,
            @JsonProperty("target") String target,
            @JsonProperty("file") String file) {
    }

    /** Собирает запрос с base64-содержимым HTML-отчёта (ext=html, target=file — как в монолите). */
    public static EpiguReportRequest of(String base64Report) {
        return new EpiguReportRequest(new FileUpload(new GosFile("html", "file", base64Report)));
    }
}
