package com.tothenew.project.OnlineShopping.repos;

import com.tothenew.project.OnlineShopping.entities.CategoryMetadataFieldValues;
import com.tothenew.project.OnlineShopping.utils.CategoryMetadataFieldValuesID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMetadataFieldValuesRepository extends CrudRepository<CategoryMetadataFieldValues, CategoryMetadataFieldValuesID> {

   /* @Query(value = "select f.id, f.name from category_metadata_field_values v " +
            "inner join " +
            "category_metadata_field f where v.category_metadata_field_id = f.id " +
            "and v.category_id= :categoryId", nativeQuery = true)
    List<Object[]> findMetadataFieldsByCategoryId(Long categoryId);*/

}