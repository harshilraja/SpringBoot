package com.chandana.helloworld.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.chandana.helloworld.model.Category;

/**
 * Created by TDERVILY on 02/03/2017.
 */
//@RepositoryRestResource(collectionResourceRel = "categoryR", path = "categoryR")
public interface CategoryRepository extends CrudRepository<Category, Long> {
}
