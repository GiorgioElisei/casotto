package it.unicam.ids.casotto.cuoco;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuocoRepo extends CrudRepository<Cuoco, Long> {
}
