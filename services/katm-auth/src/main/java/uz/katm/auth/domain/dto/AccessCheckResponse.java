package uz.katm.auth.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessCheckResponse {
    private boolean allowed;
    private String message;
}
