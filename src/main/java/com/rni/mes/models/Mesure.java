package com.rni.mes.models;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class Mesure {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long idMesure;
	private Double longitude;
	private Double latitude;
	private String prioritaire;
	private Date dateMesure;
	@Column(name = "moyenne_spatiale")
	private Float moyenneSpatiale;
	@Column(name = "large_bande")
	private String largeBande;
	@Column(name = "bande_etroite")
	private String bandeEtroite;
	private String commentaire;
	private String nomRapport;
	@ManyToOne(cascade = {CascadeType.ALL})
	private Site site;
	@ManyToOne(cascade = {CascadeType.ALL})
	private FichierRni fichierRni;
}
