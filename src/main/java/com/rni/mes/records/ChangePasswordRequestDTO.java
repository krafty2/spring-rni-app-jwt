package com.rni.mes.records;

public record ChangePasswordRequestDTO(
		String currentPassword,
        String newPassword,
        String confirmPassword) {

}
