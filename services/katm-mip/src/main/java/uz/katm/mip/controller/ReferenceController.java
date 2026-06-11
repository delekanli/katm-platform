package uz.katm.mip.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.katm.mip.service.ReferenceMipService;

/**
 * Адресные справочники МИП (фаза 2): регион / район / населённый пункт / улица.
 */
@Tag(name = "MIP Reference", description = "Адресные справочники МИП (SOAP)")
@RestController
@RequestMapping("/api/v1/mip/reference")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class ReferenceController {

    private final ReferenceMipService referenceMipService;

    @Operation(summary = "Регион по ID (GetRegionByRegionId)")
    @GetMapping("/region/{id}")
    public ResponseEntity<?> getRegion(@PathVariable long id) {
        return ResponseEntity.ok(referenceMipService.getRegionById(id));
    }

    @Operation(summary = "Район по ID (GetDistrictByDistrictId)")
    @GetMapping("/district/{id}")
    public ResponseEntity<?> getDistrict(@PathVariable long id) {
        return ResponseEntity.ok(referenceMipService.getDistrictById(id));
    }

    @Operation(summary = "Населённый пункт по ID (GetPlaceByPlaceId)")
    @GetMapping("/place/{id}")
    public ResponseEntity<?> getPlace(@PathVariable long id) {
        return ResponseEntity.ok(referenceMipService.getPlaceById(id));
    }

    @Operation(summary = "Улица по ID (GetStreetByStreetId)")
    @GetMapping("/street/{id}")
    public ResponseEntity<?> getStreet(@PathVariable long id) {
        return ResponseEntity.ok(referenceMipService.getStreetById(id));
    }
}
