package it.unicam.ids.casotto.stagione;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Stagione {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private Double prezzo;
    private int sorting;

    public Stagione() {
    }

    public Stagione(String nome, Double prezzo, int sorting) {
        this.nome = nome;
        this.prezzo = prezzo;
        this.sorting = sorting;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public int getSorting() {
        return sorting;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public void setSorting(int sorting) {
        this.sorting = sorting;
    }
}
