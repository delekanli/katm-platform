package uz.katm.analytics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.analytics.repository.AnalyticsRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/** Аналитические отчёты (фаза 1). Тонкая обёртка над {@link AnalyticsRepository}. */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsRepository repository;

    public List<Map<String, Object>> detailPortfolio(String bank, Integer pos, boolean allCoa) {
        return repository.detailPortfolio(bank, pos, allCoa);
    }

    public List<Map<String, Object>> coaSummaryPortfolio(Integer pos, boolean allCoa) {
        return repository.coaSummaryPortfolio(pos, allCoa);
    }

    public List<Map<String, Object>> coaExtSummaryPortfolio(Integer pos, boolean allCoa) {
        return repository.coaExtSummaryPortfolio(pos, allCoa);
    }

    public List<Map<String, Object>> report04() {
        return repository.report04();
    }

    public List<Map<String, Object>> report06(LocalDate start, LocalDate end) {
        return repository.report06(start, end);
    }

    public List<Map<String, Object>> report09(LocalDate start, LocalDate end) {
        return repository.report09(start, end);
    }

    public List<Map<String, Object>> extReport09(LocalDate start, LocalDate end) {
        return repository.extReport09(start, end);
    }

    public List<Map<String, Object>> duplicates(String bank) {
        return repository.duplicates(bank);
    }

    public List<Map<String, Object>> usesCreditReports(LocalDate start, LocalDate end) {
        return repository.usesCreditReports(start, end);
    }

    public List<Map<String, Object>> webDemandsStat(LocalDate start, LocalDate end) {
        return repository.webDemandsStat(start, end);
    }
}
