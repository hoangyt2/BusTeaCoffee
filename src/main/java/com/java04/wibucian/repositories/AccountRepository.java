package com.java04.wibucian.repositories;

import com.java04.wibucian.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String>, JpaSpecificationExecutor<Account> {
    Optional<Account> findAccountById(String id);

    @Query(value = "INSERT INTO ACCOUNT(idEmployee, password, role) VALUES(:idEmployee, :password, :role)", nativeQuery = true)
    @Modifying
    void saveNative(@Param("idEmployee") String idEmployee, @Param("password") String password, @Param("role") int role);
}