package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.ReglementDto;
import com.siewe.inventorymanagementsystem.model.Reglement;
import com.siewe.inventorymanagementsystem.model.User;
import com.siewe.inventorymanagementsystem.model.Vente;
import com.siewe.inventorymanagementsystem.repository.ReglementRepository;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.repository.VenteRepository;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReglementService {

    private final Logger log = LoggerFactory.getLogger(ReglementService.class);

    @Autowired
    private ReglementRepository reglementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VenteRepository venteRepository;

    /**
     * Save a reglement.
     *
     * @param reglementDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity<ReglementDto> save(ReglementDto reglementDto) {
        log.debug("Request to save Reglement : {}", reglementDto);

        //automatically set user to current user
//        reglementDto.setStaffId(userRepository.findByLogin(SecurityUtils.getCurrentUserLogin()).getId());

        Reglement reglement = new Reglement();

        reglement.setId(reglementDto.getId());
        reglement.setAmount(reglementDto.getAmount());

        //set created date;
        String patternDate = "yyyy-MM-dd HH:mm";
        LocalDateTime date = new LocalDateTime();
        reglement.setCreatedDate(date.toString(patternDate));

        if(reglementDto.getVenteId() != null){
            Vente vente = venteRepository.findOne(reglementDto.getVenteId());
            reglement.setVente(vente);
        }

        if(reglementDto.getStaffId() != null){
            User user = userRepository.findByUserId(reglementDto.getStaffId());
            reglement.setStaff(user);
        }

        Reglement result = reglementRepository.save(reglement);

        return new ResponseEntity<>(new ReglementDto().createDTO(result), HttpStatus.CREATED);
    }

    @Transactional(readOnly = true)
    public ReglementDto findOne(Long id) {
        log.debug("Request to get Reglement : {}", id);
        Reglement reglement = reglementRepository.findOne(id);
        return new ReglementDto().createDTO(reglement);
    }


    public void delete(Long id) {
        log.debug("Request to delete Reglement : {}", id);
        Reglement reglement = reglementRepository.findOne(id);

        Vente vente = reglement.getVente();
        vente.setAcompte(vente.getAcompte() - reglement.getAmount());
        venteRepository.save(vente);

        reglementRepository.deleteById(id);
    }
}
