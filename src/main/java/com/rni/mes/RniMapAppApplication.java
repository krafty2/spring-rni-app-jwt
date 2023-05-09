package com.rni.mes;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rni.mes.config.RsaKeyProperties;
import com.rni.mes.models.AppRole;
import com.rni.mes.models.AppUser;
import com.rni.mes.service.AccountService;

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
    CommandLineRunner start(AccountService accountService, PasswordEncoder passwordEncoder){
        return args -> {
        	AppRole userRole = new AppRole();
        	userRole.setRoleName("USER");
        	accountService.newRole(userRole);
        	AppRole adminRole = new AppRole();
        	adminRole.setRoleName("ADMIN");
        	accountService.newRole(adminRole);
        	
        	String userR = "USER";
        	String adminR = "ADMIN";
        	
        	AppRole findUserRole = accountService.findByRoleName(userR);
        	AppRole findAdminRole = accountService.findByRoleName(adminR);
        	
        	AppUser user1 = new AppUser();
        	user1.setUsername("user1");
        	user1.setPassword(passwordEncoder.encode("1234"));
        	
        	user1.getRoles().add(findUserRole);
        	accountService.newUser(user1);
        	
        	
        	AppUser admin1 = new AppUser();
        	admin1.setUsername("admin");
        	admin1.setPassword(passwordEncoder.encode("1015"));
        	
        	admin1.getRoles().add(findAdminRole);
        	accountService.newUser(admin1);
        };
    }

}
