package uz.katm.claim.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.katm.claim.domain.retailer.MrzData;
import uz.katm.claim.exception.ClaimServiceException;
import uz.katm.claim.mrz.MrzParser;
import uz.katm.claim.mrz.MrzRecord;
import uz.katm.claim.mrz.etc1.MRP;
import uz.katm.claim.mrz.etc1.MrtdTd1;

import java.time.LocalDate;
import java.time.Year;

/**
 * Чтение MRZ паспорта (перенос gov.uz.katm.core.bki.passport.MrzReaderServiceImpl.getDataFromMrz).
 * docType 6 — паспорт TD3 (88 символов, 2×44), docType 0 — ID-карта TD1 (90 символов, 3×30).
 * Извлекает серию/номер документа, ФИО, пол, даты рождения/истечения и ПИНФЛ.
 */
@Slf4j
@Service
public class MrzReaderService {

    private static final String DOC_TYPE_PASSPORT = "6";
    private static final String DOC_TYPE_ID_CARD = "0";

    public MrzData parse(String mrz, String docType) {
        if (!StringUtils.hasText(mrz) || !StringUtils.hasText(docType)) {
            throw new ClaimServiceException("-1", "Не указан MRZ, либо тип документа");
        }
        String trimmed = mrz.trim();
        boolean passport = DOC_TYPE_PASSPORT.equalsIgnoreCase(docType) && trimmed.length() == 88;
        boolean idCard = DOC_TYPE_ID_CARD.equalsIgnoreCase(docType) && trimmed.length() == 90;
        if (!passport && !idCard) {
            int expected = DOC_TYPE_PASSPORT.equalsIgnoreCase(docType) ? 88 : 90;
            throw new ClaimServiceException("-1",
                    "Неверная длина MRZ. Должно быть " + expected + ", передали " + mrz.length());
        }

        String fullMrz = passport
                ? trimmed.substring(0, 44) + "\n" + trimmed.substring(44, 88)
                : trimmed.substring(0, 30) + "\n" + trimmed.substring(30, 60) + "\n" + trimmed.substring(60, 90);

        try {
            MrzRecord record = MrzParser.parse(fullMrz);
            if (record == null) {
                throw new ClaimServiceException("-1", "Не удалось разобрать MRZ");
            }
            String pinfl = passport ? ((MRP) record).personalNumber : ((MrtdTd1) record).optional;
            return new MrzData(
                    record.documentNumber.substring(0, 2),
                    record.documentNumber.substring(2),
                    record.surname,
                    record.givenNames,
                    "Male".equalsIgnoreCase(String.valueOf(record.sex)) ? 1 : 2,
                    LocalDate.of(birthYear(record.dateOfBirth.year), record.dateOfBirth.month, record.dateOfBirth.day),
                    // Срок действия паспорта — будущее, 2-значный год трактуем как 20xx.
                    LocalDate.of(2000 + record.expirationDate.year, record.expirationDate.month, record.expirationDate.day),
                    pinfl);
        } catch (ClaimServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Ошибка чтения MRZ: {}", e.getMessage());
            throw new ClaimServiceException("-1", "Ошибка чтения MRZ: " + e.getMessage());
        }
    }

    /** 2-значный год рождения → 4-значный: если больше текущего 2-значного, считаем 19xx, иначе 20xx. */
    private static int birthYear(int year2) {
        int currentYear2 = Year.now().getValue() % 100;
        return year2 > currentYear2 ? 1900 + year2 : 2000 + year2;
    }
}
