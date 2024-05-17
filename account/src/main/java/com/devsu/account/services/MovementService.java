package com.devsu.account.services;

import java.util.*;

import com.devsu.account.dtos.AccountStatementReport;
import com.devsu.account.entities.Movement;

public interface MovementService {

    Movement createMovement(Movement movement);

    List<Movement> getAllMovements();

    Movement getMovementById(Long id);

    Movement updateMovement(Long id, Movement movement);

    void deleteMovement(Long id);
    
    List<AccountStatementReport> getAccountStatements(Date startDate, Date endDate, Long clientId);


}
