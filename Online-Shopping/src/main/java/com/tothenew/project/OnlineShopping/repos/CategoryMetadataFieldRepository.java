package com.tothenew.project.OnlineShopping.repos;

import com.tothenew.project.OnlineShopping.entities.CategoryMetadataField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMetadataFieldRepository extends CrudRepository<CategoryMetadataField, Long> {

    CategoryMetadataField findByNameIgnoreCase(String fieldName);

    List<CategoryMetadataField> findAll();

    List<CategoryMetadataField> findAll(Pageable pageable);

}