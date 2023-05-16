package com.rni.mes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rni.mes.models.AppUser;
import com.rni.mes.records.RegistrationRequestDTO;
import com.rni.mes.records.SiteMesure;
import com.rni.mes.service.AccountService;
import com.rni.mes.service.MesureService;
import com.rni.mes.service.SiteService;
import com.rni.mes.treatment.ReadObjectTraitement;

@RestController
@RequestMapping("/pub/")
@CrossOrigin(origins = "*")
public class MapController {

	@Autowired
	AccountService accountService;
	SiteService siteService;
	MesureService mesureService;
	
	public MapController(SiteService siteService, MesureService mesureService) {
		super();
		this.siteService = siteService;
		this.mesureService = mesureService;
	}

	@GetMapping("/mapRni")
	public List<SiteMesure> mapRni(){
		ReadObjectTraitement readObject = new ReadObjectTraitement();	
		List<SiteMesure> liste = readObject.toutLesLieuMesures(siteService);
		return liste;
	}
	
	@PostMapping("/register")
	public AppUser creationCompte(@RequestParam String username,@RequestParam String nom,
			@RequestParam String prenom,@RequestParam String password, @RequestParam String email,
			@RequestParam String confimPassword, @RequestParam boolean activate
			
			){
		AppUser appUser = new AppUser();
		appUser.setUsername(username);
		appUser.setNom(nom);
//        return  this.accountService.register(username, password, confimPassword,
//        		prenom, nom, email, activate);
		return this.accountService.newUser(appUser);
    }
}
