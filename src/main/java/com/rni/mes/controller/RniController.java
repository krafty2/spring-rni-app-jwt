package com.rni.mes.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rni.mes.models.Site;
import com.rni.mes.records.ReqDetailMesure;
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
		List<Site> sites = siteService.tousLesLieux();
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
}
