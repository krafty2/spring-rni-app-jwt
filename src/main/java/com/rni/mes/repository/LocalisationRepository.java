package com.rni.mes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rni.mes.models.Localisation;

@Repository
public interface LocalisationRepository extends CrudRepository<Localisation, Long> {
	Optional<Localisation> findByLocalite(String localite);
	Iterable<Localisation> findAll(Sort sort);
	/*
	 * retourne toute les regions
	 */
	@Query(value = "select distinct region from localisation",nativeQuery = true)
	public List<String> lesRegions();
	
	/*
	 * les provinces par regions choisit
	 */
	@Query(value = "select distinct province from localisation\r\n"
			+ "where region=?1",nativeQuery = true)
	public List<String> provinceParReg(String region);
	
	/*
	 * les localites par province choisit
	 */
	@Query(value = "select distinct localite from localisation\r\n"
			+ "where province=?1",nativeQuery = true)
	public List<String> localiteParPro(String province);
}
