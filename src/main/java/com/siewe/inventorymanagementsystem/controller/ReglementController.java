package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.dto.ReglementDto;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.service.ReglementService;
import com.siewe.inventorymanagementsystem.utils.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * REST controller for managing Reglement.
 */
@RestController
@CrossOrigin
public class ReglementController {
    private final Logger log = LoggerFactory.getLogger(ReglementController.class);

    @Autowired
    private ReglementService reglementService;

    @Autowired
    private UserRepository userRepository;

    /**
     * POST  /reglements : Create a new reglement.
     *
     * @param reglementDto the reglement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reglement, or with status 400 (Bad Request) if the reglement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/reglements", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ReglementDto> createReglement(@RequestBody ReglementDto reglementDto) throws IOException {

        log.debug("REST request to save Reglement : {}", reglementDto);
        if (reglementDto.getId() != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A reglement with id " +
                    reglementDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }

        return reglementService.save(reglementDto);
    }


    /*@RequestMapping(value = "/reglement-montant", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<ReglementDto> updateReglementAmount(@RequestBody ReglementDto reglementDto) {
        return reglementService.updateMontant(reglementDto);
    }*/

    /**
     * GET  /reglements/:id : get the "id" reglement.
     *
     * @param id the id of the reglement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reglement, or with status 404 (Not Found)
     */
    @GetMapping("/reglements/{id}")
    public ResponseEntity<ReglementDto> getReglement(@PathVariable Long id) {
        log.debug("REST request to get Reglement : {}", id);
        ReglementDto reglementDto = reglementService.findOne(id);

        return Optional.ofNullable(reglementDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    /**
     * DELETE  /reglements/:id : delete the "id" reglement.
     *
     * @param id the id of the reglement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reglements/{id}")
    public ResponseEntity<?> deleteReglement(@PathVariable Long id) {
        log.debug("REST request to delete Reglement : {}", id);
        reglementService.delete(id);
        return new ResponseEntity<ReglementDto>(HttpStatus.NO_CONTENT);
    }
}

