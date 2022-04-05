package com.example.commandev2.dao;

import com.example.commandev2.bean.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementDao extends JpaRepository<Paiement, Long> {

    Paiement findByCode(String code);
    int deleteByCode(String code);
    int deleteByCommandeReference(String reference);
    List<Paiement> findByCommandeReference(String reference);

}
