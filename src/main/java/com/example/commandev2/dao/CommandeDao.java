package com.example.commandev2.dao;

import com.example.commandev2.bean.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeDao extends JpaRepository<Commande, Long> {

    Commande findByReference(String reference);
    int deleteByReference(String reference);

}
