package com.rni.mes.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.rni.mes.config.JwtTokenParam;

import com.rni.mes.models.Utilisateur;

@Service
public class TokenService {


	private JwtEncoder jwtEncoder;
	

	private UtilisateurService utilisateurService;
	
	private JwtTokenParam jwtTokenParams;

	public TokenService(JwtEncoder jwtEncoder, UtilisateurService utilisateurService) {
		super();
		this.jwtEncoder = jwtEncoder;
		this.utilisateurService = utilisateurService;
		
	}

	public Map<String, String> generateJwtToken(
			String username,
			boolean withRefreshToken
			) 
	{
		
		Utilisateur appUser= utilisateurService.parUsername(username).get();
		String scope=appUser.getRoles().stream().map(r->r.getRoleName()).collect(Collectors.joining(" "));
		
		Map<String, String> idToken = new HashMap<>();
		Instant instant = Instant.now();
		
		JwtClaimsSet jwtClaimsSet=JwtClaimsSet.builder()
				.subject(appUser.getUsername())
                .issuer("auth-service")
                .issuedAt(instant)
                .expiresAt(instant.plus(withRefreshToken?5:30, ChronoUnit.MINUTES))
                .claim("scope",scope)
                .claim("email",appUser.getEmail())
                .claim("nom", appUser.getNom())
                .claim("prenom", appUser.getPrenom())
                .build();
		String accessToken = this.jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
		idToken.put("acces-token",accessToken);
		if(withRefreshToken){
            JwtClaimsSet jwtRefreshTokenClaimsSet=JwtClaimsSet.builder()
            		.subject(appUser.getUsername())
                    .issuer("auth-service")
                    .issuedAt(instant)
                    .expiresAt(instant.plus(10, ChronoUnit.MINUTES))
                    .claim("email", appUser.getEmail())
                    .build();
            String refreshToken = this.jwtEncoder.encode(JwtEncoderParameters.from(jwtRefreshTokenClaimsSet)).getTokenValue();
            idToken.put("refresh-token",refreshToken);
        }
		return idToken;
	}
}
