package uz.darvoza.baxrin_darvoza.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.darvoza.baxrin_darvoza.enums.EmailType;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "email_history_entity")
public class EmailHistroyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "email")
    private String email;
    @Column(name = "code")
    private String code;
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "email_type")
    private EmailType emailType;
    @Column(name = "attempt_count")
    private Integer attemptCount;

}
