package it.unicam.ids.casotto.admin;

import it.unicam.ids.casotto.user.User;

import javax.persistence.Entity;

@Entity
public class Admin extends User {

    public Admin(String username, String password) {
        super(username, password);
    }

    public Admin() {
        super();
    }
}
