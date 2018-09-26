package com.chandana.helloworld.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by TDERVILY on 01/03/2017.
 */
@Entity
public class Pet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_PET")
    @SequenceGenerator(sequenceName = "S_PET", allocationSize = 1, name = "S_PET")
    @ApiModelProperty(notes = "Unique identifier of the Pet. No two Pet can have the same id.", example = "1", required = true, position = 0)
    
    Long id;
    @Column(nullable = false)
    @ApiModelProperty(notes = "First name of the Pet.", example = "John", required = true, position = 1)
    String name;
    @Column(nullable = false)
    Integer quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CAT_ID")
    Category category;

    public Pet() {
    }

    public Pet(String name, Integer quantity, Category category) {
        this.name = name;
        this.quantity = quantity;
        this.category = category;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
