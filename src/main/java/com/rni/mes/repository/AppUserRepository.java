package com.rni.mes.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rni.mes.models.AppUser;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Long> {

	AppUser findByUsername(String username);

}
