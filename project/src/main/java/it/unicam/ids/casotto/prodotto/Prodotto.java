package it.unicam.ids.casotto.prodotto;

import it.unicam.ids.casotto.cliente.Cliente;
import it.unicam.ids.casotto.ordine.Ordine;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Prodotto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private Double prezzo;
    private Integer numeroDisponibilita;
    @ManyToMany(cascade = CascadeType.MERGE)
    private Set<Ordine> prenotazioni;

    public Prodotto(String nome, Double prezzo, Integer numeroDisponibilita) {
        this.nome = nome;
        this.prezzo = prezzo;
        this.numeroDisponibilita = numeroDisponibilita;
        this.prenotazioni = new HashSet<>();
    }

    public Prodotto() {
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

    public Integer getNumeroDisponibilita() {
        return numeroDisponibilita;
    }

    public Set<Ordine> getPrenotazioni() {
        return prenotazioni;
    }

    public void setNumeroDisponibilita(Integer numeroDisponibilita) throws Exception {
        if (this.prenotazioni.size() == 0 || this.prenotazioni.stream().map(Ordine::getQuantita).reduce(Integer::sum).orElse(0) <= numeroDisponibilita)
            this.numeroDisponibilita = numeroDisponibilita;
        else
            throw new Exception("I prodotti prenotati sono maggiori rispetto ai disponibili");
    }

    public void prenotaProdotto(Cliente cliente, Integer quantita) throws Exception {
        if (quantita > 0 && (this.prenotazioni.size() == 0 || this.prenotazioni.stream()
                .filter(p -> p.getCliente().getId() != cliente.getId())
                .map(Ordine::getQuantita)
                .reduce(Integer::sum).orElse(0) + quantita < this.numeroDisponibilita)) {
            Ordine o = this.prenotazioni.stream().filter(p -> p.getCliente().getId() == cliente.getId()).findAny().orElse(null);
            if (o == null)
                this.prenotazioni.add(new Ordine(cliente, quantita));
            else
                o.setQuantita(quantita);
        } else
            throw new Exception("Quantità non valida");
    }

    public void compraProdotto(Cliente cliente) throws Exception {
        Ordine ordine = this.prenotazioni.stream().filter(p -> p.getCliente().getId() == cliente.getId()).findAny().orElse(null);
        if (ordine == null)
            throw new Exception("Il cliente non ha effettuato una prenotazione");
        this.numeroDisponibilita -= ordine.getQuantita();
        this.prenotazioni.removeIf(p -> p.getCliente().getId() == cliente.getId());
    }
}
