package com.example.commandev2.service.facade;

import com.example.commandev2.bean.Produit;

public interface ProduitService {

    Produit findByCode(String code);
    int save(Produit produit);

}
