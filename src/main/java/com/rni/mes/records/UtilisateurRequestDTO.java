package com.rni.mes.records;

public record UtilisateurRequestDTO(
		Long idUtilisateur, String username, String prenom,
		String nom, String email,String password,String confirmPassword, String roleName
		) {

}
