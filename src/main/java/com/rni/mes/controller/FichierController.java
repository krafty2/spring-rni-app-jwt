package com.rni.mes.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rni.mes.models.FichierRni;
import com.rni.mes.models.Mesure;
import com.rni.mes.models.Site;
import com.rni.mes.models.Ville;
import com.rni.mes.records.LieuMesure;
import com.rni.mes.service.FichierRniService;
import com.rni.mes.service.MesureService;
import com.rni.mes.service.SiteService;
import com.rni.mes.service.VilleService;
import com.rni.mes.treatment.ExcelRead;
import com.rni.mes.treatment.ReadObjectTraitement;

@RestController
@RequestMapping("/fichier/")
@CrossOrigin(origins = "*")
public class FichierController {

	private SiteService siteService;
	private MesureService mesureService;
	private VilleService villeService;
	private FichierRniService fichierRniService;
	
	//Constructor
	public FichierController(SiteService siteService, MesureService mesureService,
								VilleService villeService, FichierRniService fichierRniService) {
		super();
		this.siteService = siteService;
		this.mesureService = mesureService;
		this.villeService = villeService;
		this.fichierRniService = fichierRniService;
	}
	
	//enregistre les donnees extraites d'un fichier excel
	
	@PostMapping("/import/excel")
	public ResponseEntity<?> exportExcel(@RequestBody MultipartFile file) throws IOException{
	
		if(ExcelRead.checkExcelFormat(file)) {
			List<LieuMesure> liste = new ArrayList<>();
			liste = ExcelRead.convertExcelToMap(file.getInputStream());
			
			FichierRni fichierRni = new FichierRni();
			fichierRni.setNomFichier(file.getOriginalFilename());
			fichierRni.setDateDeTransfert(LocalDate.now());
			
			fichierRniService.ajouterFichier(fichierRni);
			
			for (LieuMesure lieuMesure : liste) {
				Site site = new Site();
				Mesure mesure = new Mesure();
				Ville ville = new Ville();
				
				
				//===============
				site.setNomSite(lieuMesure.nomSite());
				//===============
				ville.setVille(lieuMesure.ville());
				ville.setRegion(lieuMesure.region());
				ville.setProvince(lieuMesure.province());
				//==============
				mesure.setLongitude(lieuMesure.longitude());
				mesure.setLatitude(lieuMesure.latitude());
				mesure.setPrioritaire(lieuMesure.prioritaire());
				mesure.setBandeEtroite(lieuMesure.bandeEtroite());
				mesure.setDateMesure(lieuMesure.dateMesure());
				mesure.setMoyenneSpatiale(lieuMesure.moyenneSpatiale());
				mesure.setCommentaire(lieuMesure.commentaire());
				mesure.setLargeBande(lieuMesure.largeBande());
				//==============
				
				
				
				//recherche si le nom du site existe deja dans la base de donnee
				Optional<Site> existeSite = siteService.trouveSite(lieuMesure.nomSite());
				
				//recherche si la ville existe deja dans la base de donnee
				Optional<Ville> existeVille = villeService.trouveVille(lieuMesure.ville());
				
				if(!existeVille.isPresent()) {
					villeService.ajouterVille(ville);
					site.setVille(ville);
					siteService.ajouterLieu(site);
					
					mesure.setFichierRni(fichierRni);
					mesure.setSite(site);
					mesureService.ajoutMesure(mesure);
				} else if (existeVille.isPresent() && !existeSite.isPresent()) {
					Ville villePresent = existeVille.get();
					site.setVille(villePresent);
					siteService.ajouterLieu(site);
					mesure.setFichierRni(fichierRni);
					mesure.setSite(site);
					mesureService.ajoutMesure(mesure);
				} else if(existeSite.isPresent()) {
					
					Site sitePresent = existeSite.get();
					mesure.setFichierRni(fichierRni);
					mesure.setSite(sitePresent);
					mesureService.ajoutMesure(mesure);
				}
			}
			
			return ResponseEntity.ok(Map.of("message", "File is uploaded and data is saved to db "));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload Excel file");
	}
	
	/*
	 * upload de fichier
	 */
	
	@PostMapping("/transfertPdf")
	public String fileUpload(@RequestParam("file") MultipartFile file,@RequestParam("id") String id) {
		String uploadDir = "/test";
		System.out.println(id + "ok cool");
		File directoryFile = new File(uploadDir);
		
		if(!directoryFile.exists()) {
			directoryFile.mkdirs();
		}
		
		File newFile = new File(directoryFile.getAbsoluteFile()+"/"+file.getOriginalFilename());
		try {
			file.transferTo(newFile);
			Long idMe = Long.parseLong(id);
			mesureService.ajoutRapport(idMe, file.getOriginalFilename());
			return "succes";
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "echec";
	}
	
	
	//telechargement du rapport
	@GetMapping("/telechargeRapport/{id}")
	public ResponseEntity<Resource> telechargementRapport(@PathVariable Long id) throws IOException {
		
		Mesure mesure = new Mesure();
		mesure = mesureService.rechercheParId(id).get();
		
		String fileName = mesure.getNomRapport();
		
		String uploadDir = "/test";
        Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists()) {
            throw new RuntimeException("File not found: " + fileName);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
