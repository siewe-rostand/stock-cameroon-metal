package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.CategoryDto;
import com.siewe.inventorymanagementsystem.model.Category;
import com.siewe.inventorymanagementsystem.repository.CategoryRepository;
import com.siewe.inventorymanagementsystem.utils.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {
    private final Logger log = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryRepository categoryRepository;


    public Category addNewCategory(String name, List<String> subcategories) {
        return categoryRepository.save(new Category(name));
    }

    /**
     * Save a category.
     *
     * @param categoryDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity<Object> save(CategoryDto categoryDto) {
        log.debug("Request to save Category : {}", categoryDto);

        Category category = new Category();

        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        category.setEnabled(true);

        Category result = categoryRepository.save(category);
        return ResponseHandler.generateResponse("category created Successfully",HttpStatus.CREATED,new CategoryDto().createDTO(result));
    }

    public ResponseEntity<Object> update(CategoryDto categoryDto) {
        log.debug("Request to save Category : {}", categoryDto);

        Category category = categoryRepository.findOne(categoryDto.getId());

        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        category.setEnabled(false);

        Category result = categoryRepository.save(category);
        return ResponseHandler.generateResponse("category updated Successfully",HttpStatus.CREATED,new CategoryDto().createDTO(result));
    }

    /**
     *  Get all the categories.
     *
     *  @return the list of entities
     */

    @Transactional(readOnly = true)
    public List<CategoryDto> findAll() {
        log.debug("Request to get all Categorys");
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for(Category c: categories){
            categoryDtos.add(new CategoryDto().createDTO(c));
        }
        return categoryDtos;
    }

    /**
            *  Get one category by id.
     *
             *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CategoryDto findOne(Long id) {
        log.debug("Request to get Category : {}", id);
        Category category = categoryRepository.findOne(id);
        return new CategoryDto().createDTO(category);
    }

    /**
     *  Delete the  category by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Category : {}", id);
        Category category = categoryRepository.findOne(id);
        if(Optional.ofNullable(category).isPresent()){
            category.setEnabled(false);
            categoryRepository.deleteById(id);
        }
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }
}
