package uz.darvoza.baxrin_darvoza.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter

public class RegistrationDTO {
    private String name;
    private String username;
    private String password;

}
