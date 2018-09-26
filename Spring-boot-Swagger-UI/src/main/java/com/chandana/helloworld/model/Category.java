package com.chandana.helloworld.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by TDERVILY on 01/03/2017.
 */
@Entity
@ApiModel(description = "Class representing a Category tracked by the application.")
public class Category implements Serializable {

    @Id
    @GeneratedValue
    @ApiModelProperty(notes = "Unique identifier of the Category. No two Categorys can have the same id.", example = "1", required = true, position = 0)
    Long id;

    @Column(nullable = false)
    String name;

    public Category() {
    }

    public Category(Long id) {
        this.id = id;
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
