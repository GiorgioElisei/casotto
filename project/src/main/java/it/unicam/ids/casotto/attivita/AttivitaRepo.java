package it.unicam.ids.casotto.attivita;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttivitaRepo extends CrudRepository<Attivita, Long> {
}
