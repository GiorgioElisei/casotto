package it.unicam.ids.casotto;

import it.unicam.ids.casotto.addetto.Addetto;
import it.unicam.ids.casotto.addetto.AddettoRepo;
import it.unicam.ids.casotto.admin.AdminRepo;
import it.unicam.ids.casotto.cliente.ClienteRepo;
import it.unicam.ids.casotto.gruppo_ombrellone.GruppoOmbrelloni;
import it.unicam.ids.casotto.gruppo_ombrellone.GruppoOmbrelloniRepo;
import it.unicam.ids.casotto.posizione.PosizioneRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    @GetMapping(path = "/ombrelloni")
    public List<GruppoOmbrelloni> ombrelloni(@CookieValue Long id) {
        try {
            Addetto a = addettoRepo.findById(id).get();
            List<GruppoOmbrelloni> ombrelloni = new ArrayList<>();
            ombrelloneRepo.findAll().forEach(o -> {
                if(a.getPosizioni().contains(o.getPosizione()))
                    ombrelloni.add(o);
            });
            return ombrelloni;
        } catch (Exception e) {
            return null;
        }
    }
}
