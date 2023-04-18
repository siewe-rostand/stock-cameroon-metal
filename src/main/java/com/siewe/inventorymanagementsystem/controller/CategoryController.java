package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.dto.CategoryDto;
import com.siewe.inventorymanagementsystem.model.error.EntityNotFoundException;
import com.siewe.inventorymanagementsystem.service.CategoryService;
import com.siewe.inventorymanagementsystem.utils.CustomErrorType;
import com.siewe.inventorymanagementsystem.utils.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Category.
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
public class CategoryController {

    private final Logger log = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    /**
     * POST  /categories : Create a new category.
     *
     * @param categoryDto the category to create
     * @return the ResponseEntity with status 201 (Created) and with body the new category, or with status 400 (Bad Request) if the category has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/categories")
    public ResponseEntity<Object> createCategory(@Valid @RequestBody CategoryDto categoryDto) throws URISyntaxException {
        log.debug("REST request to save Category : {}", categoryDto);
        if (categoryService.findByName(categoryDto.getName()) != null) {
            return new ResponseEntity<>(new CustomErrorType("Unable to create. A category with name \""+
                    categoryDto.getName() + "\" already exist."), HttpStatus.CONFLICT);
        }
        return categoryService.save(categoryDto);
    }

    /**
     * PUT  /categories : Updates an existing category.
     *
     * @param categoryDto the category to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated category,
     * or with status 400 (Bad Request) if the category is not valid,
     * or with status 500 (Internal Server Error) if the category couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/categories")
    public ResponseEntity<Object> updateCategory(@Valid @RequestBody CategoryDto categoryDto) throws URISyntaxException {
        log.debug("REST request to update Category : {}", categoryDto);
        if (categoryDto.getId() == null) {
            return createCategory(categoryDto);
        }
        return categoryService.update(categoryDto);
    }


    /**
     * GET  /categories : get all the categories.
     */
    @GetMapping("/categories-enabled")
    public List<CategoryDto> getEnabledByStore() throws URISyntaxException {
        log.debug("REST request to get a list of Categories");
        return categoryService.findAll();
    }

    @GetMapping("/categories")
    public ResponseEntity<Object> getByStore()
            throws URISyntaxException {
        log.debug("REST request to get all categories");
        List<CategoryDto> result = categoryService.findAll();

        return ResponseHandler.generateResponse("get categories successfully",HttpStatus.OK,result);
    }
    /**
     * GET  /categories/:id : get the "id" category.
     *
     * @param id the id of the category to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the category, or with status 404 (Not Found)
     */
    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long id) {
        log.debug("REST request to get Category : {}", id);
        CategoryDto categoryDto = categoryService.findOne(id);
        return Optional.ofNullable(categoryDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * DELETE  /categories/:id : delete the "id" category.
     *
     * @param id the id of the category to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        log.debug("REST request to delete Category : {}", id);
        categoryService.delete(id);
        return new ResponseEntity<CategoryDto>(HttpStatus.NO_CONTENT);
    }
}
