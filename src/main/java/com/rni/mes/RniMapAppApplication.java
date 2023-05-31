package com.rni.mes;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rni.mes.config.RsaKeyProperties;
import com.rni.mes.enums.AccountStatus;
import com.rni.mes.models.Role;
import com.rni.mes.models.Utilisateur;
import com.rni.mes.service.RoleService;
import com.rni.mes.service.UtilisateurService;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class RniMapAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RniMapAppApplication.class, args);
	}
	

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    KeyPair keyPair() throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance("RSA");
        var keyPair=keyPairGenerator.generateKeyPair();
        return keyPair;
    }
    
    @Bean
    CommandLineRunner start(
    		UtilisateurService utilisateurService,
    		RoleService roleService,
    		PasswordEncoder passwordEncoder
    		){
        return args -> {
        	
        	Role role = new Role();
        	role.setRoleName("ADMIN");
        
        	roleService.creerRole(role);
        	
        	Utilisateur utilisateur = new Utilisateur();
        	utilisateur.setUsername("admin");
        	utilisateur.setNom("administrateur");
        	utilisateur.setPrenom("admininatreur");
        	utilisateur.setPassword(passwordEncoder.encode("1234"));
        	utilisateur.setEmail("irt.app@irt.com");
        	utilisateur.setStatus(AccountStatus.ACTIVATED);
        	utilisateur.setEmailVerifie(true);
        	utilisateur.getRoles().add(role);
        	
        	utilisateurService.creerUtilisateur(utilisateur);
        
        };
    }

}
