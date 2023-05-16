package com.rni.mes.records;

import com.rni.mes.enums.Genre;

public record RegistrationRequestDTO(
		String username, String firstName,String lastName,
		String email, String password, String confirmPassword,
		Genre gender
		) {

}
