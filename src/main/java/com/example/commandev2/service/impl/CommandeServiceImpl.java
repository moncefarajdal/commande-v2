package com.example.commandev2.service.impl;

import com.example.commandev2.bean.*;
import com.example.commandev2.dao.CommandeDao;
import com.example.commandev2.service.facade.*;
import com.example.commandev2.service.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommandeServiceImpl implements CommandeService {

    @Autowired private CommandeDao commandeDao;
    @Autowired private ProduitService produitService;
    @Autowired private CommandeItemService commandeItemService;
    @Autowired private PaiementService paiementService;
    @Autowired private ClientService clientService;
    @Autowired private TypePaiementService typePaiementService;

    private void prepare(Commande commande) {
        for (CommandeItem commandeItem : commande.getCommandeItems()) {
            Produit produit = produitService.findByCode(commandeItem.getProduit().getCode());
            commandeItem.setProduit(produit);
        }

        Client client = clientService.findByCin(commande.getClient().getCin());
        commande.setClient(client);

        for (Paiement paiement : commande.getPaiements()) {
            TypePaiement typePaiement = typePaiementService.findByCode(paiement.getTypePaiement().getCode());
            paiement.setTypePaiement(typePaiement);
        }
    }

    private int validate(Commande commande) {
        if (findByReference(commande.getReference()) != null) {
            return -1;
        } else if (ListUtil.isEmpty(commande.getCommandeItems())) {
            return -2;
        } else if (countProduitNull(commande.getCommandeItems()) != 0) {
            return -3;
        } else if (ListUtil.isEmpty(commande.getPaiements())) {
            return -4;
        } else if (commande.getClient() == null) {
            return -5;
        } else if (countPaiementExist(commande.getPaiements()) > 0) {
            return -6;
        } else if (countNonExistedTypePaiements(commande.getPaiements()) > 0) {
            return -7;
        } else {
            return 1;
        }
    }

    private int countProduitNull(List<CommandeItem> commandeItems) {
        int cp = 0;
        for (CommandeItem commandeItem : commandeItems) {
            if (commandeItem.getProduit() == null) cp++;
        }
        return cp;
    }

    private int countNonExistedTypePaiements(List<Paiement> paiements) {
        int c = 0;
        for (Paiement paiement : paiements) {
            if (paiement.getTypePaiement() == null) {
                c++;
            }
        }
        return c;
    }

    private int countPaiementExist(List<Paiement> paiements) {
        int cp = 0;
        for (Paiement paiement : paiements) {
            if (paiement.getCode() == null) cp++;
        }
        return cp;
    }

    public void calculateTotal(Commande commande) {
        double total = 0;
        for (Paiement paiement: commande.getPaiements()) {
            total += paiement.getMontant();
        }
        commande.setTotalPaye(total);
    }

//    private int validateDelete(Commande commande) {
//        if (findByReference(commande.getReference()) == null) {
//            return -1;
//        } else if (paiementExist(commande) > 0) {
//            return -2;
//        } else {
//            return 1;
//        }
//    }
//
//    private void handlDeleteProcess(Commande commande) {
//        paiementService.deleteByCommandeReference(commande.getReference());
//        commandeItemService.deleteByCommandeReference(commande.getReference());
//        commandeDao.deleteByReference(commande.getReference());
//    }

    @Override
    public int save(Commande commande) {
        prepare(commande);
        int result = validate(commande);
        calculateTotal(commande);
        if (result > 0) {
            commandeDao.save(commande);
            commande.getCommandeItems().forEach(e -> {
                e.setCommande(commande);
                e.getCommande().setTotal(e.getCommande().getTotal() + e.getPrix() * e.getQte());
                commandeItemService.save(e);
            });
            commande.getPaiements().forEach(p -> {
                p.setCommande(commande);
                paiementService.save(p);
            });
        }
        return 1;
    }

    @Override
    public Commande findByReference(String reference) {
        return commandeDao.findByReference(reference);
    }

    @Override
    @Transactional
    public int deleteWithAssociatedPaiements(String reference) {
        int paiementDelete = paiementService.deleteByCommandeReference(reference);
        int commandeDelete = commandeDao.deleteByReference(reference);
        return paiementDelete + commandeDelete;
    }

    @Override
    public int deleteCommandeWithPaiementsAndItems(String reference) {
        int result1 = paiementService.deleteByCommandeReference(reference);
        int result2 = commandeItemService.deleteByCommandeReference(reference);
        int result3 = commandeDao.deleteByReference(reference);
        return result1 + result2 + result3;
    }

    @Override
    @Transactional
    public int deleteByReference(String reference) {
        return commandeDao.deleteByReference(reference);
    }
}
