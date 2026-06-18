package uz.katm.mip.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.katm.common.dto.ApiResponse;
import uz.katm.mip.service.GnkMipService;

/**
 * Отчёты ГНК через ПЦД (перенос bkiService GNK-методов). Субъект передаётся ПИНФЛ/ИНН напрямую
 * (как и прочие MIP-эндпоинты); разрешение claimId→субъект — на стороне вызывающего сервиса.
 */
@RestController
@RequestMapping("/api/v1/mip/gnk")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
@Tag(name = "MIP GNK", description = "Налоговые отчёты ГНК через ПЦД")
public class GnkController {

    private final GnkMipService gnkMipService;

    @Operation(summary = "Сведения о заработной плате (ГНК)")
    @GetMapping("/salary")
    public ResponseEntity<ApiResponse<String>> getSalary(
            @RequestParam(required = false) String pinfl,
            @RequestParam(required = false) String tin,
            @RequestParam(required = false) String lang) {
        return ResponseEntity.ok(ApiResponse.ok(gnkMipService.getSalary(pinfl, tin, lang)));
    }

    @Operation(summary = "Сведения о полученных дивидендах (ГНК)")
    @GetMapping("/dividend")
    public ResponseEntity<ApiResponse<String>> getDividend(
            @RequestParam(required = false) String pinfl,
            @RequestParam(required = false) String tin,
            @RequestParam(required = false) String lang,
            @RequestParam(defaultValue = "2023") int year) {
        return ResponseEntity.ok(ApiResponse.ok(gnkMipService.getDividend(pinfl, tin, lang, year)));
    }

    @Operation(summary = "Сведения о сданном в аренду имуществе (ГНК)")
    @GetMapping("/rent")
    public ResponseEntity<ApiResponse<String>> getRent(
            @NotBlank @RequestParam String pinfl,
            @RequestParam(required = false) String lang) {
        return ResponseEntity.ok(ApiResponse.ok(gnkMipService.getRent(pinfl, lang)));
    }

    @Operation(summary = "Бухгалтерский баланс, форма 1 (ГНК)")
    @GetMapping("/account-report-1")
    public ResponseEntity<ApiResponse<String>> getAccountReport1(
            @NotBlank @RequestParam String tin,
            @RequestParam(required = false) String lang,
            @RequestParam(defaultValue = "2023") int year,
            @RequestParam(defaultValue = "1") int period) {
        return ResponseEntity.ok(ApiResponse.ok(gnkMipService.getAccountReport1(tin, lang, year, period)));
    }

    @Operation(summary = "Финансовая отчётность, форма 2 (ГНК)")
    @GetMapping("/account-report-2")
    public ResponseEntity<ApiResponse<String>> getAccountReport2(
            @NotBlank @RequestParam String tin,
            @RequestParam(required = false) String lang,
            @RequestParam(defaultValue = "2023") int year,
            @RequestParam(defaultValue = "1") int period) {
        return ResponseEntity.ok(ApiResponse.ok(gnkMipService.getAccountReport2(tin, lang, year, period)));
    }

    @Operation(summary = "Сведения об индивидуальном предпринимателе (ГНК)")
    @GetMapping("/ip-info")
    public ResponseEntity<ApiResponse<String>> getIpInfo(
            @NotBlank @RequestParam String pinfl,
            @RequestParam(required = false) String lang) {
        return ResponseEntity.ok(ApiResponse.ok(gnkMipService.getIpInfo(pinfl, lang)));
    }

    @Operation(summary = "Общие сведения ГНК")
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<String>> getGnkInfo(
            @NotBlank @RequestParam String pinfl) {
        return ResponseEntity.ok(ApiResponse.ok(gnkMipService.getGnkInfo(pinfl)));
    }

    @Operation(summary = "Информация о физлице (ГНК)")
    @GetMapping("/pe-info")
    public ResponseEntity<ApiResponse<String>> getPeInfo(
            @RequestParam(required = false) String pinfl,
            @RequestParam(required = false) String tin,
            @RequestParam(required = false) String lang) {
        return ResponseEntity.ok(ApiResponse.ok(gnkMipService.getPeInfo(pinfl, tin, lang)));
    }

    @Operation(summary = "Информация о юрлице (ГНК)")
    @GetMapping("/juridical-info")
    public ResponseEntity<ApiResponse<String>> getJuridicalInfo(
            @NotBlank @RequestParam String tin,
            @RequestParam(required = false) String lang) {
        return ResponseEntity.ok(ApiResponse.ok(gnkMipService.getJuridicalInfo(tin, lang)));
    }

    @Operation(summary = "Имущество физлица (ГНК)")
    @GetMapping("/pe-subject")
    public ResponseEntity<ApiResponse<String>> getPeSubject(
            @NotBlank @RequestParam String pinfl,
            @RequestParam(required = false) String lang,
            @RequestParam(defaultValue = "2023") int year) {
        return ResponseEntity.ok(ApiResponse.ok(gnkMipService.getPeSubject(pinfl, lang, year)));
    }

    @Operation(summary = "Отчёт по ЕНП (ГНК)")
    @GetMapping("/tax-report")
    public ResponseEntity<ApiResponse<String>> getTaxReport(
            @NotBlank @RequestParam String tin,
            @RequestParam(required = false) String lang,
            @RequestParam(defaultValue = "2023") int year,
            @RequestParam(defaultValue = "1") int period) {
        return ResponseEntity.ok(ApiResponse.ok(gnkMipService.getTaxReport(tin, lang, year, period)));
    }

    @Operation(summary = "Количество работников (ГНК)")
    @GetMapping("/staff-count")
    public ResponseEntity<ApiResponse<String>> getStaffCount(
            @NotBlank @RequestParam String tin,
            @RequestParam(required = false) String lang,
            @RequestParam(defaultValue = "2023") int year) {
        return ResponseEntity.ok(ApiResponse.ok(gnkMipService.getStaffCount(tin, lang, year)));
    }

    @Operation(summary = "База налога на добавленную стоимость (ГНК)")
    @GetMapping("/nds")
    public ResponseEntity<ApiResponse<String>> getNds(
            @NotBlank @RequestParam String tin,
            @RequestParam(required = false) String lang) {
        return ResponseEntity.ok(ApiResponse.ok(gnkMipService.getNds(tin, lang)));
    }

    @Operation(summary = "Сведения о самозанятом лице (ГНК)")
    @GetMapping("/self-employment")
    public ResponseEntity<ApiResponse<String>> getSelfEmployment(
            @RequestParam(required = false) String pinfl,
            @RequestParam(required = false) String tin,
            @RequestParam(required = false) String lang) {
        return ResponseEntity.ok(ApiResponse.ok(gnkMipService.getSelfEmployment(pinfl, tin, lang)));
    }
}
