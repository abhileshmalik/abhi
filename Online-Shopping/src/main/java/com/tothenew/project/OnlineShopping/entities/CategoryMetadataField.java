package com.tothenew.project.OnlineShopping.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CategoryMetadataField {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

   /* @OneToMany(mappedBy = "categoryMetadataField", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CategoryMetadataFieldValues> fieldValues;*/

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

    /*public Set<CategoryMetadataFieldValues> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(Set<CategoryMetadataFieldValues> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public void addFieldValues(CategoryMetadataFieldValues fieldValue){
        if(fieldValue != null){
            if(fieldValues==null)
                fieldValues = new HashSet<>();

            fieldValues.add(fieldValue);
            fieldValue.setCategoryMetadataField(this);
        }
    }*/
}
