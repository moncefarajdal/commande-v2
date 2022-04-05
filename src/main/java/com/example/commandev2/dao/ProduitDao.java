package com.example.commandev2.dao;

import com.example.commandev2.bean.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduitDao extends JpaRepository<Produit, Long> {

    Produit findByCode(String code);
//    int deleteByCode(String code);

}
