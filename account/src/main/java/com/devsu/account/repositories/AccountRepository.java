package com.devsu.account.repositories;

import org.springframework.data.repository.CrudRepository;

import com.devsu.account.entities.Account;

public interface AccountRepository extends CrudRepository<Account, Long>{

}
