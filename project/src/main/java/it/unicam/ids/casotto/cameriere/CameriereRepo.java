package it.unicam.ids.casotto.cameriere;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CameriereRepo extends CrudRepository<Cameriere, Long> {
}
