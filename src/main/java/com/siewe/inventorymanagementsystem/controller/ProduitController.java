package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.dto.ProduitDto;
import com.siewe.inventorymanagementsystem.service.ProduitService;
import com.siewe.inventorymanagementsystem.utils.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ProduitController {
    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @PostMapping("/produit")
    public ResponseEntity<Object> create(@Valid @RequestBody ProduitDto produitDto) throws URISyntaxException, IOException {
        log.debug("controller request to save produit : {}", produitDto);

        ProduitDto produitDto1 =produitService.save(produitDto);
        return ResponseHandler.generateResponse("produit save Successfully", HttpStatus.CREATED,produitDto1);
    }
}
