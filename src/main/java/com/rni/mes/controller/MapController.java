package com.rni.mes.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.harmony.pack200.NewAttributeBands.Integral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rni.mes.models.FichierRni;
import com.rni.mes.models.Localisation;
import com.rni.mes.records.RequestVille;
import com.rni.mes.records.ReqDetailMesure;
import com.rni.mes.records.ReqDetailSite;
import com.rni.mes.service.MesureService;
import com.rni.mes.service.SiteService;
import com.rni.mes.service.FichierRniService;
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
	@Autowired
	FichierRniService fichierRniService;

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
	
	
}
