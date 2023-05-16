package com.rni.mes.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rni.mes.models.AppUser;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Long> {

	AppUser findByUsername(String username);
	AppUser findByEmail(String email);
    AppUser findByUsernameOrEmail(String username, String email);
    List<AppUser> findByUsernameContains(String keyWord);

}
