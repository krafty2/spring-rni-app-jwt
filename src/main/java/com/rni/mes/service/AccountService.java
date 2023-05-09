package com.rni.mes.service;

import org.springframework.stereotype.Service;

import com.rni.mes.models.AppRole;
import com.rni.mes.models.AppUser;
import com.rni.mes.repository.AppRoleRepository;
import com.rni.mes.repository.AppUserRepository;

@Service
public class AccountService {

	private AppRoleRepository appRoleRepository;
	private AppUserRepository appUserRepository;
	
	public AccountService(AppRoleRepository appRoleRepository, AppUserRepository appUserRepository) {
		super();
		this.appRoleRepository = appRoleRepository;
		this.appUserRepository = appUserRepository;
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
		return appUserRepository.findByUsername(username);
	}
	
	public AppRole findByRoleName(String roleName) {
		return appRoleRepository.findByRoleName(roleName);
	}
}
