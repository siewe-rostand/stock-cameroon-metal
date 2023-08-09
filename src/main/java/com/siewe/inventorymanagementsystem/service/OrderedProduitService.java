package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.OrderedProduitDto;
import com.siewe.inventorymanagementsystem.dto.ProduitDto;
import com.siewe.inventorymanagementsystem.model.OrderedProduit;
import com.siewe.inventorymanagementsystem.model.Orders;
import com.siewe.inventorymanagementsystem.model.Produit;
import com.siewe.inventorymanagementsystem.repository.OrderedProduitRepository;
import com.siewe.inventorymanagementsystem.repository.OrdersRepository;
import com.siewe.inventorymanagementsystem.repository.ProduitRepository;
import com.siewe.inventorymanagementsystem.utils.InvalidOrderItemException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderedProduitService {
    private final OrderedProduitRepository orderedProduitRepository;

    private final OrdersRepository ordersRepository;


    private final ProduitRepository produitRepository;
    private final ProduitService produitService;


    @Transactional
    public OrderedProduitDto save(OrderedProduitDto produitDto) throws InvalidOrderItemException {
        log.debug("save a new order produit : {}",produitDto);
        OrderedProduit orderedProduit = orderedProduitRepository.findByOrdersIdAndProductId(produitDto.getOrderId(),produitDto.getProduitId());
        if (orderedProduit == null){
            orderedProduit = new OrderedProduit();
        }
        Produit produit = produitRepository.findByProduitId(produitDto.getProduitId());
        Orders orders = ordersRepository.findByOrderId(produitDto.getOrderId());
        orderedProduit.setProduct(produit);
        orderedProduit.setMetrage(produitDto.getMetrage());
        orderedProduit.setOrders(orders);
        if (produit.getMetrage() -produitDto.getMetrage() <= 0){
            throw new InvalidOrderItemException("Stock " + produitDto.getName() + " insuffisant");
        }

        OrderedProduit result = orderedProduitRepository.save(orderedProduit);
        Produit produit1 = produitRepository.findByProduitId(produitDto.getProduitId());
        double resteMetrageEnStock  = produit1.getMetrage() - result.getMetrage();
        produit1.setMetrage(resteMetrageEnStock);
        log.debug("reste en stock{}",resteMetrageEnStock);
        produitRepository.save(produit1);

        return new OrderedProduitDto().createDTO(result);
    }

    @Transactional(readOnly = true)
    public OrderedProduitDto findById(Integer id){
        OrderedProduit orderedProduit = orderedProduitRepository.getById(id);
        return new OrderedProduitDto().createDTO(orderedProduit);
    }

    public void delete(Integer id){
        log.debug("Request to delete OrderedProduit : {}", id);
        OrderedProduit orderedProduit = orderedProduitRepository.findOne(id);
        if (Optional.ofNullable(orderedProduit).isPresent()){
            orderedProduitRepository.deleteById(id);
        }
    }
}
