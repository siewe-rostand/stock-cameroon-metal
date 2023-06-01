package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.OrderedProduct;
import com.siewe.inventorymanagementsystem.model.Vente;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenteRepository extends JpaRepository<Vente, Long> {

    default Vente findOne(Long id) {
        return (Vente) findById(id).orElse(null);
    }
    @Query("SELECT vente FROM  Vente vente "
            + "WHERE vente.user.userId = ?1 "
            + "AND vente.createdDate between ?2 and ?3 "
            + "AND (vente.deleted is null or vente.deleted = false) ")
    Page<Vente> findBySellerId(Long sellerId, LocalDateTime cdf, LocalDateTime cdt, Pageable pageable);

    @Query("SELECT vente FROM  Vente vente "
            + "WHERE vente.createdDate between ?1 and ?2 "
            + "AND (vente.deleted is null or vente.deleted = false) ")
    List<Vente> findByCreatedDateBetween(LocalDateTime d, LocalDateTime localDateTime);

    @Query("SELECT vente FROM  Vente vente "
            + "WHERE vente.user.userId = ?1 "
            + "AND vente.createdDate between ?2 and ?3 "
            + "AND (vente.deleted is null or vente.deleted = false) ")
    List<Vente> findBySellerIdAndCreatedDateBetween(Long sellerId, LocalDateTime cdf, LocalDateTime cdt);

    @Query("SELECT vente FROM  Vente vente "
            + "WHERE vente.acompte < vente.totalPrice"
            + "AND (vente.deleted is null or vente.deleted = false) ")
    Page<Vente> findRecouvrements(Pageable pageable);

    @Query("SELECT vente FROM  Vente vente "
            + "WHERE vente.acompte < vente.totalPrice"
            + "AND (vente.deleted is null or vente.deleted = false) ")
    List<Vente> findRecouvrements();

    @Query("SELECT vente FROM  Vente vente "
            + "WHERE vente.createdDate between ?1 and ?2 "
            + "AND (vente.deleted is null or vente.deleted = false) ")
    Page<Vente> findByCreatedDateBetween(LocalDateTime cdf, LocalDateTime cdt, Pageable pageable);

    @Query("SELECT vente FROM  Vente vente "
            + "WHERE vente.deleted = true" )
    Page<Vente> findVentesDeleted(Pageable pageable);

    @Query("SELECT vente FROM  Vente vente "
            + "WHERE vente.user.userId = ?1 "
            + "AND vente.deleted = true" )
    Page<Vente> findVentesDeletedBySellerId(Long sellerId, Pageable pageable);

    public interface ProductSales {
        Long getId();
        String getProductName();
        Double getQuantity();
        Double getBenefice();
        Double getTotalVentes();
    }

    @Query("SELECT op.product.id as productId, op.product.name as productName, "
            + "SUM(op.quantity) as quantity, "
            + "SUM(op.quantity * (op.prixVente - op.product.cump)) as benefice, "
            + "SUM(op.quantity * op.prixVente) as totalVentes FROM OrderedProduct op "
            + "WHERE op.vente.createdDate between ?1 and ?2 "
            + "GROUP BY op.product.id ORDER BY totalVentes DESC")
    List<ProductSales> findProductSalesByDateRange(LocalDateTime cdf, LocalDateTime cdt);

    /*
    @Query("SELECT op.product.id as productId, op.product.name as productName, "
            + "SUM(op.quantity) as quantity, "
            + "SUM(op.quantity * (op.prixVente - op.product.cump)) as benefice, "
            + "SUM(op.quantity * op.prixVente) as totalVentes FROM OrderedProduct op "
            + "WHERE op.vente.createdDate between ?2 and ?3 "
            + "GROUP BY op.product.id ORDER BY totalVentes DESC")
    List<ProductSales> findProductSalesByDateRange(LocalDateTime cdf, LocalDateTime cdt);
    */

    @Query("SELECT op.product.id as productId, op.product.name as productName, "
            + "SUM(op.quantity) as quantity, "
            + "SUM(op.quantity * (op.prixVente - op.product.cump)) as benefice, "
            + "SUM(op.quantity * op.prixVente) as totalVentes FROM OrderedProduct op "
            + "WHERE op.vente.user.userId = ?1 "
            + "AND op.vente.createdDate between ?2 and ?3 "
            + "GROUP BY op.product.id ORDER BY totalVentes DESC")
    List<ProductSales> findProductSalesBySellerIdAndDateRange(Long sellerId, LocalDateTime cdf, LocalDateTime cdt);

    /*public interface DailySales {
        String getDate();
        Double getBenefice();
        Double getTotalVentes();
    }

    @Query("SELECT vente.createdDate as date, "
            + "SUM(vente.quantity * (vente.prixVente - vente.product.cump)) as benefice, "
            + "SUM(vente.quantity * vente.prixVente) as totalVentes FROM Vente vente "
            + "WHERE vente.createdDate between ?1 and ?2 "
            + "GROUP BY vente.createdDate")
    List<DailySales> findDailySalesByDateRange(LocalDateTime cdf, LocalDateTime cdt);*/
}