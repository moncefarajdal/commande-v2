package com.example.commandev2.service.impl;

import com.example.commandev2.bean.Produit;
import com.example.commandev2.dao.ProduitDao;
import com.example.commandev2.service.facade.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProduitServiceImpl implements ProduitService {

    @Autowired private ProduitDao produitDao;
    @Override
    public Produit findByCode(String code) {
        return produitDao.findByCode(code);
    }

    @Override
    public int save(Produit produit) {
        produitDao.save(produit);
        return 1;
    }
}
