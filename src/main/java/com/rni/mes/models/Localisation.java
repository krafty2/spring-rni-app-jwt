package com.rni.mes.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/*
 * changer la ville par Localite
 */
@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class Localisation {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	//ville correspond a la localite dans le fichier excel
	@Column(unique = true)
	private String localite;
	private String region;
	private String province;
	private Double longitudeV;
	private Double latitudeV;
}
