package com.example.transcations.transactions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.transcations.transactions.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    // @Query(value = "SELECT COUNT(*) FROM tb_role", nativeQuery = true)
    // public long count();

    // @Query(value = "SELECT id, name FROM tb_role", nativeQuery = true)
    // public List<Role> get();

    @Query(value = "SELECT * FROM tb_roles  WHERE level = 1", nativeQuery = true)
    public Role getLevel1();
}
