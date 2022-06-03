package it.unicam.ids.casotto.ordine;

import it.unicam.ids.casotto.cliente.Cliente;

import javax.persistence.*;

@Entity
public class Ordine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(cascade = CascadeType.MERGE)
    private Cliente cliente;
    private Integer quantita;

    public Ordine(Cliente cliente, Integer quantita) {
        this.cliente = cliente;
        this.quantita = quantita;
    }

    public Ordine() {
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }
}
