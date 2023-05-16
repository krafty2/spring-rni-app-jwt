package com.rni.mes.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rni.mes.exception.EmailNotFoundException;
import com.rni.mes.models.AppUser;
import com.rni.mes.models.Utilisateur;
import com.rni.mes.records.AuthRequestDTO;
import com.rni.mes.records.PasswordInitializationRequestDTO;
import com.rni.mes.records.RegistrationRequestDTO;
import com.rni.mes.service.AccountService;
import com.rni.mes.service.TokenService;
import com.rni.mes.service.UtilisateurService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth-service/")
@CrossOrigin(origins = "*")
public class AccountController {

	@Autowired
	 private TokenService tokenService;
	@Autowired
	 private UtilisateurService utilisateurService;
	@Autowired
	 private JwtDecoder jwtDecoder;
	@Autowired
	 private AuthenticationManager authenticationManager;
	 

//	@PostMapping("/token")
//   public ResponseEntity<Map<String,String>> requestForToken(
//           @RequestParam String  username,
//           @RequestParam String  password
//          
//   		){
//		
//       Map<String,String > response;
//     
//           Authentication authentication=authenticationManager.authenticate(
//                   new UsernamePasswordAuthenticationToken(
//                           username,password
//                   )
//           );
//           response=tokenService.generateJwtToken(authentication.getName(),authentication.getAuthorities(), false);
//           return ResponseEntity.ok(response);
//      
//   }
	
	@PostMapping("/auth")
    public ResponseEntity<Map<String,String>> requestForToken(
    		
    		@RequestParam String grantType,
            @RequestParam String  username,
            @RequestParam String  password,
            @RequestParam boolean withRefreshToken,
            String refreshToken
    		){
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
	
//	@GetMapping("/isUsernameAvailable")
//	public boolean isUsernameAvailable(String username) {
//        return accountService.isUsernameAvailable(username);
//    }
//	
//	@GetMapping(path = "/emailActivation")
//    public String emailActivation(String token) {
//        return this.accountService.emailActivation(token);
//    }
	
	@GetMapping("/register")
	public AppUser creationCompte(@RequestParam String username,@RequestParam String nom,
			@RequestParam String prenom,@RequestParam String password, @RequestParam String email,
			@RequestParam String confimPassword, @RequestParam boolean activate
			
			){
		
		AppUser appUser = new AppUser();
		appUser.setUsername(username);
		appUser.setNom(nom);
		
//        return  this.accountService.register(username, password, confimPassword,
//        		prenom, nom, email, activate);
		return appUser;
    }
	
//	@PostMapping("/forgotPassword")
//    public ResponseEntity<Map<String, String>> forgotPassword(String email){
//        try {
//            this.accountService.sendActivationCode(email);
//            return ResponseEntity.ok(Map.of("message","The activation code has been sent to "+email));
//        } catch (EmailNotFoundException e) {
//            return new ResponseEntity<>(Map.of("errorMessage",e.getMessage()),HttpStatus.NOT_FOUND);
//        }catch (Exception e) {
//            return new ResponseEntity<>(Map.of("errorMessage","Internal Error"),HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
	
//	 @PostMapping(path = "/requestForPasswordInit")
//	    public ResponseEntity<Map<String, String>>  authorizePasswordInitialization(String authorizationCode, String email){
//	        try {
//	            this.accountService.authorizePasswordInitialization(authorizationCode, email);
//	            return ResponseEntity.ok(Map.of("message","Your account has been activated successfully"));
//	        } catch (Exception e) {
//	            return new ResponseEntity<>(Map.of("errorMessage",e.getMessage()),HttpStatus.NOT_FOUND);
//	        }
//	    }
	 
//	 @PostMapping(path = "/passwordInitialization")
//	    public void passwordInitialization(PasswordInitializationRequestDTO passwordInitializationRequestDTO){
//	        this.accountService.passwordInitialization(passwordInitializationRequestDTO);
//	    }
}
