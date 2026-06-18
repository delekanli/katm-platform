package uz.katm.client.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Тело запроса проверки счёта ИНПС в Халк банке (перенос CheckInpsDataParams). */
public record InpsCheckRequest(@JsonProperty("inps") String inps) {
}
