package com.example.commandev2.service.impl;

import com.example.commandev2.bean.Client;
import com.example.commandev2.dao.ClientDao;
import com.example.commandev2.service.facade.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired private ClientDao clientDao;

    @Override
    public Client findByCin(String cin) {
        return clientDao.findByCin(cin);
    }
}
