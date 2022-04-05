package com.example.commandev2.service.impl;

import com.example.commandev2.bean.Commande;
import com.example.commandev2.bean.Paiement;
import com.example.commandev2.bean.TypePaiement;
import com.example.commandev2.dao.PaiementDao;
import com.example.commandev2.service.facade.CommandeService;
import com.example.commandev2.service.facade.PaiementService;
import com.example.commandev2.service.facade.TypePaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaiementServiceImpl implements PaiementService {

    @Autowired private PaiementDao paiementDao;
    @Autowired private CommandeService commandeService;
    @Autowired private TypePaiementService typePaiementService;

//    @Override
//    public int save(Paiement paiement) {
//        Commande commande = commandeService.findByReference(paiement.getCommande().getReference());
//        paiement.setCommande(commande);
//
//        TypePaiement typePaiement = typePaiementService.findByCode(paiement.getTypePaiement().getCode());
//        paiement.setTypePaiement(typePaiement);
//
//        if (typePaiement == null) {
//            return -1;
//        } else if (commande == null) {
//            return -2;
//        } else if (commande.getTotalPaye() + paiement.getMontant() > commande.getTotal()) {
//            return -3;
//        } else {
//            paiementDao.save(paiement);
//            commande.setTotalPaye(commande.getTotalPaye() + paiement.getMontant());
//            commandeService.save(commande);
//            return 1;
//        }
//    }

    @Override
    public int save(Paiement paiement) {
        paiementDao.save(paiement);
        return 1;
    }

    @Override
    public Paiement findByCode(String code) {
        return paiementDao.findByCode(code);
    }

    @Override
    public int deleteByCode(String code) {
        return paiementDao.deleteByCode(code);
    }

    @Override
    public int deleteByCommandeReference(String reference) {
        return paiementDao.deleteByCommandeReference(reference);
    }

    @Override
    public List<Paiement> findByCommandeReference(String reference) {
        return paiementDao.findByCommandeReference(reference);
    }
}
