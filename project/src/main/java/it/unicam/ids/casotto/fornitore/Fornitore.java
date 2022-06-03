package it.unicam.ids.casotto.fornitore;

import it.unicam.ids.casotto.user.User;

import javax.persistence.Entity;

@Entity
public class Fornitore extends User {

    public Fornitore(String username, String password) {
        super(username, password);
    }

    public Fornitore() {
        super();
    }
}
