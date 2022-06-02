package it.unicam.ids.casotto;

import it.unicam.ids.casotto.addetto.Addetto;
import it.unicam.ids.casotto.addetto.AddettoRepo;
import it.unicam.ids.casotto.admin.Admin;
import it.unicam.ids.casotto.admin.AdminRepo;
import it.unicam.ids.casotto.cliente.Cliente;
import it.unicam.ids.casotto.cliente.ClienteRepo;
import it.unicam.ids.casotto.notifica.Notifica;
import it.unicam.ids.casotto.notifica.NotificaRepo;
import it.unicam.ids.casotto.stagione.Stagione;
import it.unicam.ids.casotto.user.User;
import it.unicam.ids.casotto.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UtenteController {
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private ClienteRepo clienteRepo;
    @Autowired
    private AddettoRepo addettoRepo;
    @Autowired
    private NotificaRepo notificaRepo;
    @Autowired
    private UserRepo utenteRepo;

    @PostMapping("/registra")
    public String registraCliente(@RequestParam String username, @RequestParam String password) {
        try {
            for (Cliente c : clienteRepo.findAll())
                if (c.getUsername().equals(username))
                    throw new Exception("username gia esistente");
            Cliente c = new Cliente(username, password);
            clienteRepo.save(c);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/registraAdmin")
    public String registraAdmin(@RequestParam String username, @RequestParam String password) {
        try {
            for (Admin a : adminRepo.findAll())
                if (a.getUsername().equals(username))
                    throw new Exception("username gia esistente");
            Admin a = new Admin(username, password);
            adminRepo.save(a);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/autentica")
    public User autenticaCliente(@RequestParam String username, @RequestParam String password) {
        try {
            List<User> users = new ArrayList<>();
            clienteRepo.findAll().forEach(users::add);
            adminRepo.findAll().forEach(users::add);
            addettoRepo.findAll().forEach(users::add);
            for (User u : users)
                if (u.getUsername().equals(username))
                    if (u.checkPassword(password))
                        return u;
                    else
                        throw new Exception("password errata");
            throw new Exception("utente inesistente");
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping(path = "/notifiche")
    public List<Notifica> notifiche(@CookieValue Long id) {
        try {
            utenteRepo.findById(id).get();
            List<Notifica> notifiche = new ArrayList<>();
            notificaRepo.findAll().forEach(n -> {
                if(n.getUtente().getId() == id)
                    notifiche.add(n);
            });
            return notifiche;
        } catch (Exception e) {
            return null;
        }
    }

    @DeleteMapping(path = "/eliminaNotifica")
    public String eliminaNotifica(@CookieValue Long id, @RequestParam Long idNotifica) {
        try {
            utenteRepo.findById(id).get();
            Notifica notifica = notificaRepo.findById(idNotifica).get();
            if(notifica.getUtente().getId() != id)
                throw new Exception("notifica non appartenente all'utente");
            else
                notificaRepo.delete(notifica);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
