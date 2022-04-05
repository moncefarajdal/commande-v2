package com.example.commandev2.ws;

import com.example.commandev2.bean.Produit;
import com.example.commandev2.service.facade.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/produit")
public class ProduitRest {

    @Autowired
    private ProduitService produitService;

    @GetMapping("/code/{code}")
    public Produit findByCode(@PathVariable String code) {
        return produitService.findByCode(code);
    }

    @PostMapping("/")
    public int save(@RequestBody Produit produit) {
        return produitService.save(produit);
    }
}
