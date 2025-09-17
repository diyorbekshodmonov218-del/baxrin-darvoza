package uz.darvoza.baxrin_darvoza.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AppResponse <T>{
    private T  data;
}
