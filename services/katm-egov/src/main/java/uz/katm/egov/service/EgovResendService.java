package uz.katm.egov.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.egov.client.EpiguClient;
import uz.katm.egov.domain.dto.EpiguReportRequest;
import uz.katm.egov.domain.record.EgovResendItem;
import uz.katm.egov.repository.EgovReportRepository;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 * Переотправка неотправленных кредитных отчётов в E-GOV Epigu.
 * Перенос gov.uz.ucin.katm.api.scheduler.ResendEgovReportsScheduler: берёт отчёты на повтор,
 * шлёт в Epigu, при успехе помечает web-заявку отправленной (status=1).
 * Запускается по триггеру из katm-scheduler (Kafka JOB_EGOV_RESEND).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EgovResendService {

    private final EgovReportRepository repository;
    private final EpiguClient epiguClient;

    public void resendReports() {
        List<EgovResendItem> reports = repository.getReportsForEgovResend();
        log.info("E-GOV resend: к отправке {} отчётов", reports.size());
        for (EgovResendItem item : reports) {
            String claimId = item.claimId();
            if (item.report() == null) {
                log.warn("E-GOV resend: пустой отчёт по заявке {}, пропуск", claimId);
                continue;
            }
            String base64 = Base64.getEncoder()
                    .encodeToString(item.report().getBytes(StandardCharsets.UTF_8));
            if (epiguClient.sendReport(claimId, EpiguReportRequest.of(base64))) {
                repository.updateWebClaimStatusEgov(claimId);
                log.info("E-GOV resend: заявка {} помечена отправленной", claimId);
            }
        }
    }
}
