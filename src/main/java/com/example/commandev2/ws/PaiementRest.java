package com.example.commandev2.ws;

import com.example.commandev2.bean.Paiement;
import com.example.commandev2.service.facade.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/paiement")
public class PaiementRest {

    @Autowired
    private PaiementService paiementService;

    @PostMapping("/")
    public int save(@RequestBody Paiement paiement) {
        return paiementService.save(paiement);
    }

    @GetMapping("/code/{code}")
    public Paiement findByCode(@PathVariable String code) {
        return paiementService.findByCode(code);
    }

    @DeleteMapping("/commande/reference/{reference}")
    public int deleteByCommandeReference(@PathVariable String reference) {
        return paiementService.deleteByCommandeReference(reference);
    }

    @GetMapping("/commande/reference/{reference}")
    public List<Paiement> findByCommandeReference(@PathVariable String reference) {
        return paiementService.findByCommandeReference(reference);
    }
}
