package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.CustomerDto;
import com.siewe.inventorymanagementsystem.dto.OrderDto;
import com.siewe.inventorymanagementsystem.dto.OrderedProductDto;
import com.siewe.inventorymanagementsystem.dto.ProduitDto;
import com.siewe.inventorymanagementsystem.exceptions.OutOfStockException;
import com.siewe.inventorymanagementsystem.model.*;
import com.siewe.inventorymanagementsystem.model.error.EntityNotFoundException;
import com.siewe.inventorymanagementsystem.repository.*;
import com.siewe.inventorymanagementsystem.utils.CustomErrorType;
import com.siewe.inventorymanagementsystem.utils.InvalidOrderException;
import com.siewe.inventorymanagementsystem.utils.InvalidOrderItemException;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProduitService produitService;

    @Autowired
    private ProduitRepository produitRepository;

    public Orders save(OrderDto orderDto) throws InvalidOrderItemException{
        log.debug("Request to save new order : {}", orderDto);

        //get connected user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());

        Orders orders= new Orders();
        orders.setOrderId(orderDto.getId());
        orders.setOrderRef(orderDto.getOrderRef());
        orders.setDeleted(false);
        //set to connected user
        orders.setUser(user);
        for (ProduitDto produitDto:orderDto.getProducts()){
            Produit produit = produitRepository.findByProduitId(produitDto.getId());
            if (produit.getMetrage() <= 0){
                produit.setAvailable(false);
                throw new RuntimeException("Produit " + produitDto.getName() + " insuffisant  en Stock!");
            }
        }

        return ordersRepository.save(orders);
    }

}
