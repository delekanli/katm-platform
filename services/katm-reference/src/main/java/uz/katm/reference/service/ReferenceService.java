package uz.katm.reference.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uz.katm.reference.domain.RefDto;
import uz.katm.reference.repository.ReferenceRepository;

import java.util.List;

/**
 * Справочники (перенос gov.uz.katm.core.ref.RefServiceImpl).
 * regions/localRegions/retailerModes — из БД; statuses/formats — статические списки.
 * Справочники меняются редко — результаты кэшируются.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReferenceService {

    private final ReferenceRepository repository;

    @Cacheable("ref-regions")
    public List<RefDto> getRegions() {
        log.debug("Справочник регионов");
        return repository.getRegions();
    }

    @Cacheable(value = "ref-local-regions", key = "#code")
    public List<RefDto> getLocalRegions(String code) {
        log.debug("Справочник районов региона {}", code);
        return repository.getLocalRegions(code);
    }

    @Cacheable("ref-retailer-modes")
    public List<RefDto> getRetailerModes() {
        log.debug("Справочник режимов ритейлеров");
        return repository.getRetailerModes();
    }

    public List<RefDto> getRetailerStatuses() {
        return List.of(
                new RefDto("0", "Не активный"),
                new RefDto("1", "Активный"),
                new RefDto("2", "Не активный (Задолженность)"),
                new RefDto("3", "Не активный (Договор анулирован)"),
                new RefDto("4", "Не активный (Не выполнены условия договора)")
        );
    }

    public List<RefDto> getFormats() {
        return List.of(
                new RefDto("XLSX", "Excel"),
                new RefDto("CSV", "Csv")
        );
    }
}
