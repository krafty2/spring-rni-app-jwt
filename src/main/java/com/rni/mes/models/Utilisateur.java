package com.rni.mes.models;

import java.util.List;
import java.util.ArrayList;

import com.rni.mes.enums.AccountStatus;
import com.rni.mes.enums.Genre;

import jakarta.persistence.Column;
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

@Entity @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Utilisateur {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true)
	private String username;
	private String nom;
	private String prenom;
	private String password;
	@Enumerated(EnumType.STRING)
	private Genre genre;
	private String email;
	private boolean emailVerifie;
	@Enumerated(EnumType.STRING)
	private AccountStatus status;
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Role> roles = new ArrayList<>();
}
