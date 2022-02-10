package it.unicam.ids.casotto.cliente;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.unicam.ids.casotto.gruppo_ombrellone.GruppoOmbrelloni;
import it.unicam.ids.casotto.prenotazione.Prenotazione;
import it.unicam.ids.casotto.user.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Cliente extends User {

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnoreProperties("cliente")
    private Set<Prenotazione> prenotazioni = new HashSet<>();

    public Cliente(String username, String password) {
        super(username, password);
    }

    public Cliente() {
        super();
    }

    public Set<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void addPrenotazione(Prenotazione prenotazione) {
        this.prenotazioni.add(prenotazione);
    }

    public void removePrenotazione(Prenotazione prenotazione) {
        this.prenotazioni.remove(prenotazione);
    }
}
