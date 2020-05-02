package com.tothenew.project.OnlineShopping.product;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.springframework.stereotype.Component;
import javax.persistence.*;
import java.util.Set;

@Component
@Entity
@JsonFilter("categoryfilter")
@ApiModel(description = "All details about the Category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long category_id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category subcategory;

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Category subcategory) {
        this.subcategory = subcategory;
    }
}
