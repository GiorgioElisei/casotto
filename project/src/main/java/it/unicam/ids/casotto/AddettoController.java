package it.unicam.ids.casotto;

import it.unicam.ids.casotto.addetto.Addetto;
import it.unicam.ids.casotto.addetto.AddettoRepo;
import it.unicam.ids.casotto.admin.AdminRepo;
import it.unicam.ids.casotto.attivita.Attivita;
import it.unicam.ids.casotto.attivita.AttivitaRepo;
import it.unicam.ids.casotto.cliente.Cliente;
import it.unicam.ids.casotto.cliente.ClienteRepo;
import it.unicam.ids.casotto.gruppo_ombrellone.GruppoOmbrelloni;
import it.unicam.ids.casotto.gruppo_ombrellone.GruppoOmbrelloniRepo;
import it.unicam.ids.casotto.notifica.Notifica;
import it.unicam.ids.casotto.notifica.NotificaRepo;
import it.unicam.ids.casotto.posizione.PosizioneRepo;
import it.unicam.ids.casotto.stagione.Stagione;
import it.unicam.ids.casotto.stagione.StagioneRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/addetto")
public class AddettoController {
    @Autowired
    private GruppoOmbrelloniRepo ombrelloneRepo;
    @Autowired
    private PosizioneRepo posizioneRepo;
    @Autowired
    private AddettoRepo addettoRepo;
    @Autowired
    private AttivitaRepo attivitaRepo;
    @Autowired
    private StagioneRepo stagioneRepo;
    @Autowired
    private NotificaRepo notificaRepo;
    @Autowired
    private ClienteRepo clienteRepo;

    @GetMapping(path = "/ombrelloni")
    public List<GruppoOmbrelloni> ombrelloni(@CookieValue Long id) {
        try {
            Addetto a = addettoRepo.findById(id).get();
            List<GruppoOmbrelloni> ombrelloni = new ArrayList<>();
            ombrelloneRepo.findAll().forEach(o -> {
                if (a.getPosizioni().contains(o.getPosizione()))
                    ombrelloni.add(o);
            });
            return ombrelloni;
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping(path = "/creaAttivita")
    public String creaAttivita(@CookieValue Long id, @RequestParam String nome, @RequestParam String descrizione,
                               @RequestParam Integer numeroMassimoPartecipanti, @RequestParam Long idStagione) {
        try {
            addettoRepo.findById(id).get();
            Stagione stagione = stagioneRepo.findById(idStagione).get();
            Iterator<Attivita> itr = attivitaRepo.findAll().iterator();
            while (itr.hasNext()) {
                Attivita att = itr.next();
                if (att.getNome().equalsIgnoreCase(nome) && att.getStagione().getId() == idStagione && att.getStato())
                    throw new Exception("attivita con stesso nome e stagione esistente");
            }
            if (numeroMassimoPartecipanti <= 0)
                throw new Exception("numero massimo partecipanti non valido");
            Attivita attivita = new Attivita(nome, descrizione, numeroMassimoPartecipanti, stagione);
            attivitaRepo.save(attivita);
            Iterator<Cliente> itrCliente = clienteRepo.findAll().iterator();
            while (itrCliente.hasNext())
                notificaRepo.save(new Notifica(itrCliente.next(), "nuova attivita: [" + attivita.getNome() +
                        " - " + attivita.getStagione().getNome() + "]"));
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping(path = "/attivita")
    public List<Attivita> attivita(@CookieValue Long id) {
        try {
            addettoRepo.findById(id).get();
            List<Attivita> attivita = new ArrayList<>();
            attivitaRepo.findAll().forEach(attivita::add);
            return attivita;
        } catch (Exception e) {
            return null;
        }
    }

    @DeleteMapping(path = "/eliminaAttivita")
    public String eliminaAttivita(@CookieValue Long id, @RequestParam Long idAttivita) {
        try {
            addettoRepo.findById(id).get();
            Attivita attivita = attivitaRepo.findById(idAttivita).get();
            attivita.eliminaAttivita();
            attivita.getPartecipanti().forEach(p -> notificaRepo.save(new Notifica(p, "attività [" +
                    attivita.getNome() + " - " + attivita.getStagione().getNome() + "] ELIMINATA")));
            attivitaRepo.save(attivita);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping(path = "/stagioni")
    public List<Stagione> stagioni(@CookieValue Long id) {
        try {
            addettoRepo.findById(id).get();
            List<Stagione> stagioni = new ArrayList<>();
            stagioneRepo.findAll().forEach(stagioni::add);
            return stagioni;
        } catch (Exception e) {
            return null;
        }
    }

}
