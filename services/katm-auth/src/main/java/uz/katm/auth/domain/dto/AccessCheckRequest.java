package uz.katm.auth.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccessCheckRequest {

    @NotBlank(message = "head не может быть пустым")
    private String head;

    @NotBlank(message = "code не может быть пустым")
    private String code;

    @NotBlank(message = "ip не может быть пустым")
    private String ip;
}
