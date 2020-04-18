package com.tothenew.project.OnlineShopping.utils;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CategoryMetadataFieldValuesID implements Serializable {
    private Long categoryId;
    private Long categoryMetadataFieldId;

}