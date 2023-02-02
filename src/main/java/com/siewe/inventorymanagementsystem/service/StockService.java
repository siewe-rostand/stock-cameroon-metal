package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.StockDto;
import com.siewe.inventorymanagementsystem.model.Approvisionnement;
import com.siewe.inventorymanagementsystem.model.Manquant;
import com.siewe.inventorymanagementsystem.model.OrderedProduct;
import com.siewe.inventorymanagementsystem.model.Stock;
import com.siewe.inventorymanagementsystem.repository.ProductRepository;
import com.siewe.inventorymanagementsystem.repository.StockRepository;
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
public class StockService {

    private final Logger log = LoggerFactory.getLogger(StockService.class);

    @Autowired
    private StockRepository productStockRepository;

    @Autowired
    private ProductRepository productRepository;

    //after deleting approvisionnement
    public StockDto addStock(Approvisionnement approvisionnement) {

        String pattern = "yyyy-MM-dd";
        LocalDate date = new LocalDate(DateTimeZone.forOffsetHours(1));
        Stock lastStock = productStockRepository.findFirstByProductOrderByDateDesc(approvisionnement.getProduct().getProductId());

        Stock productStock = productStockRepository.findByProductAndDate(approvisionnement.getProduct().getProductId(), date);

        if(productStock == null)
            productStock = new Stock();

        productStock.setProduct(approvisionnement.getProduct());
        productStock.setCreatedDate(date.toString(pattern));

        if (lastStock != null) {
            productStock.setCump(
                    ((lastStock.getQuantity() * lastStock.getCump()) +
                            (approvisionnement.getPrixEntree() * approvisionnement.getQuantity()))
                            / (lastStock.getQuantity() + approvisionnement.getQuantity())
            );
            productStock.setCump(Math.round(productStock.getCump() * 100.0) / 100.0);
            productStock.setQuantity(lastStock.getQuantity() + approvisionnement.getQuantity());
        } else {
            productStock.setQuantity(Double.valueOf(approvisionnement.getQuantity()));
            productStock.setCump(approvisionnement.getPrixEntree());
        }


        Stock result = productStockRepository.save(productStock);
        if(result != null){
            approvisionnement.getProduct().setStock(result.getQuantity());
            System.out.println(approvisionnement.getProduct().getName());
            approvisionnement.getProduct().setCump(result.getCump());
            approvisionnement.getProduct().setValeurStock(result.getQuantity() * result.getCump());
            productRepository.save(approvisionnement.getProduct());
        }
        return new StockDto().createDTO(result);
    }

    //after deleting approvisionnement
    @Transactional
    public void deleteStockAppro(Approvisionnement approvisionnement) throws InvalidActionException {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDate date = LocalDateTime.parse(approvisionnement.getCreatedDate(), formatter).toLocalDate();
        List<Stock> productStocks = productStockRepository.findByProductAndDateAfter(approvisionnement.getProduct().getProductId(), date.minusDays(1));

        for(Stock productStock: productStocks){
            productStock.setCump(
                    ((productStock.getQuantity() * productStock.getCump()) -
                            (approvisionnement.getPrixEntree() * approvisionnement.getQuantity()))
                            / (productStock.getQuantity() - approvisionnement.getQuantity())
            );
            productStock.setCump(Math.round(productStock.getCump() * 100.0) / 100.0);
            if(productStock.getQuantity() - approvisionnement.getQuantity() < 0){
                throw new InvalidActionException("Impossible de supprimer ! Des ventes ont déjà été réalisées sur le produit " + productStock.getProduct().getName());
            }
            productStock.setQuantity(productStock.getQuantity() - approvisionnement.getQuantity());
            productStockRepository.save(productStock);
        }

        Stock lastStock = productStockRepository.findFirstByProductOrderByDateDesc(approvisionnement.getProduct().getProductId());

        approvisionnement.getProduct().setStock(lastStock.getQuantity());
        approvisionnement.getProduct().setCump(lastStock.getCump());
        approvisionnement.getProduct().setValeurStock(lastStock.getQuantity() * lastStock.getCump());
        productRepository.save(approvisionnement.getProduct());
    }

