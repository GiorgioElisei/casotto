package it.unicam.ids.casotto;

import it.unicam.ids.casotto.attivita.Attivita;
import it.unicam.ids.casotto.attivita.AttivitaRepo;
import it.unicam.ids.casotto.cliente.Cliente;
import it.unicam.ids.casotto.cliente.ClienteRepo;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {
    @Autowired
    private ClienteRepo clienteRepo;
    @Autowired
    private GruppoOmbrelloniRepo ombrelloneRepo;
    @Autowired
    private PosizioneRepo posizioneRepo;
    @Autowired
    private StagioneRepo stagioneRepo;
    @Autowired
    private PrenotazioneRepo prenotazioneRepo;
    @Autowired
    private AttivitaRepo attivitaRepo;
    @Autowired
    private ProdottoRepo prodottoRepo;

    @GetMapping(path = "/ombrelloni")
    public List<GruppoOmbrelloni> gruppiOmbrelloni(@CookieValue Long id) {
        try {
            clienteRepo.findById(id).get();
            List<GruppoOmbrelloni> ombrelloni = new ArrayList<>();
            ombrelloneRepo.findAll().forEach(ombrelloni::add);
            return ombrelloni;
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping(path = "/mieiOmbrelloni")
    public List<GruppoOmbrelloni> mieiOmbrelloni(@CookieValue Long id) {
        try {
            Cliente cliente = clienteRepo.findById(id).get();
            return cliente.getPrenotazioni().stream().map(p -> p.getGruppoOmbrelloni()).collect(Collectors.toList());
        } catch (Exception e) {
            return null;
        }
    }

    @PutMapping(path = "/prenotaOmbrellone")
    public String prenotaOmbrellone(@CookieValue Long id, @RequestParam Long idOmbrellone, @RequestParam Long idStagione) {
        try {
            Cliente cliente = clienteRepo.findById(id).get();
            GruppoOmbrelloni ombrellone = ombrelloneRepo.findById(idOmbrellone).orElse(null);
            Stagione stagione = stagioneRepo.findById(idStagione).orElse(null);
            Prenotazione prenotazione = ombrellone.prenotaOmbrellone(stagione, cliente);
            prenotazioneRepo.save(prenotazione);
            clienteRepo.save(cliente);
            ombrelloneRepo.save(ombrellone);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @DeleteMapping(path = "/eliminaPrenotazione")
    public String eliminaPrenotazione(@CookieValue Long id, @RequestParam Long idPrenotazione) {
        try {
            Cliente cliente = clienteRepo.findById(id).get();
            Prenotazione prenotazione = prenotazioneRepo.findById(idPrenotazione).orElse(null);
            GruppoOmbrelloni ombrellone = ombrelloneRepo.findById(prenotazione.getGruppoOmbrelloni().getId()).orElse(null);
            ombrellone.removePrenotazione(prenotazione);
            cliente.removePrenotazione(prenotazione);
            prenotazioneRepo.deleteById(idPrenotazione);
            clienteRepo.save(cliente);
            ombrelloneRepo.save(ombrellone);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping(path = "/posizioni")
    public List<Posizione> posizioni(@CookieValue Long id) {
        try {
            clienteRepo.findById(id).get();
            List<Posizione> posizioni = new ArrayList<>();
            posizioneRepo.findAll().forEach(posizioni::add);
            return posizioni;
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping(path = "/stagioni")
    public List<Stagione> stagioni(@CookieValue Long id) {
        try {
            clienteRepo.findById(id).get();
            List<Stagione> stagioni = new ArrayList<>();
            stagioneRepo.findAll().forEach(stagioni::add);
            return stagioni;
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping(path = "/attivita")
    public List<Attivita> attivita(@CookieValue Long id) {
        try {
            clienteRepo.findById(id).get();
            List<Attivita> attivita = new ArrayList<>();
            attivitaRepo.findAll().forEach(attivita::add);
            return attivita;
        } catch (Exception e) {
            return null;
        }
    }

    @PutMapping(path = "/partecipaAttivita")
    public String partecipaAttivita(@CookieValue Long id, @RequestParam Long idAttivita) {
        try {
            Cliente cliente = clienteRepo.findById(id).get();
            Attivita attivita = attivitaRepo.findById(idAttivita).get();
            attivita.addPartecipante(cliente);
            attivitaRepo.save(attivita);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @DeleteMapping(path = "/eliminaPartecipaAttivita")
    public String eliminaPartecipaAttivita(@CookieValue Long id, @RequestParam Long idAttivita) {
        try {
            Cliente cliente = clienteRepo.findById(id).get();
            Attivita attivita = attivitaRepo.findById(idAttivita).get();
            attivita.eliminaPartecipante(cliente);
            attivitaRepo.save(attivita);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping(path = "/prodotti")
    public List<Prodotto> prodotti(@CookieValue Long id) {
        try {
            clienteRepo.findById(id).get();
            List<Prodotto> prodotti = new ArrayList<>();
            prodottoRepo.findAll().forEach(prodotti::add);
            return prodotti;
        } catch (Exception e) {
            return null;
        }
    }

    @PutMapping(path = "/prenotaProdotto")
    public String prenotaProdotto(@CookieValue Long id, @RequestParam Long idProdotto, @RequestParam Integer quantita) {
        try {
            Cliente cliente = clienteRepo.findById(id).get();
            Prodotto prodotto = prodottoRepo.findById(idProdotto).get();
            prodotto.prenotaProdotto(cliente, quantita);
            prodottoRepo.save(prodotto);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
