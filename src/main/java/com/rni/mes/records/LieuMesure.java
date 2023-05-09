package com.rni.mes.records;

import java.util.Date;

public record LieuMesure(Long idSite, String nomSite,
		Long idVile, String region, String province,String ville, 
		Long idMesure,double longitude, double latitude, String prioritaire,
		Date dateMesure, float moyenneSpatiale, String largeBande, String bandeEtroite,
		String commentaire, String nomRapport) {

}
