package uz.darvoza.baxrin_darvoza.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import uz.darvoza.baxrin_darvoza.entity.GateEntity;

import java.util.List;

public interface GateRepository extends CrudRepository<GateEntity, String> {

    @Query("select g.id from GateEntity g where g.adminId=:id" +
            " and g.visible=true " +
            "order by g.createdDate desc ")
    Page<String> findPageIds(@Param("id") Integer adminId, Pageable pageable);

    @Query("select distinct g from GateEntity g" +
            " left join fetch g.attachs where g.id in :id")
    List<GateEntity> findAllWithAttachByIds(@Param("id") List<String> adminId);

    @Modifying
    @Transactional
    @Query("update GateEntity set visible=false where id=?1")
    void delete(String id);

    Page<GateEntity> findAll(Pageable pageable);
}
