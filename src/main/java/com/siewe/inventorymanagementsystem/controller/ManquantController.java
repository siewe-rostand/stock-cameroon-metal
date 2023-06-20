package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.dto.ManquantDto;
import com.siewe.inventorymanagementsystem.repository.ManquantRepository;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.service.ManquantService;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Manquant.
 */
@CrossOrigin
@RestController
public class ManquantController {
    private final Logger log = LoggerFactory.getLogger(ManquantController.class);

    @Autowired
    private ManquantService manquantService;

    @Autowired
    private UserRepository userRepository;

    /**
     * POST  /manquants : Create a new manquant.
     *
     * @param manquantDto the manquant to create
     * @return the ResponseEntity with status 201 (Created) and with body the new manquant, or with status 400 (Bad Request) if the manquant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/manquants")
    public ResponseEntity<ManquantDto> createManquant(@Valid @RequestBody ManquantDto manquantDto) throws URISyntaxException {
        log.debug("REST request to save Manquant : {}", manquantDto);
        if (manquantDto.getId() != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A manquant with id " +
                    manquantDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }
        //automatically set user to current user
        //manquantDto.setUserId(userRepository.findByLogin(SecurityUtils.getCurrentUserLogin()).getId());
        return manquantService.save(manquantDto);
    }

    /**
     * PUT  /manquants : Updates an existing manquant.
     *
     * @param manquantDto the manquant to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated manquant,
     * or with status 400 (Bad Request) if the manquant is not valid,
     * or with status 500 (Internal Server Error) if the manquant couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/api/manquants")
    public ResponseEntity<ManquantDto> updateManquant(@Valid @RequestBody ManquantDto manquantDto) throws URISyntaxException {
        log.debug("REST request to update Manquant : {}", manquantDto);
        if (manquantDto.getId() == null) {
            return createManquant(manquantDto);
        }
        return manquantService.update(manquantDto);
    }


    /**
     * GET  /manquants : get all the manquants.
     */
    @GetMapping("/api/manquants-by-store/{storeId}")
    public Page<ManquantDto> getByStore(
                                        @RequestParam(name = "page", defaultValue = "0") Integer page,
                                        @RequestParam(name = "size", defaultValue = "999999") Integer size,
                                        @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                        @RequestParam(name = "direction", defaultValue = "desc") String direction,
                                        @RequestParam(name = "createdDateFrom") String createdDateFrom,
                                        @RequestParam(name = "createdDateTo") String createdDateTo) {
        log.debug("REST request to get Manquants by store");
        return manquantService.findByStoreId(page, size, sortBy, direction, createdDateFrom, createdDateTo);
    }

    @GetMapping("/api/manquants-by-product/{storeId}")
    public List<ManquantRepository.ProductManquants> findProductManquants(@PathVariable Long storeId,
                                                                          @RequestParam(name = "createdDateFrom") String createdDateFrom,
                                                                          @RequestParam(name = "createdDateTo") String createdDateTo) {
        log.debug("REST request to get Manquants by product");
        return manquantService.findProductManquantsByDateRange(storeId, createdDateFrom, createdDateTo);
    }

    /**
     * GET  /manquants/:id : get the "id" manquant.
     *
     * @param id the id of the manquant to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the manquant, or with status 404 (Not Found)
     */
    @GetMapping("/api/manquants/{id}")
    public ResponseEntity<ManquantDto> getManquant(@PathVariable Long id) {
        log.debug("REST request to get Manquant : {}", id);
        ManquantDto manquantDto = manquantService.findOne(id);
        return Optional.ofNullable(manquantDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * DELETE  /manquants/:id : delete the "id" manquant.
     *
     * @param id the id of the manquant to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/api/manquants/{id}")
    public ResponseEntity<?> deleteManquant(@PathVariable Long id) {
        log.debug("REST request to delete Manquant : {}", id);
        return manquantService.delete(id);
        //return new ResponseEntity<ManquantDto>(HttpStatus.NO_CONTENT);
    }
}
