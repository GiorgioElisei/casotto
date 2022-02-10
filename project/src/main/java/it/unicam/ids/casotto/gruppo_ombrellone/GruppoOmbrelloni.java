package it.unicam.ids.casotto.gruppo_ombrellone;

import it.unicam.ids.casotto.cliente.Cliente;
import it.unicam.ids.casotto.posizione.Posizione;
import it.unicam.ids.casotto.prenotazione.Prenotazione;
import it.unicam.ids.casotto.stagione.Stagione;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class GruppoOmbrelloni {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer ombrelloniTotali;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Prenotazione> prenotazioni = new HashSet<>();

    @ManyToMany(cascade = CascadeType.MERGE)
    private Set<Stagione> stagioni = new HashSet<>();

    @OneToOne(cascade = CascadeType.MERGE)
    private Posizione posizione;

    public GruppoOmbrelloni(Posizione posizione, Integer ombrelloniTotali) {
        this.ombrelloniTotali = ombrelloniTotali;
        this.posizione = posizione;
    }

    public GruppoOmbrelloni() {
    }

    public Long getId() {
        return id;
    }

    public Set<Stagione> getStagioni() {
        return stagioni;
    }

    public Set<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public Posizione getPosizione() {
        return posizione;
    }

    public Integer getOmbrelloniTotali() {
        return ombrelloniTotali;
    }

    public Integer getOmbrelloniPrenotatiTotali() {
        return prenotazioni.size();
    }

    public Integer getOmbrelloniPrenotati(Stagione stagione) {
        return (int) prenotazioni.stream().filter(p -> p.getStagione().getId() == stagione.getId()).count();
    }

    public void setPosizione(Posizione posizione) {
        this.posizione = posizione;
    }

    public void setOmbrelloniTotali(Integer ombrelloniTotali) {
        this.ombrelloniTotali = ombrelloniTotali;
    }

    public Prenotazione prenotaOmbrellone(Stagione stagione, Cliente cliente) throws Exception {
        if(ombrelloniTotali > getOmbrelloniPrenotati(stagione)) {
            return new Prenotazione(stagione, cliente, this);
        }
        throw new Exception("Non ci sono più ombrelloni disponibili in questo gruppo");
    }

    public void addStagione(Stagione stagione) {
        this.stagioni.add(stagione);
    }

    public void addPrenotazione(Prenotazione prenotazione) {
        this.prenotazioni.add(prenotazione);
    }

    public void removePrenotazione(Prenotazione prenotazione) {
        this.prenotazioni.remove(prenotazione);
    }
}
