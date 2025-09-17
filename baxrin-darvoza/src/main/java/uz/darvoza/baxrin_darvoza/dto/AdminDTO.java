package uz.darvoza.baxrin_darvoza.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import uz.darvoza.baxrin_darvoza.enums.GeneralStatus;
import uz.darvoza.baxrin_darvoza.enums.Roles;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminDTO {
    private Integer id;
    private String name;
    private String username;
    private Roles role;
    private String jwt;
    private GeneralStatus status;
}
