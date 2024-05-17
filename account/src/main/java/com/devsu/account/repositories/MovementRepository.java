package com.devsu.account.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.devsu.account.entities.Account;
import com.devsu.account.entities.Movement;

public interface MovementRepository  extends CrudRepository<Movement, Long>{

    List<Movement> findByAccountAndFechaBetween(Account account, Date startDate, Date endDate);

}
