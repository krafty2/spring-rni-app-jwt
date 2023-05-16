package com.rni.mes.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rni.mes.models.Site;
import com.rni.mes.records.SiteMesure;
import com.rni.mes.service.MesureService;
import com.rni.mes.service.SiteService;
import com.rni.mes.treatment.ReadObjectTraitement;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/rni/")
@CrossOrigin(origins = "*")
public class RniController {
	
	SiteService siteService;
	MesureService mesureService;

	public RniController(SiteService siteService, MesureService mesureService) {
		super();
		this.siteService = siteService;
		this.mesureService = mesureService;
	}
	
	/*
	 * affiche les informations en fonction de l'annee, la region, la province, la localite
	 */
	@CrossOrigin("*")
	@GetMapping("/searchByRPLA/{annee}/{region}/{province}/{localite}")
	public List<SiteMesure> rechercheAvance2(
			@PathVariable Integer annee, @PathVariable String region,
			@PathVariable String province, @PathVariable String localite
			){
		ReadObjectTraitement readObjectTraitement = new ReadObjectTraitement();
		List<SiteMesure> detailLieu = readObjectTraitement.traitementAPRL(annee, region, province, localite, mesureService);
		return detailLieu;
	}
	
	/*
	 * afficher tout les lieux enregistrer
	 */
	@CrossOrigin(origins = "*")
	@GetMapping("/lieux")
	public List<SiteMesure> toutLesLieux(){
		List<SiteMesure> liste = new ArrayList<>();
		List<Site> sites = siteService.tousLesLieux();
		for (Site site : sites) {
			Long idSite = site.getIdSite();
			String nomSite = site.getNomSite();
			 
			Long idVille = site.getVille().getIdVille();
			String ville = site.getVille().getVille();
			String region = site.getVille().getRegion();
			String province = site.getVille().getProvince();
			SiteMesure villeSite = new SiteMesure(idSite, nomSite, idVille,
									region, province, ville, null, 0, 0,
									null, null, 0, null, null, null, null);
			liste.add(villeSite);
		}
		//
		return liste;
	}
	
	/*
	 * rechercher un lieu 
	 */
	@CrossOrigin(origins = "*")
	@GetMapping("/detail-lieu/{id}")
	public List<SiteMesure> detailLieu(@PathVariable Long id){
		//ObjectTraitement objectTraitment = new ObjectTraitement();
		ReadObjectTraitement readObjectTraitement = new ReadObjectTraitement();
		List<SiteMesure> detailLieu = readObjectTraitement.avecId(siteService, id);
		
		return detailLieu;
	}
}
