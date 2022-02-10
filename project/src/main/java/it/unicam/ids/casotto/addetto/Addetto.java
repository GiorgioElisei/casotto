package it.unicam.ids.casotto.addetto;

import it.unicam.ids.casotto.user.User;

import javax.persistence.Entity;

@Entity
public class Addetto extends User {
    public Addetto(String username, String password) {
        super(username, password);
    }

    public Addetto() {
        super();
    }

}
