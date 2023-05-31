package com.rni.mes.treatment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordonneesVille {

	public Map<String, Double> map(){
		Map<String, Double> map = new HashMap<>();
		
		//Bobo-Dioulasso
		map.put("Bobo-Dioulasso-Lat", 11.172797);
		map.put("Bobo-Dioulasso-Lgn", -4.297659);
		
		//Ouagadougou
		map.put("Ouagadougou-Lat", 12.379812);
		map.put("Ouagadougou-Lgn", -1.524086);
		
		//Tenkodogo
		map.put("Tenkodogo-Lat", 11.792362);
		map.put("Tenkodogo-Lgn", -0.371882);
		
		//Koudougou
		map.put("Koudougou-Lat", 12.263711);
		map.put("Koudougou-Lgn", -2.358354);
		
		//Ouahigouya
		map.put("Ouahigouya-Lat", 13.570812);
		map.put("Ouahigouya-Lgn", -2.414827);
		
		//Ziniare
		map.put("Ziniaré-Lat", 12.589948);
		map.put("Ziniaré-Lgn", -1.29904);
		
		
		return map;
	}
}
