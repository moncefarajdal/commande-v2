package com.example.commandev2.service.impl;

import com.example.commandev2.bean.CommandeItem;
import com.example.commandev2.dao.CommandeItemDao;
import com.example.commandev2.service.facade.CommandeItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandeItemServiceImpl implements CommandeItemService {

    @Autowired private CommandeItemDao commandeItemDao;
    @Override
    public List<CommandeItem> findByCommandeReference(String reference) {
        return commandeItemDao.findByCommandeReference(reference);
    }

    @Override
    public void save(CommandeItem commandeItem) {
        commandeItemDao.save(commandeItem);
    }

    @Override
    public int deleteByCommandeReference(String reference) {
        return commandeItemDao.deleteByCommandeReference(reference);
    }
}
