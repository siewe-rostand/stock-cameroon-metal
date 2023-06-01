package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.dto.SupplierDto;
import com.siewe.inventorymanagementsystem.service.SupplierService;
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

/**
 * REST controller for managing Supplier.
 */
@CrossOrigin
@RestController
public class SupplierController {
    private final Logger log = LoggerFactory.getLogger(SupplierController.class);

    @Autowired
    private SupplierService supplierService;

    /**
     * POST  /suppliers : Create a new supplier.
     *
     * @param supplierDto the supplier to create
     * @return the ResponseEntity with status 201 (Created) and with body the new supplier, or with status 400 (Bad Request) if the supplier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/suppliers")
    public ResponseEntity<SupplierDto> createSupplier(@Valid @RequestBody SupplierDto supplierDto) throws URISyntaxException {
        log.debug("REST request to save Supplier : {}", supplierDto);
        if (supplierDto.getId() != null){
            return new ResponseEntity(new CustomErrorType("Unable to create. A supplier with id " +
                    supplierDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<SupplierDto>(new SupplierDto().createDTO(supplierService.save(supplierDto)), HttpStatus.CREATED);
    }

    /**
     * PUT  /suppliers : Updates an existing supplier.
     *
     * @param supplierDto the supplier to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated supplier,
     * or with status 400 (Bad Request) if the supplier is not valid,
     * or with status 500 (Internal Server Error) if the supplier couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/api/suppliers")
    public ResponseEntity<SupplierDto> updateSupplier(@Valid @RequestBody SupplierDto supplierDto) throws URISyntaxException {
        log.debug("REST request to update Supplier : {}", supplierDto);
        if (supplierDto.getId() == null) {
            return createSupplier(supplierDto);
        }
        return supplierService.update(supplierDto);
    }

    /**
     * GET  /suppliers : get all the suppliers.
     */
    @GetMapping("/api/suppliers")
    public Page<SupplierDto> getAllSuppliers(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                             @RequestParam(name = "size", defaultValue = "999999") Integer size,
                                             @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                             @RequestParam(name = "direction", defaultValue = "desc") String direction,
                                             @RequestParam(name = "name", defaultValue = "") String name) {
        log.debug("REST request to get Suppliers by supplier");
        return supplierService.findAll(page, size, sortBy, direction, name);
    }

    @GetMapping("/api/suppliers-search")
    public Map<String, List<SupplierDto>> getAllSuppliers(@RequestParam(name = "mc") String mc) {
        log.debug("REST request to get Suppliers");
        Map<String, List<SupplierDto>> map = new HashMap<>();
        map.put("results", supplierService.findByMc(mc));
        return map;
    }


    /**
     * GET  /suppliers/:id : get the "id" supplier.
     *
     * @param id the id of the supplier to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the supplier, or with status 404 (Not Found)
     */
    @GetMapping("/api/suppliers/{id}")
    public ResponseEntity<SupplierDto> getSupplier(@PathVariable Long id) {
        log.debug("REST request to get Supplier : {}", id);
        SupplierDto supplierDto = supplierService.findOne(id);
        return Optional.ofNullable(supplierDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * DELETE  /suppliers/:id : delete the "id" supplier.
     *
     * @param id the id of the supplier to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/api/suppliers/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id) {
        log.debug("REST request to delete Supplier : {}", id);
        supplierService.delete(id);
        return new ResponseEntity<SupplierDto>(HttpStatus.NO_CONTENT);
    }
}

