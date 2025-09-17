package uz.darvoza.baxrin_darvoza.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.darvoza.baxrin_darvoza.enums.GeneralStatus;
import uz.darvoza.baxrin_darvoza.enums.Roles;

@Entity
@Table(name = "admin")
@Getter
@Setter
public class AdminEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "username")
    private String username;
    @Column(name = "Password")
    private String password;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Roles role; // ADMIN yoki SUPER_ADMIN
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private GeneralStatus status;
    @Column(name = "visible")
    private Boolean visible;


}
