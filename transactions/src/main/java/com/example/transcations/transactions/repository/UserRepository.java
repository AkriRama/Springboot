package com.example.transcations.transactions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.transcations.transactions.model.User;
import com.example.transcations.transactions.model.dto.UserDTO;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u")
    public List<User> get();

    @Query("SELECT u FROM User u WHERE u.id = :userId")
    public User get(@Param(value = "userId") Integer userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id = ?1")
    public Integer delete(Integer userId);

    // @Query("INSERT INTO User u VALUES (?1 ?2 ?3 ?4)")
    // public User insert();

    // @Query("SELECT new
    // com.example.transcations.transactions.model.dto.UserDTO(u.id, u.name, r.name)
    // FROM User u JOIN u.role r WHERE u.id = :userId")
    // public UserDTO getUsingDTO(@Param(value = "userId") Integer userId);

    @Query("SELECT new com.example.transcations.transactions.model.dto.UserDTO(u.id, u.fullname, u.nickname, u.email, r.name, ul.password) FROM UserLogin ul JOIN ul.user u JOIN ul.role r WHERE u.email = :email")
    public UserDTO getUsingDTO(@Param(value = "email") String email);
}
