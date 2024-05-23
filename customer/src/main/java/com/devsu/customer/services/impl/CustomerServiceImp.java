package com.devsu.customer.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.devsu.customer.dtos.AccountDTO;
import com.devsu.customer.dtos.AccountResponseDTO;
import com.devsu.customer.dtos.CustomerInputDTO;
import com.devsu.customer.dtos.CustomerResponseDTO;
import com.devsu.customer.entities.Customer;
import com.devsu.customer.entities.CustomerAccount;
import com.devsu.customer.exceptions.AccountNotFoundException;
import com.devsu.customer.repositories.CustomerRepository;
import com.devsu.customer.services.CustomerService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImp implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountServiceClient accountServiceClient;

    public CustomerServiceImp(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, AccountServiceClient accountServiceClient) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountServiceClient = accountServiceClient;
    }

    @Override
    @Transactional
    public Customer registerCustomer(Customer customer) {
        Optional<Customer> existingCustomer = customerRepository.findById((long) customer.getCustomerId());
        if (existingCustomer.isPresent()) {
            throw new IllegalStateException("Customer ID already in use");
        }
        customer.setContrasena(passwordEncoder.encode(customer.getContrasena()));
        for (CustomerAccount account : customer.getAccounts()) {
            account.setCustomer(customer);
        }
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public CustomerResponseDTO getCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        customer.getAccounts().size();
        CustomerResponseDTO responseDTO = new CustomerResponseDTO();
        responseDTO.setId(customer.getId());
        responseDTO.setNombre(customer.getNombre());
        responseDTO.setGenero(customer.getGenero());
        responseDTO.setEdad(customer.getEdad());
        responseDTO.setIdentificacion(customer.getIdentificacion());
        responseDTO.setDireccion(customer.getDireccion());
        responseDTO.setTelefono(customer.getTelefono());
        responseDTO.setEstado(customer.isEstado());
        responseDTO.setAccounts(customer.getAccounts().stream().map(account -> {
            AccountResponseDTO accountDTO = new AccountResponseDTO();
            accountDTO.setId(account.getId());
            accountDTO.setAccountId(account.getAccountId());
            return accountDTO;
        }).collect(Collectors.toList()));

        return responseDTO;
    }

 

    public CustomerResponseDTO updateCustomer(Long id, CustomerInputDTO customerDetails) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow();
        customer.setNombre(customerDetails.getNombre());
        customer.setContrasena(passwordEncoder.encode(customerDetails.getContrasena()));
        customer.setEstado(customerDetails.isEstado());
        customer.setGenero(customerDetails.getGenero());
        customer.setEdad(customerDetails.getEdad());
        customer.setIdentificacion(customerDetails.getIdentificacion());
        customer.setDireccion(customerDetails.getDireccion());
        customer.setTelefono(customerDetails.getTelefono());
        Customer updatedCustomer = customerRepository.save(customer);
        List<AccountResponseDTO> accountResponses = updatedCustomer.getAccounts().stream()
            .map(account -> {
                try {
                    AccountDTO accountData = accountServiceClient.getAccountById(account.getAccountId());
                    return convertAccountToDto(accountData);
                } catch (AccountNotFoundException e) {
                    return null;
                }
            })
            .filter(account -> account != null)
            .collect(Collectors.toList());
        return convertToDto(updatedCustomer, accountResponses);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow();
        customerRepository.delete(customer);
    }


    private CustomerResponseDTO convertToDto(Customer customer, List<AccountResponseDTO> accounts) {
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setId(customer.getId());
        dto.setNombre(customer.getNombre());
        dto.setGenero(customer.getGenero());
        dto.setEdad(customer.getEdad());
        dto.setIdentificacion(customer.getIdentificacion());
        dto.setDireccion(customer.getDireccion());
        dto.setTelefono(customer.getTelefono());
        dto.setEstado(customer.isEstado());
        dto.setAccounts(accounts);
        return dto;
    }

    private AccountResponseDTO convertAccountToDto(AccountDTO accountData) {
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setId(accountData.getId());
        dto.setAccountId(accountData.getId());
        return dto;
    }

    @Override
    public CustomerResponseDTO updateCustomer(Long id, Customer customerDetails) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCustomer'");
    }

}
