package com.rni.mes.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.rni.mes.models.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Optional<Role> findByRoleName(String roleName);

}
