package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.dto.StockDto;
import com.siewe.inventorymanagementsystem.service.StockService;
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
public class StockController {
    private final Logger log = LoggerFactory.getLogger(StockController.class);

    @Autowired
    private StockService productStockService;

    /**
     * GET  /productStocks : get all the productStocks.
     */
    @GetMapping("/api/product-stock/{productId}")
    public List<StockDto> getProductStock(@PathVariable Long productId,
                                          @RequestParam(name = "dateFrom") String dateFrom,
                                          @RequestParam(name = "dateTo") String dateTo) {
        log.debug("REST request to get ProductStocks");
        return productStockService.findOneAndDateRange(productId, dateFrom, dateTo);
    }
}

