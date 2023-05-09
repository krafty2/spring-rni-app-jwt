package com.rni.mes.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rni.mes.models.FichierRni;

@Repository
public interface FichierRniRepository extends CrudRepository<FichierRni, Long> {

}
