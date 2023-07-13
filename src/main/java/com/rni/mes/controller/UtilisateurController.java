package com.rni.mes.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rni.mes.enums.AccountStatus;

import com.rni.mes.models.Role;
import com.rni.mes.models.Utilisateur;
import com.rni.mes.models.Localisation;
import com.rni.mes.records.RegistrationRequestDTO;
import com.rni.mes.service.MailService;
import com.rni.mes.service.MesureService;
import com.rni.mes.service.RoleService;
import com.rni.mes.service.TokenService;
import com.rni.mes.service.UtilisateurService;

import jakarta.mail.MessagingException;

import com.rni.mes.service.LocalisationService;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "*")
public class UtilisateurController {
	@Autowired
	UtilisateurService utilisateurService;
	@Autowired
	RoleService roleService;
	@Autowired
	private MailService mailservice;
	@Autowired
	private JwtEncoder jwtEncoder;
	@Autowired
	private JwtDecoder jwtDecoder;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private TokenService tokenService;
	
	@PostMapping("/auth")
    public ResponseEntity<Map<String,String>> requestForToken(
    		
    		@RequestParam String grantType,
            @RequestParam String  username,
            @RequestParam String  password,
            @RequestParam boolean withRefreshToken,
            String refreshToken
    		){
		System.out.println("hello " +  withRefreshToken);
		//----------------------------------------------
		if(grantType==null)
			return new ResponseEntity<>(Map.of("errorMessage", "grantType is required"), HttpStatus.UNAUTHORIZED);
		
        Map<String,String > response;
        if(grantType.equals("password")){
            Authentication authentication=authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,password
                    )
            );
            response=tokenService.generateJwtToken(authentication.getName(),withRefreshToken);
            return ResponseEntity.ok(response);
        } else if(grantType.equals("refreshToken")){
            String refreshTokens=refreshToken;
            if(refreshTokens==null) {
                return new ResponseEntity<>(Map.of("error","RefreshToken Not Present"),HttpStatus.UNAUTHORIZED);
            }
            Jwt decodedJwt = jwtDecoder.decode(refreshTokens);
            String username1=decodedJwt.getSubject();
            Utilisateur appUser=utilisateurService.parUsername(username1).get();
          
            response=tokenService.generateJwtToken(appUser.getUsername(),withRefreshToken);
            return ResponseEntity.ok(response);
        }
        return new ResponseEntity<Map<String, String>>(Map.of("error",String.format("grantType <<%s>> not supported ",grantType)),HttpStatus.UNAUTHORIZED);
    }
	

	@PostMapping("/register")
	public Utilisateur creationCompte(@RequestBody RegistrationRequestDTO  registrationRequestDTO) throws MessagingException{
		System.out.println("hello");
		if(!registrationRequestDTO.password().equals(registrationRequestDTO.confirmPassword()))
            throw new RuntimeException("Passwords not match");
		
		System.out.println(registrationRequestDTO.password());
		Utilisateur utilisateur = new Utilisateur();
		
		if(roleService.trouveParRole(registrationRequestDTO.roleName()).isPresent()) {
			Role roleExist = roleService.trouveParRole(registrationRequestDTO.roleName()).get();
		
			utilisateur.getRoles().add(roleExist);
		} else {
			Role role = Role.builder()
						.roleName(registrationRequestDTO.roleName())
						.build();
			roleService.creerRole(role);
			
			utilisateur.getRoles().add(role);
			
		}
		
		utilisateur.setUsername(registrationRequestDTO.username());
		utilisateur.setNom(registrationRequestDTO.nom());
		utilisateur.setPrenom(registrationRequestDTO.prenom());
		utilisateur.setEmail(registrationRequestDTO.email());
		utilisateur.setGenre(registrationRequestDTO.genre());
		utilisateur.setPassword(passwordEncoder.encode(registrationRequestDTO.password()));
		
		utilisateurService.creerUtilisateur(utilisateur);
		
		Instant instant = Instant.now();
		JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(utilisateur.getUsername())
                .issuedAt(instant)
                .expiresAt(instant.plus(5, ChronoUnit.MINUTES))
                .issuer("")
                .claim("email",utilisateur.getEmail())
                .build();
		String activationJwtToken=jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
		String htmlContent = "<html><body><p>Contenu du message</p> <a href=\"http://localhost:8080/public/emailActivation?token="+activationJwtToken +" \">valider</a></body></html>";
		String emailContent=String.format("Pour activer votre compte clickez sur ce lien : http://localhost:8080/public/emailActivation?token="+activationJwtToken);
		String emailContentHtml = "<html lang=\"en\">\r\n"
				+ "\r\n"
				+ "<body>\r\n"
				+ "    <section style=\"width: 700px;margin: auto;padding: 14px; font-family: sans-serif;text-align: center; background: #264653; box-shadow: 1px 2px 6px rgb(241, 238, 238);border-radius: 4px;\">\r\n"
				+ "\r\n"
				+ "        \r\n"
				+ "            <div style=\"margin: 0 0 26px 0; border-bottom: 2px solid white; padding: 10px;\">\r\n"
				+ "                <h1 style=\"color: #00bbf9;font-size: 30px;font-weight: 600;padding: 4px;margin: 0;\">\r\n"
				+ "                    IRT CONSULTING\r\n"
				+ "                </h1>\r\n"
				+ "                <small style=\"color: #00bbf9;\">Intégrateur de solution</small>\r\n"
				+ "            </div>\r\n"
				+ "\r\n"
				+ "            <div style=\"margin: 8px 0 16px 0; border-bottom: 2px solid white; padding: 4px;\">\r\n"
				+ "                <svg fill=\"white\" height=\"38\" viewBox=\"0 -960 960 960\" width=\"38\">\r\n"
				+ "                    <path\r\n"
				+ "                        d=\"M140-160q-24 0-42-18t-18-42v-520q0-24 18-42t42-18h680q24 0 42 18t18 42v520q0 24-18 42t-42 18H140Zm340-302L140-685v465h680v-465L480-462Zm0-60 336-218H145l335 218ZM140-685v-55 520-465Z\" />\r\n"
				+ "                </svg>\r\n"
				+ "                <p style=\"font-size: 22px;margin: 0 0 24px 0;color: white;\">Verifier l'adresse courriel</p>\r\n"
				+ "            </div>\r\n"
				+ "            <div style=\"margin: 18px 0;background: #F0F8FF;padding: 14px;border-radius: 4px;\">\r\n"
				+ "                <p style=\"font-size: 14px;font-weight: normal;color: black;\">\r\n"
				+ "                    Vous avez demandé à vous inscrire à IRT-CONSULTING avec cette adresse de courriel.\r\n"
				+ "                    Une fois que vous aurez confirmé cette adresse, votre demande d'inscription sera validé.\r\n"
				+ "                    Vous ne pourrez pas vous connecter d’ici-là. Si votre demande est refusée,\r\n"
				+ "                    vos données seront supprimées du serveur, aucune action supplémentaire de votre part n’est donc\r\n"
				+ "                    requise.\r\n"
				+ "                    Si vous n’êtes pas à l’origine de cette demande, veuillez ignorer ce message.\r\n"
				+ "                </p>\r\n"
				+ "            </div>\r\n"
				+ "            <div style=\"margin: 10px 0;border-bottom: 2px solid white; padding: 24px;\">\r\n"
				+ "                <a style=\"background: #00bbf9;padding: 8px;text-decoration: none; color: white;border-radius: 4px; box-shadow: 2px 3px 4px rgb(32, 31, 31);\" href=\"http://localhost:8080/public/emailActivation?token="+activationJwtToken+"\">Vérifier l'adresse courriel</a>\r\n"
				+ "            </div>\r\n"
				+ "       \r\n"
				+ "    </section>\r\n"
				+ "</body>\r\n"
				+ "\r\n"
				+ "</html>";
        mailservice.sendEmail(utilisateur.getEmail(),"Email verification",emailContentHtml);
		
    	return utilisateur;
    }
	
	@GetMapping("/emailActivation")
	private String emailActivation(String token) {
		
		try {
            Jwt decode = jwtDecoder.decode(token);
            
            String subject = decode.getSubject();
            Utilisateur utilisateur=utilisateurService.parUsername(subject).get();
            utilisateur.setEmailVerifie(true);
            utilisateur.setStatus(AccountStatus.ACTIVATED);
            utilisateurService.creerUtilisateur(utilisateur);
            return "Email verification success";
        } catch (JwtException e){
            e.printStackTrace();
            return e.getMessage();
        }
	}
	
	@GetMapping("/existeU")
	public boolean existeUtillisateur(String username) {
		return utilisateurService.parUsername(username).isPresent();
	}
	
	@GetMapping("/rechercheR")
	public Role rechercheR() {
		return roleService.trouveParRole("INVITE").get();
	}
}
