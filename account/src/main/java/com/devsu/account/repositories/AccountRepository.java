package com.devsu.account.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

import com.devsu.account.entities.Account;

public interface AccountRepository extends CrudRepository<Account, Long>{
    Optional<Account> findById(Long accountId);

}
