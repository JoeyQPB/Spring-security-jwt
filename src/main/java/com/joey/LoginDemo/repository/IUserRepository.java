package com.joey.LoginDemo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.joey.LoginDemo.domain.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long>{

	Optional<User> findByEmail(String email);
}
