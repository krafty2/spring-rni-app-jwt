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
	public Utilisateur creationCompte(@RequestBody RegistrationRequestDTO  registrationRequestDTO){
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
		String emailContent=String.format("Pour activer votre compte clickez sur ce lien : http://localhost:8080/public/emailActivation?token="+activationJwtToken);
        mailservice.sendEmail(utilisateur.getEmail(),"Email verification",emailContent);
		
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
