package com.rni.mes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rni.mes.models.Site;
import com.rni.mes.repository.SiteRepository;

@Service
public class SiteService {

	private SiteRepository siteRepository;

	public SiteService(SiteRepository siteRepository) {
		super();
		this.siteRepository = siteRepository;
	}

	/*
	 * enregistre d'un lieu
	 */
	public Site ajouterLieu(Site lieu) {
		return siteRepository.save(lieu);
	}
	
	/*
	 * affiche tout les lieux enregistrer
	 */
	
	public List<Site> tousLesLieux(){
		Sort sort = Sort.by("nomSite").ascending();
		return siteRepository.findAll(sort);
	}
	
	/*
	 * recherche d'un lieu par son identifiant
	 */
	public Optional<Site> trouverParId(Long id){
		return siteRepository.findById(id);
	}
	
	//recherche tout les lieux ainsi que les mesures
		public List<Object[]> lieuEtMesure(){
			return siteRepository.recherchePlus();
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
}
