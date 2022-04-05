package com.example.commandev2.service.facade;

import com.example.commandev2.bean.Paiement;
import com.example.commandev2.bean.TypePaiement;

public interface TypePaiementService {

    TypePaiement findByCode(String code);

}
