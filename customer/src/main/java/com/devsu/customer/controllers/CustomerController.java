package com.devsu.customer.controllers;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.customer.dtos.CustomerInputDTO;
import com.devsu.customer.dtos.CustomerResponseDTO;
import com.devsu.customer.entities.Customer;
import com.devsu.customer.services.impl.CustomerServiceImp;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/clientes")
public class CustomerController {

    @Autowired
    private CustomerServiceImp customerServiceImp;
    
   
    public CustomerController(CustomerServiceImp customerServiceImp) {
        this.customerServiceImp = customerServiceImp;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        Customer customerRegister = customerServiceImp.registerCustomer(customer);

        return ResponseEntity.status(HttpStatus.CREATED).body(customerRegister);
    }

    @GetMapping("/listar")
    public ResponseEntity<Customer> listCustomer() {
        Customer customer = customerServiceImp.listarCustomer();
        return ResponseEntity.ok(customer);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        Customer customer = customerServiceImp.getCustomer(id);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{id}")
     public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerInputDTO customerDetails) {
        CustomerResponseDTO updatedCustomer = customerServiceImp.updateCustomer(id, customerDetails);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        customerServiceImp.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
