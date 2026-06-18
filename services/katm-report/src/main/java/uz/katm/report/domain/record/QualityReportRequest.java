package uz.katm.report.domain.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Запрос отчёта по изменению качества КИ за период.
 * login/password требуются хранимкой DATAS.PKG_REPORTS.DISCOVERCO_129_XML_MOD
 * (первые два параметра); head/code берутся из JWT, не из тела.
 */
public record QualityReportRequest(
        @NotBlank String login,
        @NotBlank String password,
        @NotNull LocalDate dateFrom,
        @NotNull LocalDate dateTo
) {}
