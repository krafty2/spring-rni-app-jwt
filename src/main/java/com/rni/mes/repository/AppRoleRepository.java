package com.rni.mes.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rni.mes.models.AppRole;

@Repository
public interface AppRoleRepository extends CrudRepository<AppRole, Long> {

	AppRole findByRoleName(String roleName);

}
