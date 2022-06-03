package it.unicam.ids.casotto;

import it.unicam.ids.casotto.cliente.Cliente;
import it.unicam.ids.casotto.cliente.ClienteRepo;
import it.unicam.ids.casotto.fornitore.FornitoreRepo;
import it.unicam.ids.casotto.notifica.Notifica;
import it.unicam.ids.casotto.notifica.NotificaRepo;
import it.unicam.ids.casotto.prodotto.Prodotto;
import it.unicam.ids.casotto.prodotto.ProdottoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/fornitore")
public class FornitoreController {
    @Autowired
    private FornitoreRepo fornitoreRepo;
    @Autowired
    private ProdottoRepo prodottoRepo;
    @Autowired
    private ClienteRepo clienteRepo;
    @Autowired
    private NotificaRepo notificaRepo;

    @GetMapping(path = "/prodotti")
    public List<Prodotto> prodotti(@CookieValue Long id) {
        try {
            fornitoreRepo.findById(id).get();
            List<Prodotto> prodotti = new ArrayList<>();
            prodottoRepo.findAll().forEach(prodotti::add);
            return prodotti;
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping(path = "/creaProdotto")
    public String creaProdotto(@CookieValue Long id, @RequestParam String nome, @RequestParam Double prezzo,
                               @RequestParam Integer numeroDisponibilita) {
        try {
            fornitoreRepo.findById(id).get();
            Prodotto prodotto = new Prodotto(nome, prezzo, numeroDisponibilita);
            prodottoRepo.save(prodotto);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PutMapping(path = "/rifornisciProdotto")
    public String rifornisciProdotto(@CookieValue Long id, @RequestParam Long idProdotto, @RequestParam Integer numeroDisponibilita) {
        try {
            fornitoreRepo.findById(id).get();
            Prodotto prodotto = prodottoRepo.findById(idProdotto).get();
            prodotto.setNumeroDisponibilita(numeroDisponibilita);
            prodottoRepo.save(prodotto);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PutMapping(path = "/compraProdotto")
    public String compraProdotto(@CookieValue Long id, @RequestParam Long idProdotto, @RequestParam Long idCliente) {
        try {
            fornitoreRepo.findById(id).get();
            Prodotto prodotto = prodottoRepo.findById(idProdotto).get();
            Cliente cliente = clienteRepo.findById(idCliente).get();
            prodotto.compraProdotto(cliente);
            prodottoRepo.save(prodotto);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @DeleteMapping(path = "/eliminaProdotto")
    public String eliminaProdotto(@CookieValue Long id, @RequestParam Long idProdotto) {
        try {
            fornitoreRepo.findById(id).get();
            Prodotto prodotto = prodottoRepo.findById(idProdotto).get();
            prodotto.getPrenotazioni().forEach(p -> notificaRepo.save(new Notifica(p.getCliente(),
                    "Il prodotto " + prodotto.getNome() + " è stato eliminato")));
            prodottoRepo.delete(prodotto);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
