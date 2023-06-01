//package com.siewe.inventorymanagementsystem.controller;
//
//import com.siewe.inventorymanagementsystem.dto.OrderDto;
//import com.siewe.inventorymanagementsystem.model.Order;
//import com.siewe.inventorymanagementsystem.repository.UserRepository;
//import com.siewe.inventorymanagementsystem.service.OrderService;
//import com.siewe.inventorymanagementsystem.service.OrderedProductService;
//import com.siewe.inventorymanagementsystem.utils.CustomErrorType;
//import com.siewe.inventorymanagementsystem.utils.ResponseHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@CrossOrigin
//@RestController
//@RequestMapping("/api")
//public class OrderController {
//    private final Logger log = LoggerFactory.getLogger(OrderController.class);
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private OrderedProductService orderedProductService;
//
//    @PostMapping("/orders")
//    public ResponseEntity<Object> createOrder(@RequestBody OrderDto orderDto) throws CustomErrorType {
//        log.debug("REST request to create new order : {}",orderDto);
//
////        orderDto.setCustomerId(userRepository.findByUsername());
//        OrderDto order = orderService.save(orderDto);
//
//        return ResponseHandler.generateResponse("Order created successfully",HttpStatus.CREATED,order);
//    }
//
//    @GetMapping("/orders")
//    public Page<OrderDto> getAllOrders(@RequestParam(name = "page", defaultValue = "0") Integer page,
//                                    @RequestParam(name = "size", defaultValue = "999999") Integer size,
//                                    @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
//                                    @RequestParam(name = "direction", defaultValue = "desc") String direction){
//        log.debug(" controller function to get all orders");
//        return  orderService.findAll(page,size,sortBy,direction);
//    }
//
////    @PostMapping("/orders")
////    public ResponseEntity<Order> createOrder1(@RequestBody OrderDto orderDto) {
////        try {
////            Order newOrder = orderService.createOrder1(orderDto);
////            return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
////        } catch (IllegalArgumentException | CustomErrorType e) {
////            return ResponseEntity.badRequest().build();
////        }
////    }
//}
