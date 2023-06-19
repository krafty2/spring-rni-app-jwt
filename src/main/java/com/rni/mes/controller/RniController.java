package com.rni.mes.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rni.mes.models.FichierRni;
import com.rni.mes.models.Localisation;
import com.rni.mes.models.Site;
import com.rni.mes.records.ReqDetailMesure;
import com.rni.mes.records.ReqDetailSite;
import com.rni.mes.records.RequestVille;
import com.rni.mes.service.FichierRniService;
import com.rni.mes.service.LocalisationService;
import com.rni.mes.service.MesureService;
import com.rni.mes.service.SiteService;
import com.rni.mes.treatment.ReadObjectTraitement;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/rni/")
@CrossOrigin(origins = "*")
public class RniController {
	
	@Autowired
	SiteService siteService;
	@Autowired
	MesureService mesureService;
	@Autowired
	LocalisationService localisationService;
	@Autowired
	FichierRniService fichierRniService;

	
	/*
	 * affiche les informations en fonction de l'annee, la region, la province, la localite
	 */
	@GetMapping("/searchByRPLA/{annee}/{region}/{province}/{localite}")
	public List<ReqDetailMesure> rechercheAvance2(
			@PathVariable Integer annee, @PathVariable String region,
			@PathVariable String province, @PathVariable String localite
			){
		ReadObjectTraitement readObjectTraitement = new ReadObjectTraitement();
		List<ReqDetailMesure> detailLieu = readObjectTraitement.traitementParARPL(annee, region, province, localite, mesureService);
		return detailLieu;
	}
	
	@GetMapping("/recherche")
	public String test() {
		
		return "hello";
	}
	
	/*
	 * afficher tout les lieux enregistrer
	 */
	@GetMapping("/lieux")
	public List<ReqDetailMesure> toutLesLieux(){
		List<ReqDetailMesure> liste = new ArrayList<>();
		List<Site> sites = siteService.tous_les_lieux();
		for (Site site : sites) {
			Long idSite = site.getId();
			String nomSite = site.getNomSite();
			 
			Long idVille = site.getLocalisation().getId();
			String localite = site.getLocalisation().getLocalite();
			String region = site.getLocalisation().getRegion();
			String province = site.getLocalisation().getProvince();
			ReqDetailMesure detail = new ReqDetailMesure(idSite, nomSite, idVille,
									region, province, localite, null, 0, 0,
									null, null, 0, null, null, null, null);
			liste.add(detail);
		}
		//
		return liste;
	}
	
	/*
	 * rechercher un lieu 
	 */
	@CrossOrigin(origins = "*")
	@GetMapping("/detail-lieu/{id}")
	public List<ReqDetailMesure> detailLieu(@PathVariable Long id){
		//ObjectTraitement objectTraitment = new ObjectTraitement();
		ReadObjectTraitement readObjectTraitement = new ReadObjectTraitement();
		List<ReqDetailMesure> detailLieu = readObjectTraitement.avecId(siteService, id);
		
		return detailLieu;
	}
	
	//---------------------------------------------------------------------------------------
	
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
	
	@GetMapping("/sites")
	public List<ReqDetailSite> sites(){
		ReadObjectTraitement read = new ReadObjectTraitement();
		List<ReqDetailSite> sites = read.reqDetailSite(siteService);
		return sites;
		
	}
	
	@GetMapping("/del-all-mesure-file")
	public Integer delAllMesureFromFile(Long idFile) {
		return mesureService.delAllMesureFromFile(idFile);
	}
	
	@DeleteMapping("/delete-file")
	public Integer deleteFile(Long idFile) {
		Integer nbInteger = mesureService.delAllMesureFromFile(idFile);
		fichierRniService.delete(idFile);
		return  nbInteger;
	}
	
	@GetMapping("/fichiers-rni")
	public Iterable<FichierRni> fichiersRni(){
		return fichierRniService.fichiersRni();
	}
}
