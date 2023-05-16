package com.rni.mes.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rni.mes.config.JwtTokenParam;
import com.rni.mes.enums.AccountStatus;
import com.rni.mes.exception.EmailNotFoundException;
import com.rni.mes.models.AppRole;
import com.rni.mes.models.AppUser;
import com.rni.mes.records.ChangePasswordRequestDTO;
import com.rni.mes.records.PasswordInitializationRequestDTO;
import com.rni.mes.records.RegistrationRequestDTO;
import com.rni.mes.records.UtilisateurRequestDTO;
import com.rni.mes.repository.AppRoleRepository;
import com.rni.mes.repository.AppUserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountService {
	
	

	private AppRoleRepository appRoleRepository;
	private AppUserRepository appUserRepository;
	
	private PasswordEncoder passwordEncoder;
    private JwtEncoder jwtEncoder;
    private JwtDecoder jwtDecoder;
    
    private JwtTokenParam jwtTokenParams;
    private MailService mailservice;
	
	
	
	public AccountService(AppRoleRepository appRoleRepository, AppUserRepository appUserRepository,
			PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, 
			MailService mailservice) {
		super();
		this.appRoleRepository = appRoleRepository;
		this.appUserRepository = appUserRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtEncoder = jwtEncoder;
		this.jwtDecoder = jwtDecoder;
		
		this.mailservice = mailservice;
	}

	public AppUser newUser(AppUser appUser) {
		return appUserRepository.save(appUser);
	}
	
	public AppRole newRole(AppRole appRole) {
		return appRoleRepository.save(appRole);
	}
	
	public void addRoleToUser(String username,String roleName) {
		AppUser appUser = appUserRepository.findByUsername(username);
		AppRole appRole = appRoleRepository.findByRoleName(roleName);
		appUser.getRoles().add(appRole);
	}
	
	public AppUser findByUsername(String username) {
		AppUser appUser = appUserRepository.findByUsername(username);
		if(appUser==null) throw new RuntimeException(String.format("This username %s do not exist",username));
		return appUser;
	}
	
	public AppRole findByRoleName(String roleName) {
		return appRoleRepository.findByRoleName(roleName);
	}
	
	//
	public AppUser utilisateurPlus(
			String username,String password,String email,String repassword,
			boolean emailVerifie,AccountStatus status
			) {
		AppUser appUser = appUserRepository.findByUsername(username);
		if(appUser!=null) throw new RuntimeException(String.format("Username %s already exit",username));
		if(!password.equals(repassword)) throw  new RuntimeException("Passwords not match");
//		appUser=AppUser.builder()
//                .username(username)
//                .email(email)
//                .emailVerifie(emailVerifie)
//                .status(status)
//                .password(passwordEncoder.encode(password))
//                .build();
		return appUserRepository.save(appUser);
	}
	
//	public AppUser updateUserDetails(UtilisateurRequestDTO request){
//        AppUser appUser=appUserRepository.findById(request.userId()).orElse(null);
//       
//        log.info("===========");
//        log.info(request.userId().toString());
//        log.info(request.prenom());
//        log.info(request.nom());
//        log.info(request.email());
//        log.info("===========");
//        if(appUser==null) throw new RuntimeException("User not found");
//        if(request.prenom()!=null) appUser.setNom(request.prenom());
//        if(request.nom()!=null) appUser.setPrenom(request.nom());
//        if(request.email()!=null) {
//            appUser.setEmail(request.email());
//            appUser.setEmailVerifie(false);
//        }
//        appUserRepository.save(appUser);
//        return appUser;
//    }
	
	public AppRole addRole(String roleName) {
        AppRole appRole=appRoleRepository.findByRoleName(roleName);
        if(appRole!=null) throw new RuntimeException(String.format("Role %s already exit",roleName));
        appRole=AppRole.builder().roleName(roleName).build();
        return appRoleRepository.save(appRole);
    }
	
	 public AppRole addRoleToUser(String username, String roleName, boolean deleteRequestRole) {
	        AppUser appUser=appUserRepository.findByUsername(username);
	        if(appUser==null) throw new RuntimeException(String.format("This username %s do not exist",username));
	        AppRole appRole=appRoleRepository.findByRoleName(roleName);
	        if(appRole==null) throw new RuntimeException(String.format("This Role %s do not exist",roleName));
	        if(appUser.getRoles()==null) appUser.setRoles(new ArrayList<>());
	        appUser.getRoles().add(appRole);
	        if(deleteRequestRole) appUser.getRequestedRoles().remove(roleName);
	        return appRole;
	    }
	 
	 public void changePassword(ChangePasswordRequestDTO request, Long userId) {
	        AppUser appUser=appUserRepository.findById(userId).get();
	        if (!passwordEncoder.matches(request.currentPassword(),appUser.getPassword()))
	            throw new RuntimeException("The current password is incorrect");
	        if(!request.newPassword().equals(request.confirmPassword())){
	            throw new RuntimeException("Confirmed password not match");
	        }
	        appUser.setPassword(passwordEncoder.encode(request.newPassword()));
	        appUserRepository.save(appUser);
	    }
	 
	 public boolean isUsernameAvailable(String username){
	        AppUser appUser=appUserRepository.findByUsername(username);
	        return appUser==null;
	    }
	 
	 public boolean isEmailAvailable(String email){
	        AppUser appUser=appUserRepository.findByEmail(email);
	        return appUser==null;
	    }
	 
	 public AppUser findUserByUsernameOrEmail(String usernameOrEmail) {
	        System.out.println(usernameOrEmail);
	        AppUser appUser=appUserRepository.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail);
	        if(appUser==null) throw new RuntimeException("Bad Credentials");
	        return appUser;
	    }
	 
	 public AppUser findUserByUserId(Long userId) {
	        AppUser appUser=appUserRepository.findById(userId).orElse(null);
	        if(appUser==null) throw new RuntimeException(String.format("This username %s do not exist",userId));
	        return appUser;
	    }
	 
	 public AppUser register(String username, String password, String confirmPassword,
			 String prenom, String nom, String email, boolean activate) {
	        AppUser appUser=appUserRepository.findByUsername(username);
	        
	        //verifyEmail(savedAppUser.getId());
	        return appUser;
	    }
	 
	 public void verifyEmail(Long userId){
	       AppUser appUser=appUserRepository.findById(userId).get();
	        Instant instant=Instant.now();
	        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
	                .subject(userId.toString())
	                .issuedAt(instant)
	                .expiresAt(instant.plus(5, ChronoUnit.MINUTES))
	                .issuer("auth-service")
	                .claim("email",appUser.getEmail())
	                .build();
	        String activationJwtToken=jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
	        String emailContent=String.format("To activate yous account click this link : http://localhost:8888/auth-service/public/emailActivation?token="+activationJwtToken);
	        mailservice.sendEmail(appUser.getEmail(),"Email verification",emailContent);
	    }
	 
	 public String emailActivation(String token) {
	        try {
	            Jwt decode = jwtDecoder.decode(token);
	            String subject = decode.getSubject();
	            AppUser appUser=appUserRepository.findById(Long.parseLong(subject)).get();
	            appUser.setEmailVerifie(true);
	            appUser.setStatus(AccountStatus.ACTIVATED);
	            appUserRepository.save(appUser);
	            return "Email verification success";
	        } catch (JwtException e){
	            e.printStackTrace();
	            return e.getMessage();
	        }
	    }
	 
	 public Iterable<AppRole> getAllRoles() {
	        return appRoleRepository.findAll();
	    }
	 
	 public Iterable<AppUser> getAllUsers() {
	        return appUserRepository.findAll();
	    }
	 
	 public void deleteRole(Long id) {
	        this.appRoleRepository.deleteById(id);
	    }
	 
	 public AppUser activateAccount(boolean value, Long userId) {
	        AccountStatus status=value?AccountStatus.ACTIVATED:AccountStatus.DEACTIVATED;
	        AppUser appUser=appUserRepository.findById(userId).get();
	        appUser.setStatus(status);
	        return appUserRepository.save(appUser);
	    }

	 public void sendActivationCode(String email) {
	        AppUser appUser=appUserRepository.findByEmail(email);
	        if(appUser==null) throw new EmailNotFoundException("This email is not associated with any account");
	        Random random=new Random();
	        String activationCode="";
	        for (int i = 0; i <4 ; i++) {
	            activationCode+=random.nextInt(9);
	        }
	        appUser.setTemporaryActivationCode(activationCode);
	        Instant now = LocalDateTime.now().toInstant(ZoneOffset.UTC);
	        appUser.setTemporaryActivationCodeTimeStamp(now);
	        appUserRepository.save(appUser);
	        mailservice.sendEmail(email,"Password Initialization",activationCode);

	    }
	 
	 public void authorizePasswordInitialization(String authorizationCode, String email) {
	        AppUser appUser=appUserRepository.findByEmail(email);
	        if(appUser==null) throw new RuntimeException("This email is not associated with any account");
	        if(!appUser.getTemporaryActivationCode().equals(authorizationCode))
	            throw new RuntimeException("Incorrect authorization code");
	        Instant now = LocalDateTime.now().toInstant(ZoneOffset.UTC);
	        Instant lastInstant=appUser.getTemporaryActivationCodeTimeStamp();
	        Instant lastInstantPlus5=lastInstant.plus(5,ChronoUnit.MINUTES);
	        if(!now.isBefore(lastInstantPlus5))
	            throw new RuntimeException("This authorization code has been expired");
	        appUser.setTemporaryActivationCodeTimeStamp(now);
	        appUserRepository.save(appUser);
	    }
	 
	 public void passwordInitialization(PasswordInitializationRequestDTO initialization) {
	        if(!initialization.password().equals(initialization.confirmPassword()))
	            throw new RuntimeException("Passwords not match");
	        AppUser appUser=appUserRepository.findByEmail(initialization.email());
	        if(appUser==null) throw new RuntimeException("This email is not associated with any account");
	        if(!appUser.getTemporaryActivationCode().equals(initialization.authorizationCode()))
	            throw new RuntimeException("Incorrect authorization code");
	        Instant now = LocalDateTime.now().toInstant(ZoneOffset.UTC);
	        Instant lastInstant=appUser.getTemporaryActivationCodeTimeStamp();
	        Instant lastInstantPlus5=lastInstant.plus(5,ChronoUnit.MINUTES);
	        if(!now.isBefore(lastInstantPlus5))
	            throw new RuntimeException("This authorization code has been expired");
	        appUser.setPassword(passwordEncoder.encode(initialization.password()));
	        appUserRepository.save(appUser);
	    }
}
