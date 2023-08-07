package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.*;
import com.siewe.inventorymanagementsystem.model.Customer;
import com.siewe.inventorymanagementsystem.model.Orders;
import com.siewe.inventorymanagementsystem.model.Produit;
import com.siewe.inventorymanagementsystem.model.User;
import com.siewe.inventorymanagementsystem.repository.CustomerRepository;
import com.siewe.inventorymanagementsystem.repository.OrdersRepository;
import com.siewe.inventorymanagementsystem.repository.ProduitRepository;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.security.SecurityUtils;
import com.siewe.inventorymanagementsystem.utils.InvalidOrderItemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderedProduitService orderedProductService;

    @Autowired
    private ProduitRepository produitRepository;


    public Orders save(OrderDto orderDto) throws InvalidOrderItemException{
        log.debug("Request to save new order : {}", orderDto);

        //get connected user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(SecurityUtils.getCurrentUserLogin());

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

        //set customer
        if (orderDto.getCustomerId() != null){
            Customer customer = customerRepository.findOne(orderDto.getCustomerId());
            if (customer != null){
                orders.setCustomer(customer);
            }else {
                throw new RuntimeException("customer with Id "+orderDto.getCustomerId() + " do not exist");
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
        return ordersRepository.save(result);
    }

    @Transactional(readOnly = true)
    public OrderDto findOne(Long id) {
        log.debug("Request to get order by id : {}", id);
        Orders orders = ordersRepository.findOne(id);
        return new OrderDto().CreateDto(orders);
    }

    public Page<OrderDto> findAll(Integer page, Integer size, String sortBy, String direction) {
        log.debug("Request to get all Users");

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);

        //Page<User> users = userRepository.findByRole("USER", pageable);
        Page<Orders> orders = ordersRepository.findAll( pageable);

        return orders.map(order -> new OrderDto().CreateDto(order));
    }

}
