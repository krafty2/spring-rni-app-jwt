package com.rni.mes.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rni.mes.records.LieuMesure;
import com.rni.mes.service.MesureService;
import com.rni.mes.service.SiteService;
import com.rni.mes.treatment.ReadObjectTraitement;

@RestController
@RequestMapping("/pub/")
@CrossOrigin(origins = "*")
public class MapController {

	SiteService siteService;
	MesureService mesureService;
	
	public MapController(SiteService siteService, MesureService mesureService) {
		super();
		this.siteService = siteService;
		this.mesureService = mesureService;
	}

	@GetMapping("/mapRni")
	public List<LieuMesure> mapRni(){
		ReadObjectTraitement readObject = new ReadObjectTraitement();	
		List<LieuMesure> liste = readObject.toutLesLieuMesures(siteService);
		return liste;
	}
}
