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
import com.rni.mes.models.Localisation;
import com.rni.mes.records.ReqDetailMesure;
import com.rni.mes.service.FichierRniService;
import com.rni.mes.service.MesureService;
import com.rni.mes.service.SiteService;
import com.rni.mes.service.LocalisationService;
import com.rni.mes.treatment.CoordonneesVille;
import com.rni.mes.treatment.ExcelRead;
import com.rni.mes.treatment.ReadObjectTraitement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/fichier/")
@CrossOrigin(origins = "*")
public class FichierController {

	private SiteService siteService;
	private MesureService mesureService;
	private LocalisationService localisationService;
	private FichierRniService fichierRniService;
	
	//Constructor
	public FichierController(SiteService siteService, MesureService mesureService,
								LocalisationService villeService, FichierRniService fichierRniService) {
		super();
		this.siteService = siteService;
		this.mesureService = mesureService;
		this.localisationService = villeService;
		this.fichierRniService = fichierRniService;
	}
	
	//enregistre les donnees extraites d'un fichier excel
	@PostMapping("/import/excel")
	public ResponseEntity<?> exportExcel(@RequestBody MultipartFile file) throws IOException{
	
		if(ExcelRead.checkExcelFormat(file)) {
			List<ReqDetailMesure> liste = new ArrayList<>();
			liste = ExcelRead.convertExcelToMap(file.getInputStream());
			
			FichierRni fichierRni = new FichierRni();
			fichierRni.setNomFichier(file.getOriginalFilename());
			fichierRni.setDateDeTransfert(LocalDate.now());
			
			fichierRniService.ajouterFichier(fichierRni);
			
			for (ReqDetailMesure siteMesure : liste) {
				Site site = new Site();
				Mesure mesure = new Mesure();
				Localisation localisation = new Localisation();
				
				
				//===============
				site.setNomSite(siteMesure.nomSite());
				//===============
				localisation.setLocalite(siteMesure.localite());
				localisation.setRegion(siteMesure.region());
				localisation.setProvince(siteMesure.province());
				//==============
				mesure.setLongitude(siteMesure.longitude());
				mesure.setLatitude(siteMesure.latitude());
				mesure.setPrioritaire(siteMesure.prioritaire());
				mesure.setBandeEtroite(siteMesure.bandeEtroite());
				mesure.setDateMesure(siteMesure.dateMesure());
				mesure.setMoyenneSpatiale(siteMesure.moyenneSpatiale());
				mesure.setCommentaire(siteMesure.commentaire());
				mesure.setLargeBande(siteMesure.largeBande());
				//==============
				
				
				
				//recherche si le nom du site existe deja dans la base de donnee
				Optional<Site> existeSite = siteService.trouveSite(site.getNomSite());
				
				//recherche si la ville existe deja dans la base de donnee
				Optional<Localisation> existeLocalite = localisationService.trouveLocalite(localisation.getLocalite());
				
				if(!existeLocalite.isPresent()) {
					
					CoordonneesVille coordonneesVille = new CoordonneesVille();
					
					Map<String, Double> map = coordonneesVille.map();
					
					try {
						localisation.setLatitudeV(map.get(localisation.getLocalite()+"-Lat"));
						localisation.setLongitudeV(map.get(localisation.getLocalite()+"-Lgn"));
					} catch (Exception e) {
						// TODO: handle exception
						e.getMessage();
					}
					
					
					localisationService.ajouterLocalite(localisation);
					
					site.setLocalisation(localisation);
					
					siteService.ajouterLieu(site);
					
					mesure.setFichierRni(fichierRni);
					mesure.setSite(site);
					mesureService.ajoutMesure(mesure);
				} else if (existeLocalite.isPresent() && !existeSite.isPresent()) {
					Localisation localitePresent = existeLocalite.get();
					site.setLocalisation(localitePresent);
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
