package it.unicam.ids.casotto.addetto;

import it.unicam.ids.casotto.posizione.Posizione;
import it.unicam.ids.casotto.user.User;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Addetto extends User {

    @ManyToMany(cascade = CascadeType.MERGE)
    private Set<Posizione> posizioni = new HashSet<>();

    public Addetto(String username, String password) {
        super(username, password);
    }

    public Addetto() {
        super();
    }

    public Set<Posizione> getPosizioni() {
        return posizioni;
    }

    public void addPosizione(Posizione posizione) {
        this.posizioni.add(posizione);
    }
}
