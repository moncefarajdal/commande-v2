package com.example.commandev2.dao;

import com.example.commandev2.bean.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDao extends JpaRepository<Client, Long> {

    Client findByCin(String cin);

}
