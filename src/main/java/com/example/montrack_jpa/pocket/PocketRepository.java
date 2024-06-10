package com.example.montrack_jpa.pocket;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PocketRepository extends CrudRepository<Pocket, Integer> {} 
