package it.unicam.ids.casotto.prenotazione;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.unicam.ids.casotto.cliente.Cliente;
import it.unicam.ids.casotto.gruppo_ombrellone.GruppoOmbrelloni;
import it.unicam.ids.casotto.stagione.Stagione;

import javax.persistence.*;

@Entity
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(cascade = CascadeType.MERGE)
    private Stagione stagione;
    @OneToOne(cascade = CascadeType.MERGE)
    @JsonIgnoreProperties("prenotazioni")
    private Cliente cliente;
    @OneToOne(cascade = CascadeType.MERGE)
    @JsonIgnoreProperties("prenotazioni")
    private GruppoOmbrelloni gruppoOmbrelloni;

    public Prenotazione() {
    }

    public Prenotazione(Stagione stagione, Cliente cliente, GruppoOmbrelloni gruppoOmbrelloni) throws Exception {
        this.stagione = stagione;
        this.cliente = cliente;
        this.gruppoOmbrelloni = gruppoOmbrelloni;
        Prenotazione prenotazione = cliente.getPrenotazioni().stream()
                .filter(p ->
                        p.getCliente().getId() == cliente.getId() &&
                        p.getStagione().getId() == stagione.getId() &&
                        p.getGruppoOmbrelloni().getId() == gruppoOmbrelloni.getId())
                .findAny()
                .orElse(null);
        if (prenotazione != null) throw new Exception("Prenotazione gia eseguita per la stessa stagione");
        cliente.addPrenotazione(this);
        gruppoOmbrelloni.addPrenotazione(this);
    }

    public Long getId() {
        return id;
    }

    public Stagione getStagione() {
        return stagione;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public GruppoOmbrelloni getGruppoOmbrelloni() {
        return gruppoOmbrelloni;
    }
}
