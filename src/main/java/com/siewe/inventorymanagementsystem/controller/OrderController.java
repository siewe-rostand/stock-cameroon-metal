package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.dto.OrderDto;
import com.siewe.inventorymanagementsystem.model.Orders;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.security.SecurityUtils;
import com.siewe.inventorymanagementsystem.service.OrderService;
import com.siewe.inventorymanagementsystem.service.OrderedProductService;
import com.siewe.inventorymanagementsystem.utils.CustomErrorType;
import com.siewe.inventorymanagementsystem.utils.InvalidOrderItemException;
import com.siewe.inventorymanagementsystem.utils.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class OrderController {
    private final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }


    @PostMapping("/orders")
    public ResponseEntity<Object> createOrder(@RequestBody OrderDto orderDto) throws URISyntaxException {
        log.debug("REST request to create new order : {}",orderDto);
        //automatically set user to current user
        orderDto.setCreatedUser(userRepository.findByEmail(SecurityUtils.getCurrentUserLogin()).getFullName());
        Orders result = null;
        try {
            result = orderService.save(orderDto);
        } catch (InvalidOrderItemException e) {
            //return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
            return new ResponseEntity<>(new CustomErrorType(e.getMessage()), HttpStatus.CONFLICT);
        }

        return ResponseHandler.generateResponse("Order created successfully",HttpStatus.CREATED,orderDto.CreateDto(result));
    }

//    @GetMapping("/orders")
//    public Page<OrderDto> getAllOrders(@RequestParam(name = "page", defaultValue = "0") Integer page,
//                                    @RequestParam(name = "size", defaultValue = "999999") Integer size,
//                                    @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
//                                    @RequestParam(name = "direction", defaultValue = "desc") String direction){
//        log.debug(" controller function to get all orders");
//        return  orderService.findAll(page,size,sortBy,direction);
//    }

//    @PostMapping("/orders")
//    public ResponseEntity<Order> createOrder1(@RequestBody OrderDto orderDto) {
//        try {
//            Order newOrder = orderService.createOrder1(orderDto);
//            return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
//        } catch (IllegalArgumentException | CustomErrorType e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
}
