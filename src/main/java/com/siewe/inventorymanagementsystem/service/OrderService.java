package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.OrderDto;
import com.siewe.inventorymanagementsystem.dto.OrderedProductDto;
import com.siewe.inventorymanagementsystem.model.Order;
import com.siewe.inventorymanagementsystem.model.User;
import com.siewe.inventorymanagementsystem.model.error.EntityNotFoundException;
import com.siewe.inventorymanagementsystem.repository.OrderedProductRepository;
import com.siewe.inventorymanagementsystem.repository.OrdersRepository;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.utils.InvalidOrderItemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private UserService customerService;

    @Autowired
    private OrderedProductService orderedProductService;

    @Autowired
    private OrderedProductRepository orderedProductRepository;

    private Order localSave(OrderDto orderDto,boolean update){
        Order order = new Order();
        if (!update){
            order.setId(orderDto.getId());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime date = LocalDateTime.now();
            order.setCreatedDate(date.format(formatter));
        }else {
            order.setId(orderDto.getId());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime date = LocalDateTime.now();
            order.setUpdatedDate(date.format(formatter));
        }

        if (orderDto.getCustomerId() != null){
            User customer = userRepository.findByUserId(orderDto.getCustomerId());
            if (customer ==null){
                throw  new EntityNotFoundException(User.class,"id",orderDto.getCustomerId().toString());
            }
            customerService.update(orderDto.getCustomer());
            order.setCustomer(customer);
        }

        return ordersRepository.save(order);
    }

    public OrderDto save(OrderDto orderDto){
        log.debug("Request to save orders : {}", orderDto);


        Order result =localSave(orderDto,false);
        if (orderDto.getOrderedProducts() != null){
            for(OrderedProductDto orderedProductDto: orderDto.getOrderedProducts()){
                if(orderedProductDto != null){
                    orderedProductDto.setVenteId(result.getId());
                    orderedProductService.save(orderedProductDto);
                }
            }
        }

        return new OrderDto().CreateDto(result);
    }

    public OrderDto update(OrderDto orderDto) throws InvalidOrderItemException {
        log.debug("update order: {}", orderDto);

        Order order = ordersRepository.findOne(orderDto.getId());

        if (order != null){
          order=  localSave(orderDto,true);
        }else {
            throw new EntityNotFoundException(Order.class,"id",orderDto.getId().toString());
        }
        return new OrderDto().CreateDto(order);
    }

    public OrderDto findOne(Long id){
        log.debug("request to get order by id : {}",id);

        Order order = ordersRepository.findOne(id);
        if (order == null){
            throw new EntityNotFoundException(Order.class,"id",id.toString());
        }
        return new OrderDto().CreateDto(order);
    }

    public String delete(Long id){
        log.debug("Request to delete an order by it's id : {}",id);

        Order order = ordersRepository.findOne(id);
        if (order == null){
            throw new EntityNotFoundException(Order.class,"id",id.toString());
        }
        if (order.getOrderedProducts() != null){
            order.setDeleted(true);
            ordersRepository.save(order);
        }else {
            ordersRepository.deleteById(id);
        }

        return "Order with id " +id.toString()+ " deleted successfully";

    }


}
