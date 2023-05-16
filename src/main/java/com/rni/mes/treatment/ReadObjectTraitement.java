package com.rni.mes.treatment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rni.mes.records.SiteMesure;
import com.rni.mes.service.MesureService;
import com.rni.mes.service.SiteService;

public class ReadObjectTraitement {

	List<SiteMesure> detailLieu = new ArrayList<>();

	/*
	 * traitement en fonction de l'identifiant d'un lieu
	 */
	public List<SiteMesure> avecId(SiteService siteService, Long id) {
		List<Object[]> result = siteService.detailsSite(id);
		
		detailLieu = read(result, detailLieu);
		
		return detailLieu;
	}
	
	/*
	 * traitement en fonction de l'annee, la province, la region et la localite
	 */
	
	public List<SiteMesure> traitementAPRL(
			Integer annee,String regionC,
			String provinceC,String localiteC,MesureService mesureService
			){
		List<Object[]> result = mesureService.rechercheARPL(annee, regionC, provinceC, localiteC);
		
		detailLieu = read(result, detailLieu);
		
		return detailLieu;
		
	}

	
	/*
	 * recupere pour le moment toute les donnees
	 */
	public List<SiteMesure> toutLesLieuMesures(SiteService lieuService){
		List<Object[]> result = lieuService.lieuEtMesure();
		detailLieu = read(result, detailLieu);
		return detailLieu;
	}
	
	List<SiteMesure> read(List<Object[]> object,List<SiteMesure> detailLieu) {
		
		object.stream().forEach((record)->{
			Long idSite = (Long)record[0];
			String nomSite = (String) record[1];
			Long idMesure = (Long) record[3];
			Date dateMesure = (Date) record[6];
			double latitude = (double) record[8];
			double longitude = (Double) record[9];
			float moyenneSpatiale = (float) record[10];
			String nomRapport = (String) record[11];
			Long idVille = (Long) record[15];
			String province = (String) record[18];
			String region = (String) record[19];
			String ville = (String) record[20];
			
			SiteMesure _detaillieu =  new SiteMesure(idSite, nomSite,
					idVille, region,province, ville,
					idMesure, longitude, latitude,null,
					dateMesure, moyenneSpatiale, null, null, null, nomRapport);
			
			detailLieu.add(_detaillieu);
		});
		
		return detailLieu;	
	}
}
