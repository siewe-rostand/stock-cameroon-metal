package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.dto.CustomerDto;
import com.siewe.inventorymanagementsystem.service.CustomerService;
import com.siewe.inventorymanagementsystem.utils.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class CustomerController {

    private final Logger log = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;
    /**
     * POST  /customers : Create a new customer.
     *
     * @param customerDto the customer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customer, or with status 400 (Bad Request) if the customer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customers")
    public ResponseEntity<Object> createCustomer(@Valid @RequestBody CustomerDto customerDto) throws URISyntaxException {
        log.debug("REST request to save Customer : {}", customerDto);
        if (customerDto.getId() != null) {
            return new ResponseEntity<>(new CustomErrorType("Unable to create. A customer with id " +
                    customerDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<Object>(new CustomerDto().createDTO(customerService.save(customerDto)), HttpStatus.CREATED);
    }

    /**
     * PUT  /customers : Updates an existing customer.
     *
     * @param customerDto the customer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customer,
     * or with status 400 (Bad Request) if the customer is not valid,
     * or with status 500 (Internal Server Error) if the customer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/customers")
    public ResponseEntity<Object> updateCustomer(@Valid @RequestBody CustomerDto customerDto) throws URISyntaxException {
        log.debug("REST request to update Customer : {}", customerDto);
        if (customerDto.getId() == null) {
            return createCustomer(customerDto);
        }
        return customerService.update(customerDto);
    }


    /**
     * GET  /customers : get all the customers.
     */
    @GetMapping("/customers")
    public Page<CustomerDto> getAllCustomers(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                             @RequestParam(name = "size", defaultValue = "999999") Integer size,
                                             @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                             @RequestParam(name = "direction", defaultValue = "desc") String direction,
                                             @RequestParam(name = "name", defaultValue = "") String name) {
        log.debug("REST request to get Customers by customer");
        return customerService.findAll(page, size, sortBy, direction, name);
    }

    @GetMapping("/customers-search")
    public Map<String, List<CustomerDto>> getAllCustomers(@RequestParam(name = "mc") String mc) {
        log.debug("REST request to get Customers");
        Map<String, List<CustomerDto>> map = new HashMap<>();
        map.put("results", customerService.findByMc(mc));
        return map;
    }


    /**
     * GET  /customers/:id : get the "id" customer.
     *
     * @param id the id of the customer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customer, or with status 404 (Not Found)
     */
    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id) {
        log.debug("REST request to get Customer : {}", id);
        CustomerDto customerDto = customerService.findOne(id);
        return Optional.ofNullable(customerDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * DELETE  /customers/:id : delete the "id" customer.
     *
     * @param id the id of the customer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        log.debug("REST request to delete Customer : {}", id);
        customerService.delete(id);
        return new ResponseEntity<CustomerDto>(HttpStatus.NO_CONTENT);
    }
}