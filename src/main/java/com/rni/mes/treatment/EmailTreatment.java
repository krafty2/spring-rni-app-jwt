package com.rni.mes.treatment;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import com.rni.mes.config.JwtTokenParam;
import com.rni.mes.enums.AccountStatus;
import com.rni.mes.models.AppUser;
import com.rni.mes.models.Utilisateur;
import com.rni.mes.service.MailService;
import com.rni.mes.service.UtilisateurService;

public class EmailTreatment {

	@Autowired
	private MailService mailService;
	@Autowired
	private JwtEncoder jwtEncoder;
	@Autowired
	private JwtDecoder jwtDecoder;
	@Autowired
	UtilisateurService utilisateurService;
	
	private JwtTokenParam jwtTokenParams;
	
	public void verificationEmail(Utilisateur utilisateur) {
	
			Instant instant = Instant.now();
			JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
	                .subject(utilisateur.getUsername())
	                .issuedAt(instant)
	                .expiresAt(instant.plus(5, ChronoUnit.MINUTES))
	                .issuer("")
	                .claim("email",utilisateur.getEmail())
	                .build();
			String activationJwtToken=jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
			String emailContent=String.format("To activate yous account click this link : http://localhost:8888/auth-service/public/emailActivation?token=");
	        mailService.sendEmail(utilisateur.getEmail(),"Email verification",emailContent);

		
	}
	
	public String emailActivation(String token) {
        try {
            Jwt decode = jwtDecoder.decode(token);
            String subject = decode.getSubject();
            Utilisateur utilisateur=utilisateurService.parId(Long.parseLong(subject)).get();
            utilisateur.setEmailVerifie(true);
            utilisateur.setStatus(AccountStatus.ACTIVATED);
            utilisateurService.creerUtilisateur(utilisateur);
            return "Email verification success";
        } catch (JwtException e){
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
