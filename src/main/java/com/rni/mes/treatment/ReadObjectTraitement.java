package com.rni.mes.treatment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rni.mes.records.ReqDetailMesure;
import com.rni.mes.service.MesureService;
import com.rni.mes.service.SiteService;

public class ReadObjectTraitement {

	List<ReqDetailMesure> detailLieu = new ArrayList<>();

	/*
	 * traitement en fonction de l'identifiant d'un lieu
	 */
	public List<ReqDetailMesure> avecId(SiteService siteService, Long id) {
		List<Object[]> result = siteService.detailsSite(id);
		
		detailLieu = read(result, detailLieu);
		
		return detailLieu;
	}
	
	/*
	 * traitement en fonction de l'annee, la province, la region et la localite
	 */
	
	public List<ReqDetailMesure> traitementParARPL(
			Integer annee,String regionC,
			String provinceC,String localiteC,MesureService mesureService
			){
		List<Object[]> result = mesureService.rechercheARPL(annee, regionC, provinceC, localiteC);
		
		detailLieu = read(result, detailLieu);
		
		return detailLieu;
		
	}
	
	/*
	 * traitement des donnees de mesure en fonction de l'annee
	 */
	public List<ReqDetailMesure> traitementParAnnee(
			Integer annee,MesureService mesureService
			){
		List<Object[]> result = mesureService.rechercheParAnnee(annee);
		detailLieu = read(result, detailLieu);
		return detailLieu;
		
	}
	
	/*
	 * recupere pour le moment toute les donnees
	 */
	public List<ReqDetailMesure> toutLesLieuMesures(MesureService mesureService,Integer annee){
		System.out.println(annee);
		List<Object[]> result = mesureService.rechercheParAnnee(annee);
		detailLieu = read(result, detailLieu);
		return detailLieu;
	}
	
	List<ReqDetailMesure> read(List<Object[]> object,List<ReqDetailMesure> detailLieu) {
		
		object.stream().forEach((record)->{
			Long idSite = (Long)record[0];
			String nomSite = (String) record[1];
			Long idMesure = (Long) record[3];
			Date dateMesure = (Date) record[6];
			double latitude = (double) record[8];
			double longitude = (Double) record[9];
			float moyenneSpatiale = (float) record[10];
			String nomRapport = (String) record[11];
			Long idLocalite = (Long) record[15];
			String localite = (String) record[17];
			String province = (String) record[19];
			String region = (String) record[20];
			
			
			ReqDetailMesure _detaillieu =  new ReqDetailMesure(idSite, nomSite,
					idLocalite, region,province, localite,
					idMesure, longitude, latitude,null,
					dateMesure, moyenneSpatiale, null, null, null, nomRapport);
			
			detailLieu.add(_detaillieu);
		});
		
		return detailLieu;	
	}
}
