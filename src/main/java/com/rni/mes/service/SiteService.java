package com.rni.mes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rni.mes.models.Site;
import com.rni.mes.repository.SiteRepository;

@Service
public class SiteService {

	@Autowired
	private SiteRepository siteRepository;

	/*
	 * enregistre d'un lieu
	 */
	public Site ajouterLieu(Site lieu) {
		return siteRepository.save(lieu);
	}
	
	/*
	 * affiche tout les lieux enregistrer
	 */
	
	public List<Site> tous_les_lieux(){
		Sort sort = Sort.by("nomSite").ascending();
		return siteRepository.findAll(sort);
	}
	
	/*
	 * detail des sites
	 */
	
	public List<Object[]> details_Site(){
		return siteRepository.detailSite();
	}
	
	/*
	 * recherche d'un lieu par son identifiant
	 */
	public Optional<Site> trouverParId(Long id){
		return siteRepository.findById(id);
	}
	
	/*
	 * recherche d'un lieu par son nom
	 */
	
	public Optional<Site> trouveSite(String nomLieu){
		return siteRepository.findByNomSite(nomLieu);
	}
	
	/*
	 * recherche les details d'un lieu a partir de son identifiant
	 */
	public List<Object[]> detailsSite(Long id) {
		return siteRepository.details(id);
	}
	
	/*
	 * mise a jour d'un lieu de mesure
	 */
	public void modifierLieu(Long id, Site majSite) {
		Site site = siteRepository.findById(id).get();
		if(site != null) {
			site.setNomSite(majSite.getNomSite());
	
			siteRepository.save(site);
		}
	}
	
	/*
	 * recherche du nombre de site par ville
	 */
	public Integer nbrDeSite(String ville) {
		return siteRepository.nbreDeSite(ville);
	}
}
