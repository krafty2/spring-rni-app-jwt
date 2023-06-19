package com.rni.mes.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rni.mes.models.FichierRni;
import com.rni.mes.repository.FichierRniRepository;

@Service
public class FichierRniService {

	FichierRniRepository fichierRniRepository;

	public FichierRniService(FichierRniRepository fichierRniRepository) {
		super();
		this.fichierRniRepository = fichierRniRepository;
	}
	
	public FichierRni ajouterFichier(FichierRni fichierRni) {
		return fichierRniRepository.save(fichierRni);
	}
	
	public Iterable<FichierRni> fichiersRni(){
		return fichierRniRepository.findAll();
	}
	
	public void delete(Long id) {
		fichierRniRepository.deleteById(id);	
	}
}
