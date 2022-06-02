package it.unicam.ids.casotto.alimento;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlimentoRepo extends CrudRepository<Alimento, Long> {
}
