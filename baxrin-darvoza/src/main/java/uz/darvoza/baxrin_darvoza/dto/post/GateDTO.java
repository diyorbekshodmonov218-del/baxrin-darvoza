package uz.darvoza.baxrin_darvoza.dto.post;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import uz.darvoza.baxrin_darvoza.entity.AttachEntity;
import uz.darvoza.baxrin_darvoza.enums.GeneralStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GateDTO {
    private String id;
    private String name;
    private String description;
    private LocalDateTime createdDate;
    private GeneralStatus status;

    private List<AttachDTO> attachs;
}
