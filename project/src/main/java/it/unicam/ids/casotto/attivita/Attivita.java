package it.unicam.ids.casotto.attivita;

import it.unicam.ids.casotto.cliente.Cliente;
import it.unicam.ids.casotto.stagione.Stagione;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Attivita {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private String descrizione;
    private Boolean stato = true;
    private Integer numeroMassimoPartecipanti;
    @OneToOne(cascade = CascadeType.MERGE)
    private Stagione stagione;
    @OneToMany(cascade = CascadeType.MERGE)
    private Set<Cliente> partecipanti = new HashSet<>();

    public Attivita() {
    }

    public Attivita(String nome, String descrizione, Integer numeroMassimoPartecipanti, Stagione stagione) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.numeroMassimoPartecipanti = numeroMassimoPartecipanti;
        this.stagione = stagione;
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

    public Integer getNumeroMassimoPartecipanti() {
        return numeroMassimoPartecipanti;
    }

    public Stagione getStagione() {
        return stagione;
    }

    public Set<Cliente> getPartecipanti() {
        return partecipanti;
    }

    public Boolean getStato() {
        return stato;
    }

    public void addPartecipante(Cliente cliente) throws Exception {
        if(this.partecipanti.size() < this.numeroMassimoPartecipanti)
            this.partecipanti.add(cliente);
        else
            throw new Exception("Numero massimo partecipanti superato");
    }

    public void eliminaPartecipante(Cliente cliente) throws Exception {
        this.partecipanti.remove(cliente);
    }

    public void eliminaAttivita() {
        stato = false;
    }
}
