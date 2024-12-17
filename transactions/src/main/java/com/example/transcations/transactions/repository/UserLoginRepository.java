package com.example.transcations.transactions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.transcations.transactions.model.UserLogin;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin, Integer> {
    @Query("SELECT ul FROM UserLogin ul WHERE ul.id = :id")
    public UserLogin get(@Param(value = "id") Integer id);

}
