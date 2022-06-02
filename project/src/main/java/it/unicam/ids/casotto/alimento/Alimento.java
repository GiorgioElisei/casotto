package it.unicam.ids.casotto.alimento;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Alimento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private String descrizione;
    private Double prezzo;
    private Integer quantita;

    public Alimento() {
    }

    public Alimento(String nome, String descrizione, Double prezzo, Integer quantita) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.quantita = quantita;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public Integer getQuantita() {
        return quantita;
    }
}
