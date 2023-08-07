package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.dto.OrderDto;
import com.siewe.inventorymanagementsystem.dto.OrderedProduitDto;
import com.siewe.inventorymanagementsystem.dto.PdfGenerator;
import com.siewe.inventorymanagementsystem.dto.UserDto;
import com.siewe.inventorymanagementsystem.model.OrderedProduit;
import com.siewe.inventorymanagementsystem.model.Orders;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.security.SecurityUtils;
import com.siewe.inventorymanagementsystem.service.OrderService;
import com.siewe.inventorymanagementsystem.service.OrderedProductService;
import com.siewe.inventorymanagementsystem.service.OrderedProduitService;
import com.siewe.inventorymanagementsystem.utils.CustomErrorType;
import com.siewe.inventorymanagementsystem.utils.InvalidOrderItemException;
import com.siewe.inventorymanagementsystem.utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {
    private final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    private final UserRepository userRepository;

    private final OrderedProduitService produitService;


    @GetMapping(value = "/generate-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generatePdf() throws IOException {
        byte[] pdfBytes = PdfGenerator.generatePdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "example.pdf");

        return new ResponseEntity<>(pdfBytes, headers, 200);
    }

    @PostMapping("/orders/create")
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
    @GetMapping("/orders/{id}")
    public ResponseEntity<Object> getOrder(@PathVariable Long id) {
        log.debug("REST request to get order by : {}", id);
        HashMap<String, String> error = new HashMap<>();
        OrderDto orderDto = orderService.findOne(id);
        if (orderDto == null){
            error.put("error","No order found with Id :: "+id);
            return new  ResponseEntity<Object>(error,HttpStatus.NOT_FOUND);
        }else {
            return ResponseHandler.generateResponse("Order gotten successfully",HttpStatus.OK,orderDto);
        }
    }

    @GetMapping("/orderedProd/{id}")
    public ResponseEntity<Object> getOrderedProducts(@PathVariable Integer id){
        OrderedProduitDto produitDto = produitService.findById(id);
        return ResponseHandler.generateResponse("get ordered products",HttpStatus.OK,produitDto);
    }

    @GetMapping("/orders")
    public Page<OrderDto> getAllUsers(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                     @RequestParam(name = "size", defaultValue = "5") Integer size,
                                     @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                     @RequestParam(name = "direction", defaultValue = "desc") String direction
    ) {
        log.debug("REST request to get orders");
        return orderService.findAll(page, size, sortBy, direction);
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
