package com.Springboot.backend.entities;



import java.time.Instant;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@GenericGenerator(name = "native",strategy = "native")
	@Column(name="token_id")
	private int id;
	
	@Column(name="refresh_token")
	private String token;
	
	@Column(name="expire_date")
	private Instant expireDate;
	
	
	@OneToOne
	@JoinColumn(name="customer_id", referencedColumnName ="customer_id" )
	@JsonIgnore
	private Customer customer;


	
	

}
