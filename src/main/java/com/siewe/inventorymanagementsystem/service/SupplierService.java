package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.SupplierDto;
import com.siewe.inventorymanagementsystem.model.Supplier;
import com.siewe.inventorymanagementsystem.repository.SupplierRepository;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Supplier.
 */
@Service
@Transactional
public class SupplierService {
    private final Logger log = LoggerFactory.getLogger(SupplierService.class);

    @Autowired
    private SupplierRepository supplierRepository;

    /**
     * Save a supplier.
     *
     * @param supplierDto the entity to save
     * @return the persisted entity
     */
    public Supplier save(SupplierDto supplierDto) {
        log.debug("Request to save Supplier : {}", supplierDto);

        Supplier supplier = new Supplier();

        supplier.setId(supplierDto.getId());
        supplier.setName(supplierDto.getName());
        supplier.setPhone(supplierDto.getPhone());
        supplier.setAddress(supplierDto.getAddress());

        String pattern = "yyyy-MM-dd";
        LocalDateTime datetime = new LocalDateTime(DateTimeZone.forOffsetHours(1));
        supplier.setCreatedDate(datetime.toString(pattern));

        return supplierRepository.save(supplier);
    }


    public ResponseEntity<SupplierDto> update(SupplierDto supplierDto) {
        log.debug("Request to save Supplier : {}", supplierDto);

        Supplier supplier = supplierRepository.findOne(supplierDto.getId());

        supplier.setId(supplierDto.getId());
        supplier.setName(supplierDto.getName());
        supplier.setPhone(supplierDto.getPhone());
        supplier.setAddress(supplierDto.getAddress());

        Supplier result = supplierRepository.save(supplier);
        return new ResponseEntity<SupplierDto>(new SupplierDto().createDTO(result), HttpStatus.CREATED);
    }


    /**
     *  Get all the suppliers.
     *
     *  @return the list of entities
     */

    @Transactional(readOnly = true)
    public Page<SupplierDto> findAll(Integer page, Integer size, String sortBy, String direction, String name) {
        log.debug("Request to get all Suppliers");

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Page<Supplier> suppliers = supplierRepository.findAll("%"+name+"%", pageable);

        return suppliers.map(supplier -> new SupplierDto().createDTO(supplier));
    }

    public List<SupplierDto> findByMc(String mc) {

        List<Supplier> suppliers = supplierRepository.findByMc("%"+mc+"%");
        List<SupplierDto> supplierDtos = new ArrayList<>();

        for (Supplier supplier : suppliers)
            supplierDtos.add(new SupplierDto().createDTO(supplier));

        return supplierDtos;
    }

    /**
     *  Get one supplier by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public SupplierDto findOne(Long id) {
        log.debug("Request to get Supplier : {}", id);
        Supplier supplier = supplierRepository.findOne(id);
        return new SupplierDto().createDTO(supplier);
    }

    /**
     *  Delete the  supplier by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Supplier : {}", id);
        Supplier supplier = supplierRepository.findOne(id);
        if(Optional.ofNullable(supplier).isPresent()){
            supplierRepository.deleteById(id);
        }
    }
}
