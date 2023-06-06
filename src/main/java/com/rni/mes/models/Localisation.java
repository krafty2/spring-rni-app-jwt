package com.rni.mes.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class Ville {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long idVille;
	//ville correspond a la localite dans le fichier excel
	@Column(unique = true)
	private String ville;
	private String region;
	private String province;
	private Double longitudeV;
	private Double latitudeV;
}
