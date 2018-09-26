package com.chandana.helloworld.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chandana.helloworld.dao.CategoryRepository;
import com.chandana.helloworld.model.Category;

import io.swagger.annotations.Api;

/**
 * Created by TDERVILY on 06/03/2017.
 */
@RestController
@RequestMapping("/category")
@Api(value = "category", description = "Rest API for Category operations", tags = "Category Entity")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @CrossOrigin
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Category>> list() {
        return new ResponseEntity<>(this.categoryRepository.findAll(), HttpStatus.OK);
    }
}
