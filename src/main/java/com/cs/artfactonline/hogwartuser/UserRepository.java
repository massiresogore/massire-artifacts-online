package com.cs.artfactonline.hogwartuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<HogwartUser, Integer> {
    Optional<HogwartUser> findByUsername(String username);
}
