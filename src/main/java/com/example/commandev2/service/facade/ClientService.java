package com.example.commandev2.service.facade;

import com.example.commandev2.bean.Client;

public interface ClientService {

    Client findByCin(String cin);

}
