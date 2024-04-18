package com.Springboot.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Springboot.backend.entities.Customer;
import com.Springboot.backend.entities.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer>{

	RefreshToken findByToken(String token);

	RefreshToken findByCustomer(Customer byEmail);



}
