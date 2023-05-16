package com.rni.mes.records;

public record AuthRequestDTO(
		String grantType, String username, String password,
		boolean withRefreshToken, String refreshToken
		) {

}
