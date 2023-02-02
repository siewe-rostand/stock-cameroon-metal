package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.Category;
import com.siewe.inventorymanagementsystem.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

//    default Product findOne(Long id) {
//        return (Product) findById(id).orElse(null);
//    }


    Product findByProductId(Long id);
    Product findByName(String name);
    Product deleteByProductId(Long id);

    @Query("SELECT  p FROM  Product p "
            + "WHERE  ( p.name like ?1 or p.cip like ?1 )"
            + "AND (p.deleted is null or p.deleted = false)")
    Page<Product> findAll(String name, Pageable pageable);

    /*@Query("SELECT  p FROM  Product p "
            + "left join p.category cat "
            + "WHERE  ( p.name like ?1 or p.cip like ?1 )"
            + "and (cat.name like ?2 or cat.name like ?2 or ?2 is null)" )
    Page<Product> findAll(String name, String category, Pageable pageable);*/

    @Query("SELECT  p FROM  Product p "
            + "WHERE ( p.name like ?1 or p.cip like ?1 )"
            + "AND p.enabled = true "
            + "AND (p.deleted is null or p.deleted = false)")
    Page<Product> findByEnabledTrue(String name, Pageable pageable);

    @Query("SELECT p FROM  Product p "
            + "WHERE  p.enabled = true "
            + "AND (p.deleted is null or p.deleted = false)")
    Page<Product> findByEnabledTrue(Pageable pageable);


    @Query("SELECT p FROM Product p "
            + "WHERE p.enabled = true "
            + "AND (p.deleted is null or p.deleted = false) "
            + "AND p.name like ?1 or p.cip like ?1 ")
    List<Product> findByMc(String s);

    @Query("SELECT  p FROM  Product p "
            + "WHERE  ( p.name like ?1 or p.cip like ?1 )"
            + "AND (p.deleted is null or p.deleted = false) "
            + "AND p.stock <= p.stockAlerte ")
    Page<Product> findAllWithStockBas(String product, Pageable pageable);

    @Query("SELECT p FROM  Product p "
            + "WHERE  p.enabled = true "
            + "AND (p.deleted is null or p.deleted = false) "
            + "AND p.stock <= p.stockAlerte ")
    Page<Product> findByEnabledTrueWithStockBas(String s, Pageable pageable);

    List<Product> findByCategory(Long id);

    @Query("SELECT  p FROM  Product p "
            + "WHERE  ( p.name like ?1 or p.cip like ?1 )"
            + "AND (p.deleted is null or p.deleted = false) "
            + "AND (p.category.categoryId = ?2 ) "
            + "AND p.stock <= p.stockAlerte ")
    Page<Product> findAllByCategoryIdWithStockBas(String s, Long id, Pageable pageable);

    @Query("SELECT  p FROM  Product p "
            + "WHERE  ( p.name like ?1 or p.cip like ?1 )"
            + "AND (p.category.categoryId = ?2 ) "
            + "AND (p.deleted is null or p.deleted = false)")
    Page<Product> findAllByCategoryId(String s, Long id, Pageable pageable);
}
