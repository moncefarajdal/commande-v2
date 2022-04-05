package com.example.commandev2.dao;

import com.example.commandev2.bean.CommandeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandeItemDao extends JpaRepository<CommandeItem, Long> {

    List<CommandeItem> findByCommandeReference(String reference);
    int deleteByCommandeReference(String reference);


}
