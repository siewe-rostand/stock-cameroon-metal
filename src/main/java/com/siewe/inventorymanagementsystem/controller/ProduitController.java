package com.siewe.inventorymanagementsystem.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.siewe.inventorymanagementsystem.dto.ProduitDto;
import com.siewe.inventorymanagementsystem.service.ProduitService;
import com.siewe.inventorymanagementsystem.utils.InvalidActionException;
import com.siewe.inventorymanagementsystem.utils.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ProduitController {
    private final ProduitService produitService;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private  LocalDateTime timestamp;


    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @PostMapping("/produit")
    public ResponseEntity<Object> create(@Valid @RequestBody ProduitDto produitDto) throws URISyntaxException, IOException, InvalidActionException {
        log.debug("controller request to save produit : {}", produitDto);
        timestamp = LocalDateTime.now();
        Calendar cal = Calendar.getInstance();
        HashMap<String, String> error = new HashMap<>();
            if (produitService.existByRef(produitDto.getRef())){
//                error.put("error", "A product with this ref already exist !");
//                error.put("timestamp",cal.getTime().toString());
                throw new InvalidActionException("A product with this ref already exist !");
//                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
        ProduitDto produitDto1 =produitService.save(produitDto);
        return ResponseHandler.generateResponse("produit save Successfully", HttpStatus.CREATED,produitDto1);
    }

    @GetMapping("/produit")
    public Page<ProduitDto> getAllProduct(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                          @RequestParam(name = "size", defaultValue = "50") Integer size,
                                          @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
                                          @RequestParam(name = "direction", defaultValue = "asc") String direction,
                                          @RequestParam(name = "product", defaultValue = "") String product){
        return produitService.findAll(page, size, sortBy, direction, product);
    }
}
