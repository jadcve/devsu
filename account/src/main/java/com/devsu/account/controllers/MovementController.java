package com.devsu.account.controllers;

import com.devsu.account.dtos.AccountStatementReport;
import com.devsu.account.entities.Movement;
import com.devsu.account.services.impl.MovementServiceImp;
import com.devsu.account.validations.InsufficientBalanceException;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/movimientos")
public class MovementController {

    @Autowired
    private MovementServiceImp movementServiceImp;

    public MovementController(MovementServiceImp movementServiceImp) {
        this.movementServiceImp = movementServiceImp;
    }

    @GetMapping
    public List<Movement> getAllMovements() {
        return movementServiceImp.getAllMovements();
    }

    @GetMapping("/{id}")
    public Movement getMovementById(@PathVariable("id") Long id) {
        return movementServiceImp.getMovementById(id);
    }

    @PostMapping
    public Movement createMovement(@RequestBody Movement movement) {
        return movementServiceImp.createMovement(movement);
    }

    @PutMapping("/{id}")
    public Movement updateMovement(@PathVariable("id") Long id, @RequestBody Movement movement) {
        return movementServiceImp.updateMovement(id, movement);
    }

    @DeleteMapping("/{id}")
    public void deleteMovement(@PathVariable("id") Long id) {
        movementServiceImp.deleteMovement(id);
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }


    @ControllerAdvice
    class GlobalExceptionHandler {

        @ExceptionHandler(InsufficientBalanceException.class)
        public ResponseEntity<String> handleInsufficientBalanceException(InsufficientBalanceException ex, WebRequest request) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
}
}
