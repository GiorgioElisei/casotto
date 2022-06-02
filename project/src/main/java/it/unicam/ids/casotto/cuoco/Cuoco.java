package it.unicam.ids.casotto.cuoco;

import it.unicam.ids.casotto.user.User;

import javax.persistence.Entity;

@Entity
public class Cuoco  extends User {

    public Cuoco(String username, String password) {
        super(username, password);
    }

    public Cuoco() {
        super();
    }
}
