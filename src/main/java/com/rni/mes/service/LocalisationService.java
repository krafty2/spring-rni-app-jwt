package com.rni.mes.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rni.mes.models.Ville;
import com.rni.mes.repository.VilleRepository;

@Service
public class VilleService {

	VilleRepository villeRepository;
	
	public VilleService(VilleRepository villeRepository) {
		super();
		this.villeRepository = villeRepository;
	}

	//recherche par ville
	public Optional<Ville> trouveVille(String ville){
		return villeRepository.findByVille(ville);
	}
	
	public Ville ajouterVille(Ville ville) {
		return villeRepository.save(ville);
	}
	
	//toute les villes
	public Iterable<Ville> touteLesVilles(){
		return villeRepository.findAll();
	}
}
