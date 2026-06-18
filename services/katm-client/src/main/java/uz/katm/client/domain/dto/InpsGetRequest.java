package uz.katm.client.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Тело запроса данных ИНПС в Халк банке (перенос GetInpsDataParams).
 * Если счёт ИНПС определён проверкой — передаётся только inps; иначе — паспортные данные субъекта.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record InpsGetRequest(
        @JsonProperty("pass_sn") String passSn,
        @JsonProperty("pass_num") String passNum,
        @JsonProperty("inps") String inps
) {
    public static InpsGetRequest byInps(String inps) {
        return new InpsGetRequest(null, null, inps);
    }

    public static InpsGetRequest byPassport(String passSn, String passNum, String inps) {
        return new InpsGetRequest(passSn, passNum, inps);
    }
}
