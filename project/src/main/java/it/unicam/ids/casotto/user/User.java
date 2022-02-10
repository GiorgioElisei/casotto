package it.unicam.ids.casotto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Locale;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    @JsonProperty
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getUsername() {
        return username;
    }

    public String getTipo() {
        return this.getClass().getSimpleName().toLowerCase(Locale.ROOT);
    }
}
