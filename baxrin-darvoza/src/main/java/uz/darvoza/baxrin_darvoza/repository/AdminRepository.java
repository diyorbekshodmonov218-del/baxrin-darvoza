package uz.darvoza.baxrin_darvoza.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uz.darvoza.baxrin_darvoza.entity.AdminEntity;
import uz.darvoza.baxrin_darvoza.enums.GeneralStatus;
import uz.darvoza.baxrin_darvoza.enums.Roles;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends CrudRepository<AdminEntity, Integer> {
    Optional<AdminEntity> findByUsernameAndVisibleTrue(String username);
    Optional<AdminEntity> findByUsername(String username);
    Optional<AdminEntity> findByIdAndVisibleTrue(Integer adminId);
    List<AdminEntity> findAllByRoleAndVisibleTrue(Roles roles);

    @Transactional
    @Modifying
    @Query("update AdminEntity set status=?2 where id=?1")
    void changeStatus(Integer adminId, GeneralStatus status);

    @Transactional
    @Modifying
    @Query("update AdminEntity set visible=?2 where id=?1")
    void deleteById(Integer adminId,boolean visible);
}
