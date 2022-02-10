package it.unicam.ids.casotto;

import it.unicam.ids.casotto.addetto.Addetto;
import it.unicam.ids.casotto.addetto.AddettoRepo;
import it.unicam.ids.casotto.admin.Admin;
import it.unicam.ids.casotto.admin.AdminRepo;
import it.unicam.ids.casotto.cliente.Cliente;
import it.unicam.ids.casotto.cliente.ClienteRepo;
import it.unicam.ids.casotto.user.User;
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

    @PostMapping("/registra")
    public String registraCliente(@RequestParam String username, @RequestParam String password) {
        try {
            for (Cliente c : clienteRepo.findAll())
                if (c.getUsername().equals(username))
                    throw new Exception();
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
                    throw new Exception();
            Admin a = new Admin(username, password);
            adminRepo.save(a);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/registraAddetto")
    public String registraAddetto(@RequestParam String username, @RequestParam String password) {
        try {
            for (Addetto a : addettoRepo.findAll())
                if (a.getUsername().equals(username))
                    throw new Exception();
            Addetto a = new Addetto(username, password);
            addettoRepo.save(a);
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
}
