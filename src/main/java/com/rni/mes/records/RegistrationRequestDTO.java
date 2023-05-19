package com.rni.mes.records;

public record RegistrationRequestDTO(
		String username, String prenom,String nom, String email,
		String password,String confirmPassword, String roleName,
		String genre
		) {

}