    public StockDto deleteStockVente(OrderedProduct op) {

        String pattern = "yyyy-MM-dd";
        LocalDate date = new LocalDate(DateTimeZone.forOffsetHours(1));
        Stock lastStock = productStockRepository.findFirstByProductOrderByDateDesc(op.getProduct().getProductId());

        Stock productStock = productStockRepository.findByProductAndDate(op.getProduct().getProductId(), date);

        if(productStock == null)
            productStock = new Stock();

        productStock.setProduct(op.getProduct());
        productStock.setCreatedDate(date.toString(pattern));

        if (lastStock != null) {
            productStock.setQuantity(lastStock.getQuantity() - op.getQuantity());
            productStock.setCump(lastStock.getCump());
        }
        Stock result = productStockRepository.save(productStock);
        if(result != null){
            op.getProduct().setStock(result.getQuantity());
            op.getProduct().setCump(result.getProduct().getCump());
            op.getProduct().setValeurStock(result.getQuantity() * result.getProduct().getCump());
            productRepository.save(op.getProduct());
        }
        return new StockDto().createDTO(result);
    }

    //after delete vente
    public void restoreStockVente(OrderedProduct op) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDate date = LocalDateTime.parse(op.getVente().getCreatedDate(), formatter).toLocalDate();
        List<Stock> productStocks = productStockRepository.findByProductAndDateAfter(op.getProduct().getProductId(), date.minusDays(1));

        for(Stock productStock: productStocks){
            productStock.setQuantity(productStock.getQuantity() + op.getQuantity());
            productStockRepository.save(productStock);
        }

        Stock lastStock = productStockRepository.findFirstByProductOrderByDateDesc(op.getProduct().getProductId());

        op.getProduct().setStock(lastStock.getQuantity());
        op.getProduct().setCump(lastStock.getCump());
        op.getProduct().setValeurStock(lastStock.getQuantity() * lastStock.getCump());
        productRepository.save(op.getProduct());
    }

    //after delete manquant
    public void restoreStockManquant(Manquant manquant) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDate date = LocalDateTime.parse(manquant.getCreatedDate(), formatter).toLocalDate();
        List<Stock> productStocks = productStockRepository.findByProductAndDateAfter(manquant.getProduct().getProductId(), date.minusDays(1));

        for(Stock productStock: productStocks){
            productStock.setQuantity(productStock.getQuantity() + manquant.getQuantity());
            productStockRepository.save(productStock);
        }

        Stock lastStock = productStockRepository.findFirstByProductOrderByDateDesc(manquant.getProduct().getProductId());

        manquant.getProduct().setStock(lastStock.getQuantity());
        manquant.getProduct().setCump(lastStock.getCump());
        manquant.getProduct().setValeurStock(lastStock.getQuantity() * lastStock.getCump());
        productRepository.save(manquant.getProduct());
    }

    public StockDto deleteStockManquant(Manquant manquant) {

        String pattern = "yyyy-MM-dd";
        LocalDate date = new LocalDate(DateTimeZone.forOffsetHours(1));
        Stock lastStock = productStockRepository.findFirstByProductOrderByDateDesc(manquant.getProduct().getProductId());

        Stock productStock = productStockRepository.findByProductAndDate(manquant.getProduct().getProductId(), date);

        if(productStock == null)
            productStock = new Stock();

        productStock.setProduct(manquant.getProduct());
        productStock.setCreatedDate(date.toString(pattern));

        if (lastStock != null) {
            productStock.setQuantity(lastStock.getQuantity() - manquant.getQuantity());
            productStock.setCump(lastStock.getCump());
        }
        Stock result = productStockRepository.save(productStock);
        if(result != null){
            manquant.getProduct().setStock(result.getQuantity());
            manquant.getProduct().setCump(result.getProduct().getCump());
            manquant.getProduct().setValeurStock(result.getQuantity() * result.getProduct().getCump());
            productRepository.save(manquant.getProduct());
        }
        return new StockDto().createDTO(result);
    }

    public List<StockDto> findByProductIdAndDateRange(Long productId, String dateFrom, String dateTo) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
        LocalDate df = null;
        if(dateFrom!=null)
            df = formatter.parseLocalDate(dateFrom);

        LocalDate dt = null;
        if(dateTo!=null)
            dt = formatter.parseLocalDate(dateTo);

        List<Stock> productStocks = productStockRepository.findByProductIdAndDateRange(productId, df, dt);
        List<StockDto> productStockDtos = new ArrayList<>();
        for(Stock productStock: productStocks){
            productStockDtos.add(new StockDto().createDTO(productStock));
        }
        return productStockDtos;
    }
}
