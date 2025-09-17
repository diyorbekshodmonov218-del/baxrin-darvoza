package uz.darvoza.baxrin_darvoza.repository;

import org.springframework.data.repository.CrudRepository;
import uz.darvoza.baxrin_darvoza.entity.GateEntity;

public interface OrderRepository extends CrudRepository<GateEntity, Integer> {

}
