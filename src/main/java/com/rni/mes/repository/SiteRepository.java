package com.rni.mes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.rni.mes.models.Site;

public interface SiteRepository extends CrudRepository<Site, Long> {

	List<Site> findAll(Sort sort);

	Optional<Site> findByNomSite(String nomLieu);
	
	
//	@Query(value="select L.nom_lieu,L.region,L.province,L.localite,\r\n"
//			+ "M.longitude, M.latitude,M.date_mesure,M.moyenne_spatiale\r\n"
//			+ "from lieu L\r\n"
//			+ "inner join mesure M\r\n"
//			+ "on L.id=M.lieu_id\r\n"
//			+ "where L.id = ?1",nativeQuery=true)
	@Query(value = "select *\r\n"
			+ "from site S\r\n"
			+ "inner join mesure M on S.id=M.site_id\r\n"
			+ "inner join localisation L on S.localisation_id=L.id\r\n"
			+ "where S.id = ?1",nativeQuery = true)
	List<Object[]> details(Long id);
	
	/*
	 * envoie le nombre de site par ville
	 */
	@Query(value="select count(*) as nbreSite\r\n"
			+ "from site S\r\n"
			+ "inner join localisation L\r\n"
			+ "on S.ville_id_ville=L.id_ville\r\n"
			+ "where L.ville=?1",nativeQuery = true)
	Integer nbreDeSite(String ville);
	
	/*
	 * detail des site(locality, region,province)
	 */

	@Query(value = "select S.id,S.nom_site,L.region,L.province,L.localite \r\n"
			+ "from site S\r\n"
			+ "inner join localisation L on S.localisation_id=L.id\r\n"
			+ "order by L.region,L.province,L.localite,S.nom_site",nativeQuery = true )
	List<Object[]> detailSite();
}
