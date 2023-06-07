package com.rni.mes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rni.mes.models.Mesure;
import com.rni.mes.repository.MesureRepository;

@Service
public class MesureService {

	private MesureRepository mesureRepository;

	//Constructor
	public MesureService(MesureRepository mesureRepository) {
		super();
		this.mesureRepository = mesureRepository;
	}
	
	public Mesure ajoutMesure(Mesure mesure) {
		return mesureRepository.save(mesure);
	}

	//recherche une mesure a partir de son iddentifiant
	public Optional<Mesure> rechercheParId(Long id){
		return mesureRepository.findById(id);
		
	}
	
	//recherche en fonction de l'annnee, region, province, localite
	public List<Object[]> rechercheARPL(Integer annee, String region, String province,String localite){
		return mesureRepository.parARPL(annee, region, province, localite);
	}
	
	/*
	 * recupere les donnees tournant autour d'une mesure
	 */
	public List<Object[]> rechercheParAnnee(Integer annee){
		return mesureRepository.rechercheParAnnee(annee);
	}
	
	//mise a jour d'un lieu de mesure
	public void updateMesure(Long id, Mesure newDataMesure) {
		
		Mesure mesure = mesureRepository.findById(id).get();
		if(mesure != null ) {
			
			mesure.setLongitude(newDataMesure.getLongitude());
			mesure.setLatitude(newDataMesure.getLatitude());
			mesure.setPrioritaire(newDataMesure.getPrioritaire());
			mesure.setMoyenneSpatiale(newDataMesure.getMoyenneSpatiale());
			System.out.println("service " + mesure.getMoyenneSpatiale() + " valeurController " + newDataMesure.getMoyenneSpatiale());
			//mesure.setDateMesure(newDataMesure.getDateMesure());
			mesure.setLargeBande(newDataMesure.getLargeBande());
			mesure.setBandeEtroite(newDataMesure.getBandeEtroite());
			mesure.setCommentaire(newDataMesure.getCommentaire());	
		}
		mesureRepository.save(mesure);
	}
	
	/*
	 * joint le rapport a la mesure
	 * mise a jour de la mesure
	 */
	public void ajoutRapport(Long id, String nomRapport) {
		Mesure mesure = mesureRepository.findById(id).get();
		if(mesure!=null)
			mesure.setNomRapport(nomRapport);
		mesureRepository.save(mesure);
	}
	
	/*
	 * recuperer les annees des mesures, pour la recherche de l'annee la plus recente
	 */
	public List<Integer> anneesMesure(){
		return mesureRepository.anneesMesure();
	}
	
	/*
	 * recupere le nombre de mesure pour une annee et par ville
	 */
	public Integer nbreMesure(String ville,Integer annee) {
		return mesureRepository.nbreMesure(ville, annee);
	}
}
