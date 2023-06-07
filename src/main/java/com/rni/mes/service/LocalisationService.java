package com.rni.mes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rni.mes.models.Localisation;
import com.rni.mes.repository.LocalisationRepository;

@Service
public class LocalisationService {

	@Autowired
	LocalisationRepository localisationRepository;

	//recherche par ville
	public Optional<Localisation> trouveLocalite(String localite){
		return localisationRepository.findByLocalite(localite);
	}
	
	public Localisation ajouterLocalite(Localisation localisation) {
		return localisationRepository.save(localisation);
	}
	
	//toute les villes
	public Iterable<Localisation> touteLesLocalites(){
		Sort sort = Sort.by("localite").ascending();
		return localisationRepository.findAll(sort);
	}
	
	/*
	 * toute les regions
	 */
	public List<String> lesRegions(){
		return localisationRepository.lesRegions();
	}
	
	/*
	 * toute les provinces par region
	 */
	public List<String> provinceParReg(String region){
		return localisationRepository.provinceParReg(region);
	}
	
	/*
	 * toute les localite par province
	 */
	public List<String> localiteParPro(String province){
		return localisationRepository.localiteParPro(province);
	}
}
