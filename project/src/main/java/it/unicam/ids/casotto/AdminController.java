package it.unicam.ids.casotto;

import it.unicam.ids.casotto.admin.AdminRepo;
import it.unicam.ids.casotto.gruppo_ombrellone.GruppoOmbrelloni;
import it.unicam.ids.casotto.gruppo_ombrellone.GruppoOmbrelloniRepo;
import it.unicam.ids.casotto.posizione.Posizione;
import it.unicam.ids.casotto.posizione.PosizioneRepo;
import it.unicam.ids.casotto.stagione.Stagione;
import it.unicam.ids.casotto.stagione.StagioneRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private GruppoOmbrelloniRepo ombrelloneRepo;
    @Autowired
    private PosizioneRepo posizioneRepo;
    @Autowired
    private StagioneRepo stagioneRepo;

    @GetMapping(path = "/ombrelloni")
    public List<GruppoOmbrelloni> ombrelloni(@CookieValue Long id) {
        try {
            adminRepo.findById(id).get();
            List<GruppoOmbrelloni> ombrelloni = new ArrayList<>();
            ombrelloneRepo.findAll().forEach(ombrelloni::add);
            return ombrelloni;
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping(path = "/creaOmbrellone")
    public Long creaOmbrellone(@CookieValue Long id, @RequestParam Long posizioneId, @RequestParam Integer ombrelloniTotali) {
        try {
            adminRepo.findById(id).get();
            Posizione posizione = posizioneRepo.findById(posizioneId).orElse(null);
            GruppoOmbrelloni ombrellone = new GruppoOmbrelloni(posizione, ombrelloniTotali);
            GruppoOmbrelloni created = ombrelloneRepo.save(ombrellone);
            return created.getId();
        } catch (Exception e) {
            return -1L;
        }
    }

    @DeleteMapping(path = "/eliminaOmbrellone")
    public String eliminaOmbrellone(@CookieValue Long id, @RequestParam Long idOmbrellone) {
        try {
            adminRepo.findById(id).get();
            GruppoOmbrelloni ombrellone = ombrelloneRepo.findById(idOmbrellone).orElse(null);
            if(ombrellone.getOmbrelloniPrenotatiTotali() > 0)
                throw new Exception("Non può essere eliminato un gruppo di ombrelloni con delle prenotazioni");
            ombrelloneRepo.deleteById(idOmbrellone);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping(path = "/posizioni")
    public List<Posizione> posizioni(@CookieValue Long id) {
        try {
            adminRepo.findById(id).get();
            List<Posizione> posizioni = new ArrayList<>();
            posizioneRepo.findAll().forEach(posizioni::add);
            return posizioni;
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping(path = "/creaPosizione")
    public String creaPosizione(@CookieValue Long id, @RequestParam String nome, @RequestParam String descrizione, @RequestParam Double prezzo) {
        try {
            adminRepo.findById(id).get();
            Posizione posizione = new Posizione(nome, descrizione, prezzo);
            posizioneRepo.save(posizione);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @DeleteMapping(path = "/eliminaPosizione")
    public String eliminaPosizione(@CookieValue Long id, @RequestParam Long idPosizione) {
        try {
            adminRepo.findById(id).get();
            Iterator<GruppoOmbrelloni> i = ombrelloneRepo.findAll().iterator();
            while(i.hasNext()) {
                GruppoOmbrelloni go = i.next();
                if(go.getPosizione().getId() == idPosizione)
                    throw new Exception("Non può essere eliminata una posizione utilizzata da un gruppo di ombrelloni esistenti");
            }
            posizioneRepo.deleteById(idPosizione);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping(path = "/stagioni")
    public List<Stagione> stagioni(@CookieValue Long id) {
        try {
            adminRepo.findById(id).get();
            List<Stagione> stagioni = new ArrayList<>();
            stagioneRepo.findAll().forEach(stagioni::add);
            return stagioni;
        } catch (Exception e) {
            return null;
        }
    }

    @DeleteMapping(path = "/eliminaStagione")
    public String eliminaStagione(@CookieValue Long id, @RequestParam Long idStagione) {
        try {
            adminRepo.findById(id).get();
            Iterator<GruppoOmbrelloni> i = ombrelloneRepo.findAll().iterator();
            while(i.hasNext()) {
                GruppoOmbrelloni go = i.next();
                for (Stagione stagione: go.getStagioni())
                    if(stagione.getId() == idStagione)
                        throw new Exception("Non può essere eliminata una stagione utilizzata da un gruppo di ombrelloni esistenti");
            }
            stagioneRepo.deleteById(idStagione);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping(path = "/creaStagione")
    public String creaStagione(@CookieValue Long id, @RequestParam String nome, @RequestParam Double prezzo, @RequestParam Integer sorting) {
        try {
            adminRepo.findById(id).get();
            Stagione stagione = new Stagione(nome, prezzo, sorting);
            stagioneRepo.save(stagione);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PutMapping(path = "/addStagione")
    public String addStagione(@CookieValue Long id, @RequestParam Long ombrelloneId, @RequestParam Long stagioneId) {
        try {
            adminRepo.findById(id).get();
            GruppoOmbrelloni ombrellone = ombrelloneRepo.findById(ombrelloneId).orElse(null);
            Stagione stagione = stagioneRepo.findById(stagioneId).orElse(null);
            ombrellone.addStagione(stagione);
            ombrelloneRepo.save(ombrellone);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
