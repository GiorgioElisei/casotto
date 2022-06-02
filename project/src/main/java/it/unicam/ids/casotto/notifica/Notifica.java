package it.unicam.ids.casotto.notifica;

import it.unicam.ids.casotto.user.User;

import javax.persistence.*;

@Entity
public class Notifica {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(cascade = CascadeType.MERGE)
    private User utente;
    private String messaggio;

    public Notifica() {
    }

    public Notifica(User utente, String messaggio) {
        this.utente = utente;
        this.messaggio = messaggio;
    }

    public Long getId() {
        return id;
    }

    public User getUtente() {
        return utente;
    }

    public String getMessaggio() {
        return messaggio;
    }
}
