package com.example.commandev2.service.facade;

import com.example.commandev2.bean.CommandeItem;

import java.util.List;

public interface CommandeItemService {

    List<CommandeItem> findByCommandeReference(String reference);
    void save(CommandeItem commandeItem);
    int deleteByCommandeReference(String reference);

}
