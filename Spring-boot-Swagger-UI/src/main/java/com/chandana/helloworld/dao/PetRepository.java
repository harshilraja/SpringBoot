package com.chandana.helloworld.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.chandana.helloworld.model.Pet;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by TDERVILY on 02/03/2017.
 */
//@RepositoryRestResource(collectionResourceRel = "petR", path = "petR")
public interface PetRepository extends PagingAndSortingRepository<Pet, Long> {
	@ApiOperation("find all Addresses that are associated with a given Customer")
    Pet findByname(String name);
}
