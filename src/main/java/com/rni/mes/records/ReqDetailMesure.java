package com.rni.mes.records;

import java.util.Date;

public record ReqDetailMesure(Long idSite, String nomSite,
		Long idLocalite, String region, String province,String localite, 
		Long idMesure,double longitude, double latitude, String prioritaire,
		Date dateMesure, float moyenneSpatiale, String largeBande, String bandeEtroite,
		String commentaire, String nomRapport) {

}
