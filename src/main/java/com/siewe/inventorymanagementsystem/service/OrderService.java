package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.OrderDto;
import com.siewe.inventorymanagementsystem.dto.OrderedProductDto;
import com.siewe.inventorymanagementsystem.dto.OrderedProduitDto;
import com.siewe.inventorymanagementsystem.dto.ProduitDto;
import com.siewe.inventorymanagementsystem.model.Orders;
import com.siewe.inventorymanagementsystem.model.Produit;
import com.siewe.inventorymanagementsystem.model.User;
import com.siewe.inventorymanagementsystem.repository.OrdersRepository;
import com.siewe.inventorymanagementsystem.repository.ProduitRepository;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.utils.InvalidOrderItemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@Transactional
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderedProduitService orderedProductService;

    @Autowired
    private ProduitRepository produitRepository;

    public Orders save(OrderDto orderDto) throws InvalidOrderItemException{
        log.debug("Request to save new order : {}", orderDto);

        //get connected user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());

        Timestamp createdDate =new Timestamp(1);
        Orders orders= new Orders();
        orders.setOrderId(orderDto.getId());
        orders.setOrderRef("CMDE "+createdDate +" ");
        orders.setCreatedDate(createdDate);
        orders.setDeleted(false);
        //set to connected user
        orders.setUser(user);
        for (OrderedProduitDto orderedProduitDto:orderDto.getProducts()){
            Produit produit = produitRepository.findByProduitId(orderedProduitDto.getProduitId());
            if (produit.getMetrage() - orderedProduitDto.getMetrage() <= 0){
                throw new RuntimeException("Produit " + orderedProduitDto.getName() + " insuffisant  en Stock!");
            }
        }

        Orders result =ordersRepository.save(orders);
        if (orderDto.getProducts() != null){
            for(OrderedProduitDto orderedProductDto: orderDto.getProducts()){
                if(orderedProductDto != null){
                    orderedProductDto.setOrderId(result.getOrderId());
                    orderedProductService.save(orderedProductDto);
                }
            }
        }
        ordersRepository.save(result);
        return result;
    }

}
