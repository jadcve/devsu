package com.devsu.customer.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;

import com.devsu.customer.dtos.CustomerInputDTO;
import com.devsu.customer.dtos.CustomerResponseDTO;
import com.devsu.customer.entities.Customer;
import com.devsu.customer.entities.CustomerAccount;
import com.devsu.customer.repositories.CustomerRepository;
import com.devsu.customer.services.CustomerService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImp implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public CustomerServiceImp(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
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
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        customer.setNombre(customerDetails.getNombre());
        customer.setContrasena(passwordEncoder.encode(customerDetails.getContrasena()));
        customer.setEstado(customerDetails.isEstado());
        customer.setGenero(customerDetails.getGenero());
        customer.setEdad(customerDetails.getEdad());
        customer.setIdentificacion(customerDetails.getIdentificacion());
        customer.setDireccion(customerDetails.getDireccion());
        customer.setTelefono(customerDetails.getTelefono());
        return customerRepository.save(customer);
    }
    
    public CustomerResponseDTO updateCustomer(Long id, CustomerInputDTO customerDetails) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        customer.setNombre(customerDetails.getNombre());
        customer.setContrasena(passwordEncoder.encode(customerDetails.getContrasena()));
        customer.setEstado(customerDetails.isEstado());
        customer.setGenero(customerDetails.getGenero());
        customer.setEdad(customerDetails.getEdad());
        customer.setIdentificacion(customerDetails.getIdentificacion());
        customer.setDireccion(customerDetails.getDireccion());
        customer.setTelefono(customerDetails.getTelefono());
        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDto(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        customerRepository.delete(customer);
    }

    @Transactional
    public List<Customer> listarCustomer() {
        Iterable<Customer> customersIterable = customerRepository.findAll();
        List<Customer> customers = StreamSupport.stream(customersIterable.spliterator(), false)
                                                 .collect(Collectors.toList());
        return customers;
    }

    private CustomerResponseDTO convertToDto(Customer customer) {
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setId(customer.getId());
        dto.setNombre(customer.getNombre());
        
        return dto;
    }
}