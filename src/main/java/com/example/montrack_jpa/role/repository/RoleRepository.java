package com.example.montrack_jpa.role.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.montrack_jpa.role.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
  Role findByName(String name);
}
