package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    default Category findOne(Long id) {
        return (Category) findById(id).orElse(null);
    }
    Category findByCategoryId(Long id);

    @Query("SELECT  c FROM  Category c "
            + "WHERE c.name = ?1" )
    Category findByName(String name);


    List<Category> findByEnabledTrue();

    @Override
    void delete(Category category);
}
