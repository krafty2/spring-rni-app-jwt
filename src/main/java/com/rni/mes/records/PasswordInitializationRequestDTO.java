package com.rni.mes.records;

public record PasswordInitializationRequestDTO(
		 String password, String confirmPassword,
	        String authorizationCode, String email
		) {

}
