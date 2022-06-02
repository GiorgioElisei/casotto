package it.unicam.ids.casotto.cameriere;

import it.unicam.ids.casotto.user.User;

import javax.persistence.Entity;

@Entity
public class Cameriere extends User {
    public Cameriere() {
        super();
    }

    public Cameriere(String username, String password) {
        super(username, password);
    }
}
