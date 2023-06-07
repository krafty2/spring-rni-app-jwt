package com.rni.mes.models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class FichierRni {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String nomFichier;
	//represente la date de derniere modification du fichier recuperer directement sur ce dernier
	private LocalDate dateDerniereModification;
	private LocalDate dateDeTransfert;
}
