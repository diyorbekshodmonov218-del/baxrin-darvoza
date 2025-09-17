package uz.darvoza.baxrin_darvoza.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import uz.darvoza.baxrin_darvoza.enums.Roles;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class JwtDTO {
    private String username;
    private Integer id;
    private List<Roles> roles;

}
