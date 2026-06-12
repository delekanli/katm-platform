package uz.katm.mip.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uz.katm.mip.ws.MipPortConfigurer;
import uz.katm.mip.ws.district.GetDistrictByDistrictId;
import uz.katm.mip.ws.district.GetDistrictByDistrictIdPortType;
import uz.katm.mip.ws.district.GetDistrictByDistrictIdResponse;
import uz.katm.mip.ws.district.GetDistrictByDistrictIdService;
import uz.katm.mip.ws.place.GetPlaceByPlaceId;
import uz.katm.mip.ws.place.GetPlaceByPlaceIdPortType;
import uz.katm.mip.ws.place.GetPlaceByPlaceIdResponse;
import uz.katm.mip.ws.place.GetPlaceByPlaceIdService;
import uz.katm.mip.ws.region.GetRegionByRegionId;
import uz.katm.mip.ws.region.GetRegionByRegionIdPortType;
import uz.katm.mip.ws.region.GetRegionByRegionIdResponse;
import uz.katm.mip.ws.region.GetRegionByRegionIdService;
import uz.katm.mip.ws.street.GetStreetByStreetId;
import uz.katm.mip.ws.street.GetStreetByStreetIdPortType;
import uz.katm.mip.ws.street.GetStreetByStreetIdResponse;
import uz.katm.mip.ws.street.GetStreetByStreetIdService;


/**
 * Адресные справочники МИП (фаза 2 миграции): регион/район/населённый пункт/улица.
 * Данные меняются редко — агрессивно кэшируются по идентификатору.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReferenceMipService {

    private final MipPortConfigurer portConfigurer;

    @Value("${katm.mip.region-endpoint:}")
    private String regionEndpoint;

    @Value("${katm.mip.district-endpoint:}")
    private String districtEndpoint;

    @Value("${katm.mip.place-endpoint:}")
    private String placeEndpoint;

    @Value("${katm.mip.street-endpoint:}")
    private String streetEndpoint;

    @Cacheable(value = "mip-ref-region", key = "#regionId")
    public GetRegionByRegionIdResponse getRegionById(long regionId) {
        log.info("SOAP GetRegionByRegionId: id={}", regionId);

        GetRegionByRegionId request = new GetRegionByRegionId();
        request.setId(regionId);

        GetRegionByRegionIdPortType port =
                new GetRegionByRegionIdService().getPort(GetRegionByRegionIdPortType.class);
        portConfigurer.configure(port, regionEndpoint);

        return port.receive2(request);
    }

    @Cacheable(value = "mip-ref-district", key = "#districtId")
    public GetDistrictByDistrictIdResponse getDistrictById(long districtId) {
        log.info("SOAP GetDistrictByDistrictId: id={}", districtId);

        GetDistrictByDistrictId request = new GetDistrictByDistrictId();
        request.setId(districtId);

        GetDistrictByDistrictIdPortType port =
                new GetDistrictByDistrictIdService().getPort(GetDistrictByDistrictIdPortType.class);
        portConfigurer.configure(port, districtEndpoint);

        return port.receive2(request);
    }

    @Cacheable(value = "mip-ref-place", key = "#placeId")
    public GetPlaceByPlaceIdResponse getPlaceById(long placeId) {
        log.info("SOAP GetPlaceByPlaceId: id={}", placeId);

        GetPlaceByPlaceId request = new GetPlaceByPlaceId();
        request.setId(placeId);

        GetPlaceByPlaceIdPortType port =
                new GetPlaceByPlaceIdService().getPort(GetPlaceByPlaceIdPortType.class);
        portConfigurer.configure(port, placeEndpoint);

        return port.receive1(request);
    }

    @Cacheable(value = "mip-ref-street", key = "#streetId")
    public GetStreetByStreetIdResponse getStreetById(long streetId) {
        log.info("SOAP GetStreetByStreetId: id={}", streetId);

        GetStreetByStreetId request = new GetStreetByStreetId();
        request.setId(streetId);

        GetStreetByStreetIdPortType port =
                new GetStreetByStreetIdService().getPort(GetStreetByStreetIdPortType.class);
        portConfigurer.configure(port, streetEndpoint);

        return port.receive2(request);
    }

}
