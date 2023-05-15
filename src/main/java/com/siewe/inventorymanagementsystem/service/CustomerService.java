package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.CustomerDto;
import com.siewe.inventorymanagementsystem.model.Customer;
import com.siewe.inventorymanagementsystem.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {
    private final Logger log = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;


    /**
     * Save a customer.
     *
     * @param customerDto the entity to save
     * @return the persisted entity
     */
    public Customer save(CustomerDto customerDto) {
        log.debug("Request to save Customer : {}", customerDto);

        Customer customer = new Customer();

        customer.setId(customerDto.getId());
        customer.setName(customerDto.getName());
        customer.setPhone(customerDto.getPhone());
        customer.setAddress(customerDto.getAddress());
        customer.setCity(customerDto.getCity());
        customer.setQuarter(customerDto.getQuarter());
        customer.setPhone2(customerDto.getPhone2());

        Calendar cal = Calendar.getInstance();
        customer.setCreatedDate(new Timestamp(cal.getTimeInMillis()));


        return customerRepository.save(customer);
    }


    public ResponseEntity<Object> update(CustomerDto customerDto) {
        log.debug("Request to save Customer : {}", customerDto);

        Customer customer = customerRepository.findOne(customerDto.getId());

        customer.setId(customerDto.getId());
        customer.setName(customerDto.getName());
        customer.setPhone(customerDto.getPhone());
        customer.setPhone2(customerDto.getPhone2());
        customer.setAddress(customerDto.getAddress());
        customer.setCity(customerDto.getCity());
        customer.setQuarter(customerDto.getQuarter());

        Customer result = customerRepository.save(customer);
        return new ResponseEntity<Object>(new CustomerDto().createDTO(result), HttpStatus.CREATED);
    }


    /**
     *  Get all the customers.
     *
     *  @return the list of entities
     */

    @Transactional(readOnly = true)
    public Page<CustomerDto> findAll(Integer page, Integer size, String sortBy, String direction, String name) {
        log.debug("Request to get all Customers");

        Pageable pageable =  PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Page<Customer> customers = customerRepository.findAll("%"+name+"%", pageable);

        Page<CustomerDto> customerDtos = customers.map(customer -> new CustomerDto().createDTO(customer));
        return customerDtos;
    }

    public List<CustomerDto> findByMc(String mc) {

        List<Customer> customers = customerRepository.findByMc("%"+mc+"%");
        List<CustomerDto> customerDtos = new ArrayList<>();

        for (Customer customer : customers)
            customerDtos.add(new CustomerDto().createDTO(customer));

        return customerDtos;
    }


    /**
     *  Get one customer by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CustomerDto findOne(Long id) {
        log.debug("Request to get Customer : {}", id);
        Customer customer = customerRepository.findOne(id);
        return new CustomerDto().createDTO(customer);
    }

    /**
     *  Delete the  customer by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Customer : {}", id);
        Customer customer = customerRepository.findOne(id);
        if(Optional.ofNullable(customer).isPresent()){
            customerRepository.deleteById(id);
        }
    }
}
