package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit,Integer> {

    Produit findByProduitId(Integer id);
    Produit findByColor(String color);
    Produit findByRef(String ref);

    Page<Produit> findAll( Pageable pageable);

    @Query("select p from Produit p WHERE p.color like ?1 or p.ref like ?1")
    List<Produit> findByKeyword(String keyword);
}
