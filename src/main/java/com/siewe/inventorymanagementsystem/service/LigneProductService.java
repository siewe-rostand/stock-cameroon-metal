package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.LigneProductDto;
import com.siewe.inventorymanagementsystem.model.Inventaire;
import com.siewe.inventorymanagementsystem.model.LigneProduct;
import com.siewe.inventorymanagementsystem.model.Product;
import com.siewe.inventorymanagementsystem.repository.InventaireRepository;
import com.siewe.inventorymanagementsystem.repository.LigneProductRepository;
import com.siewe.inventorymanagementsystem.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LigneProductService {
    private final Logger log = LoggerFactory.getLogger(LigneProductService.class);

    @Autowired
    private LigneProductRepository ligneProductRepository;

    @Autowired
    private InventaireRepository inventaireRepository;

    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<LigneProductDto> save(LigneProductDto ligneProductDto) {
        log.debug("Request to save LigneProduct : {}", ligneProductDto);

        LigneProduct ligneProduct = new LigneProduct();

        ligneProduct.setId(ligneProductDto.getId());
        ligneProduct.setStockTheorique(ligneProductDto.getStockTheorique());
        ligneProduct.setStockPhysique(ligneProductDto.getStockPhysique());

        if(ligneProductDto.getId() != null){
            Product product = productRepository.findOne(ligneProductDto.getId());
            ligneProduct.setProduct(product);
        }

        if(ligneProductDto.getInventaireId() != null){
            Inventaire inventaire = inventaireRepository.findOne(ligneProductDto.getInventaireId());
            ligneProduct.setInventaire(inventaire);
        }

        LigneProduct result = ligneProductRepository.save(ligneProduct);
        return new ResponseEntity<>(new LigneProductDto().createDTO(result), HttpStatus.CREATED);
    }


    public ResponseEntity<LigneProductDto> update(LigneProductDto ligneProductDto) {
        log.debug("Request to save LigneProduct : {}", ligneProductDto);

        LigneProduct ligneProduct = ligneProductRepository.findOne(ligneProductDto.getId());

        ligneProduct.setId(ligneProductDto.getId());
        ligneProduct.setStockTheorique(ligneProductDto.getStockTheorique());
        ligneProduct.setStockPhysique(ligneProductDto.getStockPhysique());

        LigneProduct result = ligneProductRepository.save(ligneProduct);
        return new ResponseEntity<>(new LigneProductDto().createDTO(result), HttpStatus.CREATED);
    }


    @Transactional(readOnly = true)
    public List<LigneProductDto> findAll() {
        log.debug("Request to get all LigneProducts");
        List<LigneProduct> ligneProducts = ligneProductRepository.findAll();
        List<LigneProductDto> ligneProductDtos = new ArrayList<>();
        for(LigneProduct c: ligneProducts){
            ligneProductDtos.add(new LigneProductDto().createDTO(c));
        }
        return ligneProductDtos;
    }

    /**
     *  Get one ligneProduct by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public LigneProductDto findOne(Long id) {
        log.debug("Request to get LigneProduct : {}", id);
        LigneProduct ligneProduct = ligneProductRepository.findOne(id);
        return new LigneProductDto().createDTO(ligneProduct);
    }


    public void delete(Long id) {
        log.debug("Request to delete LigneProduct : {}", id);
        LigneProduct ligneProduct = ligneProductRepository.findOne(id);
        if(Optional.ofNullable(ligneProduct).isPresent()){
            ligneProductRepository.deleteById(id);
        }
    }
}
