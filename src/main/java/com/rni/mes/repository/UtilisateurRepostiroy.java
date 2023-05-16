package com.rni.mes.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.rni.mes.models.Utilisateur;

public interface UtilisateurRepostiroy extends CrudRepository<Utilisateur, Long>{

	Optional<Utilisateur> findByUsername(String username);
	
	Optional<Utilisateur> findById(Long id);

}
