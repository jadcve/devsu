package com.devsu.account.services.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsu.account.dtos.AccountStatementReport;
import com.devsu.account.dtos.CustomerAccountDTO;
import com.devsu.account.dtos.CustomerDTO;
import com.devsu.account.entities.Account;
import com.devsu.account.entities.Movement;
import com.devsu.account.entities.MovementType;
import com.devsu.account.repositories.AccountRepository;
import com.devsu.account.repositories.MovementRepository;
import com.devsu.account.services.MovementService;
import com.devsu.account.validations.InsufficientBalanceException;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

@Service
public class MovementServiceImp implements MovementService {

    @Autowired
    private MovementRepository movementRepository;
    
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerServiceClient customerServiceClient;

    public MovementServiceImp(MovementRepository movementRepository, AccountRepository accountRepository) {
        this.movementRepository = movementRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public Movement createMovement(Movement movement) {
        if (movement == null) {
            throw new IllegalArgumentException("Movement is required");
        }
        Account account = accountRepository.findById(movement.getAccount().getId())
                .orElseThrow();

        Double nuevoSaldo = account.getSaldo();
        if (movement.getMovimiento() == MovementType.DEPOSITO) {
            nuevoSaldo += movement.getValor();
        } else if (movement.getMovimiento() == MovementType.RETIRO) {
            if (nuevoSaldo < movement.getValor()) {
                throw new InsufficientBalanceException("Saldo insuficiente en la cuenta");
            }
            nuevoSaldo -= movement.getValor();
        }

        account.setSaldo(nuevoSaldo);
        movement.setFecha(new Date());
        accountRepository.save(account);
        return movementRepository.save(movement);
    }

    @Override
    @Transactional
    public List<Movement> getAllMovements() {
        return (List<Movement>) movementRepository.findAll();
    }

    @Override
    @Transactional
    public Movement getMovementById(Long id) {
        return movementRepository.findById(id)
                .orElseThrow();
       
    }

    @Override
    @Transactional
    public Movement updateMovement(Long id, Movement movement) {
        Movement existingMovement = movementRepository.findById(id)
        .orElseThrow();

        if (movement.getFecha() != null) {
            existingMovement.setFecha(movement.getFecha());
        }
        if (movement.getMovimiento() != null) {
            existingMovement.setMovimiento(movement.getMovimiento());
        }
        if (movement.getValor() != null) {
            existingMovement.setValor(movement.getValor());
        }

        return movementRepository.save(existingMovement);
        
       
    }

    @Override
    @Transactional
    public void deleteMovement(Long id) {
        movementRepository.deleteById(id);
    }

    @Override
    public List<AccountStatementReport> getAccountStatements(Date startDate, Date endDate, Long clientId) {
        CustomerDTO customer = customerServiceClient.getCustomerById(clientId);

        System.out.println("Customer: " + customer); // Debugging

        List<AccountStatementReport> reports = new ArrayList<>();

        for (CustomerAccountDTO customerAccount : customer.getAccounts()) {
            Account account = accountRepository.findById(customerAccount.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

                System.out.println("Account: " + account); // Debugging

            List<Movement> movements = movementRepository.findByAccountAndFechaBetween(account, startDate, endDate);

            System.out.println("Movements: " + movements); // Debugging

            List<AccountStatementReport.MovementDetail> movementDetails = movements.stream().map(movement -> {
                AccountStatementReport.MovementDetail detail = new AccountStatementReport.MovementDetail();
                detail.setFecha(movement.getFecha());
                detail.setMovimiento(movement.getMovimiento().name());
                detail.setMonto(movement.getValor());
                return detail;
            }).collect(Collectors.toList());

            AccountStatementReport report = new AccountStatementReport();
            report.setAccountId(account.getId());
            report.setNumeroCuenta(account.getNumero());
            report.setSaldoCuenta(account.getSaldo());
            report.setMovimientos(movementDetails);

            reports.add(report);
        }

        return reports;
    }
    
    
}
