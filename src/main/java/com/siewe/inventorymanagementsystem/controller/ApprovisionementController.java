package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.dto.ApprovisionnementDto;
import com.siewe.inventorymanagementsystem.repository.ApprovisionnementRepository;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.service.ApprovisionnementService;
import com.siewe.inventorymanagementsystem.utils.CustomErrorType;
import com.siewe.inventorymanagementsystem.utils.InvalidActionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Approvisionnement.
 */
@CrossOrigin
@RestController
public class ApprovisionementController {

    private final Logger log = LoggerFactory.getLogger(ApprovisionementController.class);

    @Autowired
    private ApprovisionnementService approvisionnementService;

    @Autowired
    private UserRepository userRepository;


    /**
     * POST  /approvisionnements : Create a new approvisionnement.
     *
     * @param approvisionnementDto the approvisionnement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new approvisionnement, or with status 400 (Bad Request) if the approvisionnement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/approvisionnements")
    public ResponseEntity<ApprovisionnementDto> createApprovisionnement(@Valid @RequestBody ApprovisionnementDto approvisionnementDto) throws URISyntaxException {
        log.debug("REST request to save Approvisionnement : {}", approvisionnementDto);
        if (approvisionnementDto.getId() != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A approvisionnement with id " +
                    approvisionnementDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }
        //automatically set user to current user
        //approvisionnementDto.setUserId(userRepository.findByLogin(SecurityUtils.getCurrentUserLogin()).getId());
        return approvisionnementService.save(approvisionnementDto);
    }

    /**
     * PUT  /approvisionnements : Updates an existing approvisionnement.
     *
     * @param approvisionnementDto the approvisionnement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated approvisionnement,
     * or with status 400 (Bad Request) if the approvisionnement is not valid,
     * or with status 500 (Internal Server Error) if the approvisionnement couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/api/approvisionnements")
    public ResponseEntity<ApprovisionnementDto> updateApprovisionnement(@Valid @RequestBody ApprovisionnementDto approvisionnementDto) throws URISyntaxException {
        log.debug("REST request to update Approvisionnement : {}", approvisionnementDto);
        if (approvisionnementDto.getId() == null) {
            return createApprovisionnement(approvisionnementDto);
        }
        return approvisionnementService.update(approvisionnementDto);
    }


    /**
     * GET  /approvisionnements : get all the approvisionnements.
     */
    @GetMapping("/api/approvisionnements-by-store/{storeId}")
    public Page<ApprovisionnementDto> getByStore(@PathVariable Long storeId,
                                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                 @RequestParam(name = "size", defaultValue = "999999") Integer size,
                                                 @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                                 @RequestParam(name = "direction", defaultValue = "desc") String direction,
                                                 @RequestParam(name = "createdDateFrom") String createdDateFrom,
                                                 @RequestParam(name = "createdDateTo") String createdDateTo,
                                                 @RequestParam(name = "productId", defaultValue = "0") Long productId) {
        log.debug("REST request to get Approvisionnements by store");
        return approvisionnementService.findByStoreId(page, size, sortBy, direction, storeId, createdDateFrom, createdDateTo, productId);
    }
    @GetMapping("/api/approvisionnements-by-product/{storeId}")
    public List<ApprovisionnementRepository.ProductApprovisionnements> findProductApprovisionnements(@PathVariable Long storeId,
                                                                                                     @RequestParam(name = "createdDateFrom") String createdDateFrom,
                                                                                                     @RequestParam(name = "createdDateTo") String createdDateTo,
                                                                                                     @RequestParam(name = "productId", defaultValue = "0") Long productId) {
        log.debug("REST request to get Approvisionnements by product");
        return approvisionnementService.findProductApprovisionnementsByDateRange(storeId, createdDateFrom, createdDateTo, productId);
    }
    /**
     * GET  /approvisionnements/:id : get the "id" approvisionnement.
     *
     * @param id the id of the approvisionnement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the approvisionnement, or with status 404 (Not Found)
     */
    @GetMapping("/api/approvisionnements/{id}")
    public ResponseEntity<ApprovisionnementDto> getApprovisionnement(@PathVariable Long id) {
        log.debug("REST request to get Approvisionnement : {}", id);
        ApprovisionnementDto approvisionnementDto = approvisionnementService.findOne(id);
        return Optional.ofNullable(approvisionnementDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * DELETE  /approvisionnements/:id : delete the "id" approvisionnement.
     *
     * @param id the id of the approvisionnement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/api/approvisionnements/{id}")
    public ResponseEntity<?> deleteApprovisionnement(@PathVariable Long id) throws InvalidActionException {
        log.debug("REST request to delete Approvisionnement : {}", id);
        return approvisionnementService.delete(id);
        //return new ResponseEntity<ApprovisionnementDto>(HttpStatus.NO_CONTENT);
    }
}

