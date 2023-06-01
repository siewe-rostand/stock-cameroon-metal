package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.ProductDto;
import com.siewe.inventorymanagementsystem.dto.ProductStockDto;
import com.siewe.inventorymanagementsystem.model.*;
import com.siewe.inventorymanagementsystem.repository.ProductRepository;
import com.siewe.inventorymanagementsystem.repository.ProductStockRepository;
import com.siewe.inventorymanagementsystem.utils.InvalidActionException;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductStockService {

    private final Logger log = LoggerFactory.getLogger(ProductStockService.class);

    @Autowired
    private ProductStockRepository productStockRepository;

    @Autowired
    private ProductRepository productRepository;


    public ProductStockDto addStock(Approvisionnement approvisionnement) {

        String pattern = "yyyy-MM-dd";
        LocalDate date = new LocalDate(DateTimeZone.forOffsetHours(1));
        ProductStock lastProductStock = productStockRepository.findFirstByProductIdOrderByDateDesc(approvisionnement.getProduct().getId());

        ProductStock productStock = productStockRepository.findByProductIdAndDate(approvisionnement.getProduct().getId(), date);

        if(productStock == null)
            productStock = new ProductStock();

        productStock.setProduct(approvisionnement.getProduct());
        productStock.setDate(date.toString(pattern));

        if (lastProductStock != null) {
            productStock.setCump(
                    ((lastProductStock.getStock() * lastProductStock.getCump()) +
                            (approvisionnement.getPrixEntree() * approvisionnement.getQuantity()))
                            / (lastProductStock.getStock() + approvisionnement.getQuantity())
            );
            productStock.setCump(Math.round(productStock.getCump() * 100.0) / 100.0);
            productStock.setStock(lastProductStock.getStock() + approvisionnement.getQuantity());
        } else {
            productStock.setStock(Double.valueOf(approvisionnement.getQuantity()));
            productStock.setCump(approvisionnement.getPrixEntree());
        }


        ProductStock result = productStockRepository.save(productStock);
        if(result != null){
            approvisionnement.getProduct().setQuantity(result.getStock());
            System.out.println(approvisionnement.getProduct().getName());
            approvisionnement.getProduct().setCump(result.getCump());
            approvisionnement.getProduct().setValeurStock(result.getStock() * result.getCump());
            productRepository.save(approvisionnement.getProduct());
        }
        return new ProductStockDto().createDTO(result);
    }
    /*
    public void addStock(ProductDto productDto){
        Product product = productRepository.findOne(productDto.getId());
        if (product != null){
            String pattern = "yyyy-MM-dd";
            LocalDate date = new LocalDate(DateTimeZone.forOffsetHours(1));
            ProductStock lastProductStock = productStockRepository.findFirstByProductIdOrderByDateDesc(productDto.getId());
            ProductStock productStock = productStockRepository.findByProductIdAndDate(productDto.getId(), date);

            if(productStock == null)
                productStock = new ProductStock();

            productStock.setProduct(product);
            productStock.setStock(productDto.getStock());
            productStock.setCump(productDto.getPrice());
            productStock.setDate(date.toString(pattern));

            productStockRepository.save(productStock);
        }

    }
     */

    //after deleting approvisionnement
    @Transactional
    public void deleteStockAppro(Approvisionnement approvisionnement) throws InvalidActionException {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDate date = LocalDateTime.parse(approvisionnement.getCreatedDate(), formatter).toLocalDate();
        List<ProductStock> productStocks = productStockRepository.findByProductIdAndDateAfter(approvisionnement.getProduct().getId(), date.minusDays(1));

        for(ProductStock productStock: productStocks){
            productStock.setCump(
                    ((productStock.getStock() * productStock.getCump()) -
                            (approvisionnement.getPrixEntree() * approvisionnement.getQuantity()))
                            / (productStock.getStock() - approvisionnement.getQuantity())
            );
            productStock.setCump(Math.round(productStock.getCump() * 100.0) / 100.0);
            if(productStock.getStock() - approvisionnement.getQuantity() < 0){
                throw new InvalidActionException("Impossible de supprimer ! Des ventes ont déjà été réalisées sur le produit " + productStock.getProduct().getName());
            }
            productStock.setStock(productStock.getStock() - approvisionnement.getQuantity());
            productStockRepository.save(productStock);
        }

        ProductStock lastProductStock = productStockRepository.findFirstByProductIdOrderByDateDesc(approvisionnement.getProduct().getId());

        approvisionnement.getProduct().setQuantity(lastProductStock.getStock());
        approvisionnement.getProduct().setCump(lastProductStock.getCump());
        approvisionnement.getProduct().setValeurStock(lastProductStock.getStock() * lastProductStock.getCump());
        productRepository.save(approvisionnement.getProduct());
    }

    public ProductStockDto deleteStockVente(OrderedProduct op) {

        String pattern = "yyyy-MM-dd";
        LocalDate date = new LocalDate(DateTimeZone.forOffsetHours(1));
        ProductStock lastProductStock = productStockRepository.findByProduct(op.getProduct());

        ProductStock productStock = productStockRepository.findByProductIdAndDate(op.getProduct().getId(), date);

        if(productStock == null)
            productStock = productStockRepository.findByProduct(op.getProduct());


        productStock.setProduct(op.getProduct());
        productStock.setDate(date.toString(pattern));

        if (lastProductStock != null) {
            productStock.setStock(lastProductStock.getStock() - op.getQuantity());
            productStock.setCump(lastProductStock.getCump());
        }
        ProductStock result = productStockRepository.save(productStock);
        if(result != null){
            op.getProduct().setQuantity(result.getStock());
            op.getProduct().setCump(result.getProduct().getCump());
            op.getProduct().setValeurStock(result.getStock() * result.getProduct().getCump());
            productRepository.save(op.getProduct());
        }
        return new ProductStockDto().createDTO(result);
    }

    //after delete vente
    public void restoreStockVente(OrderedProduct op) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDate date = LocalDateTime.parse(op.getVente().getCreatedDate(), formatter).toLocalDate();
        List<ProductStock> productStocks = productStockRepository.findByProductIdAndDateAfter(op.getProduct().getId(), date.minusDays(1));

        for(ProductStock productStock: productStocks){
            productStock.setStock(productStock.getStock() + op.getQuantity());
            productStockRepository.save(productStock);
        }

        ProductStock lastProductStock = productStockRepository.findFirstByProductIdOrderByDateDesc(op.getProduct().getId());

        op.getProduct().setQuantity(lastProductStock.getStock());
        op.getProduct().setCump(lastProductStock.getCump());
        op.getProduct().setValeurStock(lastProductStock.getStock() * lastProductStock.getCump());
        productRepository.save(op.getProduct());
    }

    //after delete manquant
    public void restoreStockManquant(Manquant manquant) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDate date = LocalDateTime.parse(manquant.getCreatedDate(), formatter).toLocalDate();
        List<ProductStock> productStocks = productStockRepository.findByProductIdAndDateAfter(manquant.getProduct().getId(), date.minusDays(1));

        for(ProductStock productStock: productStocks){
            productStock.setStock(productStock.getStock() + manquant.getQuantity());
            productStockRepository.save(productStock);
        }

        ProductStock lastProductStock = productStockRepository.findFirstByProductIdOrderByDateDesc(manquant.getProduct().getId());

        manquant.getProduct().setQuantity(lastProductStock.getStock());
        manquant.getProduct().setCump(lastProductStock.getCump());
        manquant.getProduct().setValeurStock(lastProductStock.getStock() * lastProductStock.getCump());
        productRepository.save(manquant.getProduct());
    }

    public ProductStockDto deleteStockManquant(Manquant manquant) {

        String pattern = "yyyy-MM-dd";
        LocalDate date = new LocalDate(DateTimeZone.forOffsetHours(1));
        ProductStock lastProductStock = productStockRepository.findFirstByProductIdOrderByDateDesc(manquant.getProduct().getId());

        ProductStock productStock = productStockRepository.findByProductIdAndDate(manquant.getProduct().getId(), date);

        if(productStock == null)
            productStock = new ProductStock();

        productStock.setProduct(manquant.getProduct());
        productStock.setDate(date.toString(pattern));

        if (lastProductStock != null) {
            productStock.setStock(lastProductStock.getStock() - manquant.getQuantity());
            productStock.setCump(lastProductStock.getCump());
        }
        ProductStock result = productStockRepository.save(productStock);
        if(result != null){
            manquant.getProduct().setQuantity(result.getStock());
            manquant.getProduct().setCump(result.getProduct().getCump());
            manquant.getProduct().setValeurStock(result.getStock() * result.getProduct().getCump());
            productRepository.save(manquant.getProduct());
        }
        return new ProductStockDto().createDTO(result);
    }

    public List<ProductStockDto> findByProductIdAndDateRange(Long productId, String dateFrom, String dateTo) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
        LocalDate df = null;
        if(dateFrom!=null)
            df = formatter.parseLocalDate(dateFrom);

        LocalDate dt = null;
        if(dateTo!=null)
            dt = formatter.parseLocalDate(dateTo);

        List<ProductStock> productStocks = productStockRepository.findByProductIdAndDateRange(productId, df, dt);
        List<ProductStockDto> productStockDtos = new ArrayList<>();
        for(ProductStock productStock: productStocks){
            productStockDtos.add(new ProductStockDto().createDTO(productStock));
        }
        return productStockDtos;
    }
}