package com.rni.mes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rni.mes.models.Mesure;

@Repository
public interface MesureRepository extends CrudRepository<Mesure, Long> {

	/*
	 * recherche en fonction de l'annee, la region, la province , et la localite
	 */
	@Query(value = "select * from site S\r\n"
			+ "inner join mesure M on S.id=M.site_id\r\n"
			+ "inner join localisation L on S.localisation_id=L.id\r\n"
			+ "where extract(year from M.date_mesure)=?1\r\n"
			+ "and L.region = ?2 and L.province= ?3 and L.localite = ?4\r\n"
			+ "order by L.localite,L.region,L.province,S.nom_site",nativeQuery = true)
	List<Object[]> parARPL(Integer annee, String region, String province, String localite);
		
	/*
	 * recherche en fonction de l'annee
	 */
	@Query(value = "select * from site S\r\n"
			+ "inner join mesure M on S.id=M.site_id\r\n"
			+ "inner join localisation L on S.localisation_id=L.id\r\n"
			+ "where extract(year from M.date_mesure)=?1\r\n"
			+ "order by L.localite,L.region,L.province,S.nom_site",nativeQuery = true)
	List<Object[]> rechercheParAnnee(Integer annee);
		
	/*
	 * permet d'extraire distintement les differentes annees de mesure des sites
	 */
	@Query(value = "Select distinct extract(year from date_mesure)\r\n"
			+ "from mesure order by extract(year from date_mesure) desc",nativeQuery = true)
	List<Integer> anneesMesure();
	
	/*
	 * compte le nombre de mesure par ville et prend l'annee la plus recente
	 */
	@Query(value = "select count(*) as nbreMesure\r\n"
			+ "from mesure M\r\n"
			+ "inner join site S on M.site_id=S.id\r\n"
			+ "inner join localisation L on S.localisation_id=L.id\r\n"
			+ "where L.localite=?1 \r\n"
			+ "and extract(year from M.date_mesure)=?2",nativeQuery = true)
	Integer nbreMesure( String localite ,Integer Annee);
}
