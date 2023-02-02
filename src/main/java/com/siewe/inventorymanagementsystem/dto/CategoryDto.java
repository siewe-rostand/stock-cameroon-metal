package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.Category;
import com.siewe.inventorymanagementsystem.model.Product;
import lombok.Data;

@Data
public class CategoryDto {

    private Long id;
    private String name;
    private Boolean enabled;
    private int nbProducts;

    public CategoryDto createDTO(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        if(category != null){
            categoryDto.setId(category.getCategoryId());
            categoryDto.setName(category.getName());
            categoryDto.setEnabled(category.getEnabled());

            int cpt = 0;
            if(category.getProducts() != null){
                for(Product product : category.getProducts()){
                    if(product.getDeleted() != null){
                        if(!product.getDeleted())
                            cpt++;
                    }
                    else
                        cpt++;
                }
            }
            categoryDto.setNbProducts(cpt);

            return categoryDto;
        }
        return null;
    }

}
