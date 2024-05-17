package com.devsu.account.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.account.dtos.AccountStatementReport;
import com.devsu.account.services.impl.MovementServiceImp;

@RestController
@RequestMapping("/reportes")
public class ReportController {

    @Autowired
    private MovementServiceImp movementServiceImp;

    public ReportController(MovementServiceImp movementServiceImp) {
        this.movementServiceImp = movementServiceImp;
    }

    @GetMapping()
    public ResponseEntity<List<AccountStatementReport>> getAccountStatements(
            @RequestParam("fechaInicio") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("fechaFin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam("clienteId") Long clientId) {

        List<AccountStatementReport> reports = movementServiceImp.getAccountStatements(startDate, endDate, clientId);
        return ResponseEntity.ok(reports);
    }
}
