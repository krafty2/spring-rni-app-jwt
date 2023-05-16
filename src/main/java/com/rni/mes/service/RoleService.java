package com.rni.mes.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rni.mes.models.Role;
import com.rni.mes.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	RoleRepository roleRepository;
	
	public Role creerRole(Role role) {
		
		return roleRepository.save(role);
		
	}
	
	public Optional<Role> trouveParRole(String roleName){
		return roleRepository.findByRoleName(roleName);
	}
}
