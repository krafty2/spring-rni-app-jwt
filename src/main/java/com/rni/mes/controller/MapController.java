package com.rni.mes.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rni.mes.models.Localisation;
import com.rni.mes.records.RequestVille;
import com.rni.mes.records.ReqDetailMesure;

import com.rni.mes.service.MesureService;
import com.rni.mes.service.SiteService;
import com.rni.mes.service.LocalisationService;
import com.rni.mes.treatment.ReadObjectTraitement;

@RestController
@RequestMapping("/public/")
@CrossOrigin(origins = "*")
public class MapController {

	@Autowired
	SiteService siteService;
	@Autowired
	MesureService mesureService;
	@Autowired
	LocalisationService localisationService;

	/*
	 * recupere toute les mesures les plus recents
	 *
	 */
	@GetMapping("/mapRni")
	public List<ReqDetailMesure> mapRni(){
		ReadObjectTraitement readObject = new ReadObjectTraitement();	
		List<ReqDetailMesure> liste = readObject
										.toutLesLieuMesures(mesureService,mesureService.anneesMesure().get(0));
		return liste;
	}
	
	@GetMapping("/villes")
	public Iterable<Localisation> ville(){
//		System.out.println(mesureService.anneesMesure().get(0));
		return localisationService.touteLesLocalites();
	}
	/*
	 * va passer en privee, temporaire pour test tout ce qui suit en bas
	 */
	
	@GetMapping("/requestVille")
	public List<RequestVille> reqVilles(){
		Iterable<Localisation> villes = localisationService.touteLesLocalites();
		List<RequestVille> result = new ArrayList<>();
		Integer annee = mesureService.anneesMesure().get(0);
		villes.forEach((v)->{
			Integer nbrMesure = mesureService.nbreMesure(v.getLocalite(), annee);
			Integer nbrSite = siteService.nbrDeSite(v.getLocalite());
			
			RequestVille requestVille = new RequestVille(v.getId(), v.getLocalite(), v.getRegion(),
					v.getProvince(), v.getLongitudeV(), v.getLatitudeV(),
					nbrSite, nbrMesure);
			result.add(requestVille);
		});
		return result;
	}
	
	/*
	 * affiche toute les informations relatives a une mesure, le site proche et la ville
	 */
	@GetMapping("/details-mesure")
	public List<ReqDetailMesure> detailsMesure(Integer annee){
		System.out.println(annee);
		ReadObjectTraitement readObject = new ReadObjectTraitement();	
		List<ReqDetailMesure> liste = readObject
										.traitementParAnnee(annee,mesureService);
		return liste;
	}
	
	@GetMapping("/annees-mesure")
	public List<Integer> lesAnneesDeMesures(){
		return mesureService.anneesMesure();
	}
	
	/*
	 * affiche toute les regions enregistres
	 */
	@GetMapping("/regions")
	public List<String> lesRegions(){
		return localisationService.lesRegions();
	}
	
	@GetMapping("/provinces")
	public List<String> provinces(String region){
		return localisationService.provinceParReg(region);
	}
	
	@GetMapping("/localites")
	public List<String> localites(String province){
		return localisationService.localiteParPro(province);
	}
}
