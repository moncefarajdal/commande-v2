package com.example.commandev2.service.facade;

import com.example.commandev2.bean.Commande;

public interface CommandeService {

    int save(Commande commande);
    Commande findByReference(String reference);
    int deleteWithAssociatedPaiements(String reference);
    int deleteCommandeWithPaiementsAndItems(String reference);
    int deleteByReference(String reference);

}
