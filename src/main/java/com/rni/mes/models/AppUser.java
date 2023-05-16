package com.rni.mes.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.rni.mes.enums.AccountStatus;
import com.rni.mes.enums.Genre;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class AppUser {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique=true)
	private String username;
	private String password;
	//
	private String nom;
	private String prenom;
	private String telephone;
	@Enumerated(EnumType.STRING)
	private Genre genre;
	private String email;
	private boolean emailVerifie;
	@Enumerated(EnumType.STRING)
	private AccountStatus status;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<AppRole> roles = new ArrayList<>();
	
	@ElementCollection
    private List<String> requestedRoles=new ArrayList<>();
	
	private String temporaryActivationCode;
    private Instant temporaryActivationCodeTimeStamp;
    
    
}
