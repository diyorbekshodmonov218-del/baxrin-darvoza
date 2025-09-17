package uz.darvoza.baxrin_darvoza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.darvoza.baxrin_darvoza.entity.AttachEntity;

import java.util.List;

public interface AttachRepository extends JpaRepository<AttachEntity,String> {
    List<AttachEntity> findByGateId(String gateId);
}
