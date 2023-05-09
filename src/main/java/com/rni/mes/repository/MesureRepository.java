package com.rni.mes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rni.mes.models.Mesure;

@Repository
public interface MesureRepository extends CrudRepository<Mesure, Long> {

	//recherche en fonction de l'annee, la region, la province , et la localite
		@Query(value = "select * from site S\r\n"
				+ "inner join mesure M on S.id_site=M.site_id_site\r\n"
				+ "inner join ville V on S.ville_id_ville=V.id_ville\r\n"
				+ "where extract(year from M.date_mesure)=?1\r\n"
				+ "and V.region = ?2 and V.province= ?3 and V.ville = ?4\r\n"
				+ "order by S.nom_site",nativeQuery = true)
		List<Object[]> parARPL(Integer annee, String region, String province, String localite);
}
