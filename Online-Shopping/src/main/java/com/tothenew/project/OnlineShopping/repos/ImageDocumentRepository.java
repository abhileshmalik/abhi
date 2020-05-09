package com.tothenew.project.OnlineShopping.repos;

import com.tothenew.project.OnlineShopping.entities.ImageDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageDocumentRepository extends CrudRepository<ImageDocument,Long> {


}
