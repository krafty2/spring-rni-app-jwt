package com.rni.mes.records;

public record RequestVille(Long idVille,String Ville,String region,
		String province, Double lgnVille,Double latVille,
		Integer nbrSite, Integer nbrMesure
		) {

}
