package it.unicam.ids.casotto.notifica;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificaRepo extends CrudRepository<Notifica, Long> {
}