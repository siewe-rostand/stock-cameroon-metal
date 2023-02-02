package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.OrderedProductDto;
import com.siewe.inventorymanagementsystem.model.OrderedProduct;
import com.siewe.inventorymanagementsystem.model.Product;
import com.siewe.inventorymanagementsystem.model.Vente;
import com.siewe.inventorymanagementsystem.repository.OrderedProductRepository;
import com.siewe.inventorymanagementsystem.repository.ProductRepository;
import com.siewe.inventorymanagementsystem.repository.VenteRepository;
import com.siewe.inventorymanagementsystem.utils.InvalidOrderItemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderedProductService {

    private final Logger log = LoggerFactory.getLogger(OrderedProductService.class);


    @Autowired
    private VenteRepository venteRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderedProductRepository orderedProductRepository;

    @Autowired
    private StockService productStockService;

    /**
     * Save a orderedProduct.
     *
     * @param orderedProductDto the entity to save
     * @return the persisted entity
     */
    @Transactional
    public OrderedProductDto save(OrderedProductDto orderedProductDto) throws InvalidOrderItemException {
        log.debug("Request to save OrderedProduct : {}", orderedProductDto);

        OrderedProduct orderedProduct = orderedProductRepository.findByVenteAndProduct(orderedProductDto.getVenteId(), orderedProductDto.getProductId());
        if(orderedProduct == null){
            orderedProduct = new OrderedProduct();
        }

        Vente vente = venteRepository.findByVenteId(orderedProductDto.getVenteId());
        orderedProduct.setVente(vente);

        Product product = productRepository.findByProductId(orderedProductDto.getProductId());
        orderedProduct.setProduct(product);

        orderedProduct.setQuantity(orderedProductDto.getQuantity());
        orderedProduct.setPrixVente(orderedProductDto.getPrixVente());

        if(product.getStock() - orderedProductDto.getQuantity() < 0){
            throw new InvalidOrderItemException("Stock " + orderedProductDto.getName() + " insuffisant !");
        }

        OrderedProduct result = orderedProductRepository.save(orderedProduct);
        if(result != null){
            productStockService.deleteStockVente(result);
        }
        return new OrderedProductDto().createDTO(result);
    }

    @Transactional(readOnly = true)
    public List<OrderedProductDto> findAllByVente(Long id) {
        log.debug("Request to get all OrderedProducts by Vente");

        List<OrderedProductDto> orderedProductDtos = new ArrayList<>();
        List<OrderedProduct> orderedProducts = orderedProductRepository.findByVente(id);

        for (OrderedProduct orderedProduct : orderedProducts)
            orderedProductDtos.add(new OrderedProductDto().createDTO(orderedProduct));

        return orderedProductDtos;
    }


    /**
     *  Get one orderedProduct by id.
     *
     *  @param id the id of the orderedProduct
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public OrderedProductDto findOne(Long id) {
        log.debug("Request to get OrderedProduct : {}", id);
        OrderedProduct orderedProduct = orderedProductRepository.findOne(id);
        return new OrderedProductDto().createDTO(orderedProduct);
    }

    /**
     *  Delete the orderedProduct by id.
     *
     *  @param id the id of the orderedProduct
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderedProduct : {}", id);
        OrderedProduct orderedProduct = orderedProductRepository.findOne(id);

        if(Optional.ofNullable(orderedProduct).isPresent()){
            orderedProductRepository.deleteById(id);
        }

    }
}
