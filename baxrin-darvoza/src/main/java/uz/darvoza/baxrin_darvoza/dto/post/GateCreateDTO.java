package uz.darvoza.baxrin_darvoza.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GateCreateDTO {
    @NotBlank(message = "name not entered")
    private String name;
    @NotBlank(message = "description not entered")
    private String description;

}
