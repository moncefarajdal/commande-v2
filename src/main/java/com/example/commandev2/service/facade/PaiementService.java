package com.example.commandev2.service.facade;

import com.example.commandev2.bean.Paiement;

import java.util.List;

public interface PaiementService {

    int save(Paiement paiement);
    Paiement findByCode(String code);
    int deleteByCode(String code);
    int deleteByCommandeReference(String reference);
    List<Paiement> findByCommandeReference(String reference);

}
