package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.OrderDto;
import com.siewe.inventorymanagementsystem.dto.OrderedProductDto;
import com.siewe.inventorymanagementsystem.exceptions.OutOfStockException;
import com.siewe.inventorymanagementsystem.model.Order;
import com.siewe.inventorymanagementsystem.model.OrderedProduct;
import com.siewe.inventorymanagementsystem.model.Product;
import com.siewe.inventorymanagementsystem.model.User;
import com.siewe.inventorymanagementsystem.model.error.EntityNotFoundException;
import com.siewe.inventorymanagementsystem.repository.OrderedProductRepository;
import com.siewe.inventorymanagementsystem.repository.OrdersRepository;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.utils.CustomErrorType;
import com.siewe.inventorymanagementsystem.utils.InvalidOrderException;
import com.siewe.inventorymanagementsystem.utils.InvalidOrderItemException;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserService customerService;

    @Autowired
    private OrderedProductService orderedProductService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderedProductRepository orderedProductRepository;

    private Order localSave(OrderDto orderDto,boolean update) throws CustomErrorType {
        Order order = new Order();
        if (!update){
            order.setDeleted(false);

            String pattern = "yyyy-MM-dd HH:mm";
            LocalDateTime datetime = new LocalDateTime(DateTimeZone.forOffsetHours(1));
            order.setCreatedDate(datetime.toString(pattern));

        }else {

            String pattern = "yyyy-MM-dd HH:mm";
            LocalDateTime datetime = new LocalDateTime(DateTimeZone.forOffsetHours(1));
            order.setUpdatedDate(datetime.toString(pattern));
        }

        if (orderDto.getCustomerId() != null){
            User customer = userRepository.findByUserId(orderDto.getCustomer().getId());
            if (customer ==null){
                throw  new CustomErrorType("Unable to create. A user with username " +
                        orderDto.getCustomer().getId().toString() + " already exist.");
            }
            customerService.update(orderDto.getCustomer());
            order.setCustomer(customer);
        }

        return ordersRepository.save(order);
    }

    public OrderDto save(OrderDto orderDto) throws CustomErrorType {
        log.debug("Request to save orders : {}", orderDto);


        Order result =localSave(orderDto,false);
        List<OrderedProduct> orderedProducts = new ArrayList<>();
        if (orderDto.getOrderedProducts() != null){
            for (OrderedProductDto productDto : orderDto.getOrderedProducts()){
                productDto.setOrderId(result.getId());
                OrderedProduct orderedProduct =  orderedProductService.createOrderItem(productDto);
                orderedProducts.add(orderedProduct);
            }
            result.setOrderedProducts(orderedProducts);
        }

        return new OrderDto().CreateDto(result);
    }

    public Order getOrder(Long orderId) {
        return ordersRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(Order.class,"Order with ID " + orderId + " not found"));
    }
    public void createOrder(OrderDto orderDto) throws OutOfStockException{
        log.info("order dto class {}",orderDto);
        validateOrderItems(orderDto.getOrderedProducts());
        List<OrderedProduct> orderedProducts = orderDto.getOrderedProducts().stream().map(orderedProductDto -> new OrderedProduct(productService.getProduct(orderedProductDto.getProductId()),
                orderedProductDto.getQuantity())).collect(Collectors.toList());

        Order newOrder = new Order(orderedProducts);
        ordersRepository.save(newOrder);

        //update product inventory levels
        updateProductInventoryLevels(orderDto.getOrderedProducts(),false);
    }

    public Order createOrder1(OrderDto orderDto) throws CustomErrorType {
        log.debug("create order from new method {}",orderDto);
        Order order = new Order();
        order.setDeleted(false);

        String pattern = "yyyy-MM-dd HH:mm";
        LocalDateTime datetime = new LocalDateTime(DateTimeZone.forOffsetHours(1));
        order.setCreatedDate(datetime.toString(pattern));
        Order result = ordersRepository.save(order);
        //create customer
        if (orderDto.getCustomerId() != null){
            User customer = userRepository.findByUserId(orderDto.getCustomer().getId());
            if (customer ==null){
                throw  new CustomErrorType("Unable to create. A user with username " +
                        orderDto.getCustomer().getId().toString() + " already exist.");
            }
            customerService.update(orderDto.getCustomer());
            result.setCustomer(customer);
        }

        //create new list of order products
        List<OrderedProduct> orderedProducts = new ArrayList<>();
        for (OrderedProductDto productDto : orderDto.getOrderedProducts()){
            OrderedProduct orderedProduct =  new OrderedProduct();
            productDto.setOrderId(result.getId());
            orderedProducts.add(orderedProduct);
        }
        order.setOrderedProducts(orderedProducts);

        return ordersRepository.save(order);
    }

//    public void updateOrder(Long orderId, OrderDto orderDTO) throws EntityNotFoundException,OutOfStockException{
//        Order order = getOrder(orderId);
//        List<OrderedProduct> orderItems = new ArrayList<>();
//        BigDecimal totalAmount = BigDecimal.ZERO;
//        for (OrderedProductDto orderItemDTO : orderDTO.getOrderedProducts()) {
//            OrderedProduct orderItem = orderedProductService.createOrderItem(orderItemDTO);
//            orderItem.setOrder(order);
//            orderItems.add(orderItem);
//            totalAmount = totalAmount.add(orderItem.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
//        }
//        order.setOrderedProducts(orderItems);
//        order.setTotalPrice(totalAmount.doubleValue());
//        order = ordersRepository.save(order);
//
//        // Update product inventory levels
//        for (OrderedProduct orderItem : orderItems) {
//            Product product = orderItem.getProduct();
//            int quantity = orderItem.getQuantity().intValue();
//            product.setQuantity(product.getQuantity() - quantity);
//            productService.updateProduct(product);
//        }
//
//    }

    public OrderDto update(OrderDto orderDto) throws InvalidOrderItemException, CustomErrorType {
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


    public void validateOrderItems1(Order order) throws InvalidOrderException {
        List<OrderedProduct> orderItems = order.getOrderedProducts();

        if (orderItems == null || orderItems.isEmpty()) {
            throw new InvalidOrderException("Order has no items.");
        }

        for (OrderedProduct item : orderItems) {
            Product product = item.getProduct();

            if (product == null) {
                throw new InvalidOrderException("Order item has no associated product.");
            }

            int orderedQuantity = item.getQuantity().intValue();

            if (orderedQuantity <= 0) {
                throw new InvalidOrderException("Order item has no valid order quantity.");
            }

            int availableQuantity = product.getQuantity().intValue();

            if (availableQuantity < orderedQuantity) {
                throw new InvalidOrderException("Order item has more quantity than available in inventory.");
            }
        }
    }

    private void validateOrderItems(List<OrderedProductDto> items) throws OutOfStockException {
        for (OrderedProductDto itemRequest : items) {
            Product product = productService.getProduct(itemRequest.getProductId());
            int requestedQuantity = itemRequest.getQuantity().intValue();
            if (product.getQuantity() < requestedQuantity) {
                throw new RuntimeException("Product " + product.getName() + " is out of stock");
            }
        }
    }

    private void updateProductInventoryLevels(List<OrderedProductDto> items, boolean isRestock) {
        for (OrderedProductDto item : items) {
            Product product = productService.getProduct(item.getProductId());
            int quantity = item.getQuantity().intValue();
            if (isRestock) {
                product.setQuantity(product.getQuantity() + quantity);
            } else {
                product.setQuantity(product.getQuantity() - quantity);
            }
            productService.updateProduct(product);
        }
    }
}
