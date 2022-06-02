package it.unicam.ids.casotto.addetto;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddettoRepo extends CrudRepository<Addetto, Long> {
}
