package com.rni.mes.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rni.mes.models.Utilisateur;
import com.rni.mes.repository.UtilisateurRepostiroy;

@Service
public class UtilisateurService {
	
	@Autowired
	UtilisateurRepostiroy utilisateurRepostiroy;

	public Utilisateur creerUtilisateur(Utilisateur utilisateur) {
		
		return utilisateurRepostiroy.save(utilisateur);
		
	}
	
	public Optional<Utilisateur> parUsername(String username){
		return utilisateurRepostiroy.findByUsername(username);
	}
	 
	public Optional<Utilisateur> parId(Long id){
		return utilisateurRepostiroy.findById(id);
	}
}
