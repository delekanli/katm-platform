package uz.katm.notification.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendEmailRequest {

    @NotBlank(message = "Адрес получателя обязателен")
    @Email(message = "Некорректный email адрес")
    private String to;

    @NotBlank(message = "Тема письма обязательна")
    private String subject;

    @NotBlank(message = "Тело письма обязательно")
    private String body;

    private boolean html = false;
}
