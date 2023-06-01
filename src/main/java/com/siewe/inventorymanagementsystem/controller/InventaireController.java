package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.dto.InventaireDto;
import com.siewe.inventorymanagementsystem.service.InventaireService;
import com.siewe.inventorymanagementsystem.utils.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class InventaireController {
    private final Logger log = LoggerFactory.getLogger(InventaireController.class);

    @Autowired
    private InventaireService inventaireService;

    /**
     * POST  /inventaires : Create a new inventaire.
     *
     * @param inventaireDto the inventaire to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inventaire, or with status 400 (Bad Request) if the inventaire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/inventaires")
    public ResponseEntity<InventaireDto> createInventaire(@Valid @RequestBody InventaireDto inventaireDto) throws URISyntaxException {
        log.debug("REST request to save Inventaire : {}", inventaireDto);
        if (inventaireDto.getId() != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A inventaire with id " +
                    inventaireDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }
        return inventaireService.save(inventaireDto);
    }

    /**
     * PUT  /inventaires : Updates an existing inventaire.
     *
     * @param inventaireDto the inventaire to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inventaire,
     * or with status 400 (Bad Request) if the inventaire is not valid,
     * or with status 500 (Internal Server Error) if the inventaire couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/api/inventaires")
    public ResponseEntity<InventaireDto> updateInventaire(@Valid @RequestBody InventaireDto inventaireDto) throws URISyntaxException {
        log.debug("REST request to update Inventaire : {}", inventaireDto);
        if (inventaireDto.getId() == null) {
            return createInventaire(inventaireDto);
        }
        return inventaireService.update(inventaireDto);
    }


    @GetMapping("/api/inventaires")
    public List<InventaireDto> getAllInventaires()
            throws URISyntaxException {
        log.debug("REST request to get all inventaires");
        return inventaireService.findAll();
    }

    @GetMapping("/api/inventaires/{id}")
    public ResponseEntity<InventaireDto> getInventaire(@PathVariable Long id) {
        log.debug("REST request to get Inventaire : {}", id);
        InventaireDto inventaireDto = inventaireService.findOne(id);
        return Optional.ofNullable(inventaireDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/api/inventaires-validate/{id}")
    public ResponseEntity<InventaireDto> validateInventaire(@PathVariable Long id) {
        log.debug("REST request to validate Inventaire : {}", id);
        return inventaireService.validate(id);
    }


    @RequestMapping(value="/api/inventaires-export", method= RequestMethod.POST)
    public ResponseEntity<byte[]> export(@RequestBody InventaireDto inventaireDto) throws IOException {

        String FILE_PATH = "";
        HttpHeaders headers = new HttpHeaders();

        FILE_PATH = inventaireService.createXlsFile(inventaireDto);
        headers.setContentType(MediaType.parseMediaType("application/xls"));

        Path path = Paths.get(FILE_PATH);
        byte[] data = Files.readAllBytes(path);
        String filename = "fiche_" + inventaireDto.getLabel() + ".xls";
        filename = filename.replace(" ", "_");

        headers.set(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=" + filename.toLowerCase());
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }


    @DeleteMapping("/api/inventaires/{id}")
    public ResponseEntity<?> deleteInventaire(@PathVariable Long id) {
        log.debug("REST request to delete Inventaire : {}", id);
        inventaireService.delete(id);
        return new ResponseEntity<InventaireDto>(HttpStatus.NO_CONTENT);
    }
}
