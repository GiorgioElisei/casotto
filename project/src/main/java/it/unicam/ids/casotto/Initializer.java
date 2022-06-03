package it.unicam.ids.casotto;

import it.unicam.ids.casotto.addetto.Addetto;
import it.unicam.ids.casotto.addetto.AddettoRepo;
import it.unicam.ids.casotto.admin.Admin;
import it.unicam.ids.casotto.admin.AdminRepo;
import it.unicam.ids.casotto.attivita.Attivita;
import it.unicam.ids.casotto.attivita.AttivitaRepo;
import it.unicam.ids.casotto.cliente.Cliente;
import it.unicam.ids.casotto.cliente.ClienteRepo;
import it.unicam.ids.casotto.fornitore.Fornitore;
import it.unicam.ids.casotto.fornitore.FornitoreRepo;
import it.unicam.ids.casotto.gruppo_ombrellone.GruppoOmbrelloni;
import it.unicam.ids.casotto.gruppo_ombrellone.GruppoOmbrelloniRepo;
import it.unicam.ids.casotto.posizione.Posizione;
import it.unicam.ids.casotto.posizione.PosizioneRepo;
import it.unicam.ids.casotto.prenotazione.Prenotazione;
import it.unicam.ids.casotto.prenotazione.PrenotazioneRepo;
import it.unicam.ids.casotto.prodotto.Prodotto;
import it.unicam.ids.casotto.prodotto.ProdottoRepo;
import it.unicam.ids.casotto.stagione.Stagione;
import it.unicam.ids.casotto.stagione.StagioneRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Initializer implements CommandLineRunner {
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private ClienteRepo clienteRepo;
    @Autowired
    private AddettoRepo addettoRepo;
    @Autowired
    private PosizioneRepo posizioneRepo;
    @Autowired
    private StagioneRepo stagioneRepo;
    @Autowired
    private GruppoOmbrelloniRepo gruppoOmbrelloniRepo;
    @Autowired
    private PrenotazioneRepo prenotazioneRepo;
    @Autowired
    private FornitoreRepo fornitoreRepo;
    @Autowired
    private AttivitaRepo attivitaRepo;
    @Autowired
    private ProdottoRepo prodottoRepo;

    @Override
    public void run(String... args) throws Exception {
        this.adminRepo.save(new Admin("admin", "admin"));

        this.fornitoreRepo.save(new Fornitore("fornitore", "fornitore"));

        Cliente cliente = new Cliente("cliente", "cliente");
        clienteRepo.save(cliente);

        List<Stagione> stagioni = new ArrayList<>();
        stagioni.add(new Stagione("MAGGIO", 2.0, 1));
        stagioni.add(new Stagione("GIUGNO", 3.5, 2));
        stagioni.add(new Stagione("LUGLIO", 5.0, 3));
        stagioni.add(new Stagione("AGOSTO", 10.0, 4));
        stagioni.add(new Stagione("SETTEMBRE", 4.0, 5));
        List<Stagione> stagioniSaved = new ArrayList<>();
        for (Stagione s : stagioni)
            stagioniSaved.add(this.stagioneRepo.save(s));

        Posizione posizione = this.posizioneRepo.save(new Posizione("AVANTI", "AVANTI", 5.0));
        this.posizioneRepo.save(new Posizione("CENTRO", "CENTRO", 3.5));
        this.posizioneRepo.save(new Posizione("DIETRO", "DIETRO", 2.0));

        GruppoOmbrelloni go = new GruppoOmbrelloni(posizione, 30);
        this.gruppoOmbrelloniRepo.save(go);
        for (Stagione s : stagioniSaved)
            go.addStagione(s);
        //this.gruppoOmbrelloniRepo.save(go);

        Prenotazione prenotazione = new Prenotazione(stagioniSaved.get(0), cliente, go);
        prenotazioneRepo.save(prenotazione);
        gruppoOmbrelloniRepo.save(go);

        Addetto addetto = new Addetto("addetto", "addetto");
        addetto.addPosizione(posizione);
        this.addettoRepo.save(addetto);

        attivitaRepo.save(new Attivita("Moto d'aqua", "Moto d'acqua dalle 9 alle 14",
                10, stagioni.get(0)));

        prodottoRepo.save(new Prodotto("Patatine", 4.15, 50));
        prodottoRepo.save(new Prodotto("Tramezzino", 3.50, 30));

    }
}
