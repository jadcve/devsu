package com.devsu.account.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.devsu.account.entities.Account;
import com.devsu.account.entities.Movement;
import com.devsu.account.entities.MovementType;
import com.devsu.account.repositories.AccountRepository;
import com.devsu.account.repositories.MovementRepository;

class MovementServiceTest {

    @InjectMocks
    private MovementServiceImp movementService;

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMovementDeposit() {
        Account account = new Account();
        account.setId(1L);
        account.setSaldo(1000.0);

        Movement movement = new Movement();
        movement.setAccount(account);
        movement.setMovimiento(MovementType.DEPOSITO);
        movement.setValor(200.0);

        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(movementRepository.save(any(Movement.class))).thenReturn(movement);

        Movement createdMovement = movementService.createMovement(movement);

        assertNotNull(createdMovement);
        assertEquals(1200.0, account.getSaldo());
        verify(accountRepository, times(1)).findById(anyLong());
        verify(accountRepository, times(1)).save(any(Account.class));
        verify(movementRepository, times(1)).save(any(Movement.class));
    }

    @Test
    void testCreateMovementWithdrawal() {
        Account account = new Account();
        account.setId(1L);
        account.setSaldo(1000.0);

        Movement movement = new Movement();
        movement.setAccount(account);
        movement.setMovimiento(MovementType.RETIRO);
        movement.setValor(200.0);

        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(movementRepository.save(any(Movement.class))).thenReturn(movement);

        Movement createdMovement = movementService.createMovement(movement);

        assertNotNull(createdMovement);
        assertEquals(800.0, account.getSaldo());
        verify(accountRepository, times(1)).findById(anyLong());
        verify(accountRepository, times(1)).save(any(Account.class));
        verify(movementRepository, times(1)).save(any(Movement.class));
    }

    @Test
    void testCreateMovementWithdrawalInsufficientBalance() {
        Account account = new Account();
        account.setId(1L);
        account.setSaldo(100.0);

        Movement movement = new Movement();
        movement.setAccount(account);
        movement.setMovimiento(MovementType.RETIRO);
        movement.setValor(200.0);

        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        com.devsu.account.validations.InsufficientBalanceException exception = assertThrows(com.devsu.account.validations.InsufficientBalanceException.class, () -> {
            movementService.createMovement(movement);
        });

        assertEquals("Saldo insuficiente en la cuenta", exception.getMessage());
        verify(accountRepository, times(1)).findById(anyLong());
        verify(accountRepository, never()).save(any(Account.class));
        verify(movementRepository, never()).save(any(Movement.class));
    }
}
