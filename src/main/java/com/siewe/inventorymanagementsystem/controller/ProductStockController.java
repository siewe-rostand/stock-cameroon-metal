package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.dto.ProductStockDto;
import com.siewe.inventorymanagementsystem.service.ProductStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing ProductStock.
 */
@CrossOrigin
@RestController
public class ProductStockController {
    private final Logger log = LoggerFactory.getLogger(ProductStockController.class);

    @Autowired
    private ProductStockService productStockService;

    /**
     * GET  /productStocks : get all the productStocks.
     */
    @GetMapping("/api/product-stock/{productId}")
    public List<ProductStockDto> getProductStock(@PathVariable Long productId,
                                                 @RequestParam(name = "dateFrom") String dateFrom,
                                                 @RequestParam(name = "dateTo") String dateTo) {
        log.debug("REST request to get ProductStocks");
        return productStockService.findByProductIdAndDateRange(productId, dateFrom, dateTo);
    }
}

