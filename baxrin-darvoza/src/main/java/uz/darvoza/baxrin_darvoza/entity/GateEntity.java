package uz.darvoza.baxrin_darvoza.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.darvoza.baxrin_darvoza.enums.GeneralStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "gate")
@Getter
@Setter
public class GateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "description", length = 255)
    private String description;
    @Column(name= "created_date")
    private LocalDateTime createdDate;
    @Column(name = "status")
    private GeneralStatus status;
    @Column(name = "visible")
    private Boolean visible;

    @OneToMany(mappedBy = "gate", fetch = FetchType.LAZY)
    private List<AttachEntity> attachs;

    @Column(name = "admin_id")
    private Integer adminId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id",insertable = false, updatable = false)
    private AdminEntity adminEntity;

}
