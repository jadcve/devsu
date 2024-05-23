package com.devsu.account.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.account.entities.Account;
import com.devsu.account.services.impl.AccountServiceImp;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/cuentas")
public class AccountController {

    @Autowired
    private AccountServiceImp accountServiceImp;

    public AccountController(AccountServiceImp accountServiceImp) {
        this.accountServiceImp = accountServiceImp;
    }

    
    @PostMapping("/crear")
    public ResponseEntity<?> createAccount(@Valid @RequestBody Account account, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        Account accountRegister = accountServiceImp.registerAccount(account);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountRegister);
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        Account account = accountServiceImp.getAccountById(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id, @Valid @RequestBody Account account, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        Account updatedAccount = accountServiceImp.updateAccount(id, account);
        if (updatedAccount == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/eliminar/{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountServiceImp.deleteAccount(id);
    }


    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
    

}
