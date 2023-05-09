package com.rni.mes.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rni.mes.models.Ville;

@Repository
public interface VilleRepository extends CrudRepository<Ville, Long> {

	Optional<Ville> findByVille(String ville);

}
