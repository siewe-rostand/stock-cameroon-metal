package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.dto.ListFactureDto;
import com.siewe.inventorymanagementsystem.model.ListFacture;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.service.ListFactureService;
import com.siewe.inventorymanagementsystem.utils.CustomErrorType;
import com.siewe.inventorymanagementsystem.utils.InvalidActionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Bl.
 */
@CrossOrigin
@RestController
public class ListFactureController {
    private final Logger log = LoggerFactory.getLogger(ListFactureController.class);

    @Autowired
    private ListFactureService blService;

    @Autowired
    private UserRepository userRepository;

    /**
     * POST  /bls : Create a new bl.
     *
     * @param blDto the bl to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bl, or with status 400 (Bad Request) if the bl has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/bls")
    public ResponseEntity<ListFactureDto> createBl(@Valid @RequestBody ListFactureDto blDto) throws URISyntaxException {
        log.debug("REST request to save Bl : {}", blDto);
        if (blDto.getId() != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A bl with id " +
                    blDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }
        //automatically set user to current user
//        blDto.setUserId(userRepository.findByLogin(SecurityUtils.getCurrentUserLogin()).getId());
        return blService.save(blDto);
    }

    /**
     * PUT  /bls : Updates an existing bl.
     *
     * @param blDto the bl to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bl,
     * or with status 400 (Bad Request) if the bl is not valid,
     * or with status 500 (Internal Server Error) if the bl couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/api/bls")
    public ResponseEntity<ListFactureDto> updateBl(@Valid @RequestBody ListFactureDto blDto) throws URISyntaxException {
        log.debug("REST request to update Bl : {}", blDto);
        if (blDto.getId() == null) {
            return createBl(blDto);
        }
        return blService.update(blDto);
    }


    @GetMapping("/api/bls")
    public List<ListFactureDto> getAllBls()
            throws URISyntaxException {
        log.debug("REST request to get all bls");
        return blService.findAll();
    }

    @GetMapping("/api/bls/{id}")
    public ResponseEntity<ListFactureDto> getBl(@PathVariable Long id) {
        log.debug("REST request to get Bl : {}", id);
        ListFactureDto blDto = blService.findOne(id);
        return Optional.ofNullable(blDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/api/bls/{id}")
    public ResponseEntity<?> deleteBl(@PathVariable Long id) {
        log.debug("REST request to delete Bl : {}", id);
        try {
            blService.delete(id);
        } catch (InvalidActionException e) {
            return new ResponseEntity(new CustomErrorType(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<ListFactureDto>(HttpStatus.NO_CONTENT);
    }
}

