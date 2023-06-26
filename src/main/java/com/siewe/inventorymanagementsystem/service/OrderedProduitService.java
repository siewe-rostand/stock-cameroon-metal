package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.OrderedProduitDto;
import com.siewe.inventorymanagementsystem.model.OrderedProduit;
import com.siewe.inventorymanagementsystem.model.Orders;
import com.siewe.inventorymanagementsystem.model.Produit;
import com.siewe.inventorymanagementsystem.repository.OrderedProduitRepository;
import com.siewe.inventorymanagementsystem.repository.OrdersRepository;
import com.siewe.inventorymanagementsystem.repository.ProduitRepository;
import com.siewe.inventorymanagementsystem.utils.InvalidOrderItemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class OrderedProduitService {
    private OrderedProduitRepository orderedProduitRepository;

    private OrdersRepository ordersRepository;


    private ProduitRepository produitRepository;

    public OrderedProduitService(OrderedProduitRepository orderedProduitRepository, OrdersRepository ordersRepository, ProduitRepository produitRepository) {
        this.orderedProduitRepository = orderedProduitRepository;
        this.ordersRepository = ordersRepository;
        this.produitRepository = produitRepository;
    }

    @Transactional
    public OrderedProduitDto save(OrderedProduitDto produitDto) throws InvalidOrderItemException {
        Produit produit = produitRepository.findByProduitId(produitDto.getProduitId());
        Orders orders = ordersRepository.findByOrderId(produitDto.getOrderId());

        OrderedProduit orderedProduit = orderedProduitRepository.findByOrdersAndProduct(orders,produit);
        if (orderedProduit == null){
            orderedProduit = new OrderedProduit();
        }

        orderedProduit.setProduct(produit);
        orderedProduit.setMetrage(produitDto.getMetrage());
        orderedProduit.setOrders(orders);
        if (produit.getMetrage() -produitDto.getMetrage() <= 0){
            throw new InvalidOrderItemException("Stock " + produitDto.getName() + " insuffisant");
        }

        OrderedProduit result = orderedProduitRepository.save(orderedProduit);
        return new OrderedProduitDto().createDTO(result);
    }
}
