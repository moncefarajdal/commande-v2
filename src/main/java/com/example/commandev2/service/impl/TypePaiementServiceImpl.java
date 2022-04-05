package com.example.commandev2.service.impl;

import com.example.commandev2.bean.TypePaiement;
import com.example.commandev2.dao.TypePaiementDao;
import com.example.commandev2.service.facade.TypePaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TypePaiementServiceImpl implements TypePaiementService {

    @Autowired private TypePaiementDao typePaiementDao;

    @Override
    public TypePaiement findByCode(String code) {
        return typePaiementDao.findByCode(code);
    }
}
