package uz.katm.claim.domain.retailer;

import java.time.LocalDate;

/** Данные субъекта, извлечённые из MRZ паспорта (перенос ClaimDto-полей из MrzReaderServiceImpl). */
public record MrzData(
        String docSeries,
        String docNumber,
        String lastName,
        String firstName,
        Integer male,
        LocalDate birthDate,
        LocalDate expireDate,
        String pinfl
) {
}
