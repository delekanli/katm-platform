package uz.katm.notification.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SendSmsRequest {

    @NotBlank(message = "Номер получателя обязателен")
    @Pattern(regexp = "\\d{12}", message = "recipient должен содержать 12 цифр (например 998901234567)")
    private String recipient;

    @NotBlank(message = "Текст сообщения обязателен")
    private String text;
}
