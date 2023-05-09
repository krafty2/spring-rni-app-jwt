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
			+ "from site L\r\n"
			+ "inner join mesure M on L.id_site=M.site_id_site\r\n"
			+ "inner join ville V on L.ville_id_ville=V.id_ville\r\n"
			+ "where L.id_site = ?1",nativeQuery = true)
	List<Object[]> details(Long id);
	
	//recherche tout les lieux et mesure
	@Query(value = "select * from site S\r\n"
			+ "inner join mesure M on S.id_site=M.site_id_site\r\n"
			+ "inner join ville V on S.ville_id_ville=V.id_ville\r\n"
			+ "order by S.nom_site",nativeQuery = true)
	List<Object[]> recherchePlus();


}
