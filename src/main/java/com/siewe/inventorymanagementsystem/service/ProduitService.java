package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.ProduitDto;
import com.siewe.inventorymanagementsystem.model.Produit;
import com.siewe.inventorymanagementsystem.repository.ProduitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
@Transactional
public class ProduitService {

    private final ProduitRepository produitRepository;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public ProduitDto save(ProduitDto produitDto){
        Produit produit = new Produit();

        produit.setColor(produitDto.getColor());
        produit.setMetrage(produitDto.getMetrage());
        produit.setEpaiseur(produitDto.getEpaiseur());
        produit.setRef(produitDto.getRef());
        produit.setName(produitDto.getName());
        produit.setAvailable(true);

        produit.setCreatedDate(produitDto.getCreatedDate());

        Produit result = produitRepository.save(produit);
        return new ProduitDto().createDTO(result);
    }

    public ProduitDto update(ProduitDto produitDto) throws IOException {
        log.debug("service to update a produit{}",produitDto);
        Produit produit = produitRepository.findByProduitId(produitDto.getId());
       if (produit == null){
           throw new RuntimeException("no product with this id"+produitDto.getId() + "found");
       }else {
           produit.setMetrage(produitDto.getMetrage());
           produit.setEpaiseur(produitDto.getEpaiseur());
           produit.setColor(produitDto.getColor());
           produit.setRef(produitDto.getRef());

           produit.setCreatedDate(produitDto.getCreatedDate());

           Produit result = produitRepository.save(produit);
           return new ProduitDto().createDTO(result);
       }
    }

    public Page<ProduitDto> findAll(Integer page, Integer size, String sortBy,
                                 String direction, String name){
        log.info("service to get all produit");
        Pageable pageable =  PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);

       Page<Produit> produit = produitRepository.findAll(pageable);

        return produit.map(produit1 -> {
            return new ProduitDto().createDTO(produit1);
        });
    }

    public ProduitDto findById(Integer id){
        Produit produit = produitRepository.findByProduitId(id);

        return new ProduitDto().createDTO(produit);
    }

    public void deleteById(Integer id){
        log.debug("service to delete a produit");
        Produit produit = produitRepository.findByProduitId(id);

        if (produit == null){
            throw new RuntimeException("Produit not found");
        }
        produitRepository.deleteById(id);
    }
}
