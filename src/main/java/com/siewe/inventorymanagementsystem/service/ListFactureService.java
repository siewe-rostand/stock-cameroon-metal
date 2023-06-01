package com.siewe.inventorymanagementsystem.service;


import com.siewe.inventorymanagementsystem.dto.ApprovisionnementDto;
import com.siewe.inventorymanagementsystem.dto.ListFactureDto;
import com.siewe.inventorymanagementsystem.model.Approvisionnement;
import com.siewe.inventorymanagementsystem.model.ListFacture;
import com.siewe.inventorymanagementsystem.model.Supplier;
import com.siewe.inventorymanagementsystem.model.User;
import com.siewe.inventorymanagementsystem.repository.*;
import com.siewe.inventorymanagementsystem.utils.InvalidActionException;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ListFactureService {

    private final Logger log = LoggerFactory.getLogger(ListFactureService.class);

    @Autowired
    private ListFactureRepository listFactureRepository;

    @Autowired
    private ApprovisionnementService approvisionnementService;

    @Autowired
    private ManquantService manquantService;

    @Autowired
    private ApprovisionnementRepository approvisionnementRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierService supplierService;


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${dir.pharma}")
    private String FOLDER;

    /*@Autowired
    private ProductBlService productBlService;*/

    /**
     * Save a bl.
     *
     * @param listFactureDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity<ListFactureDto> save(ListFactureDto listFactureDto) {
        log.debug("Request to save Bl : {}", listFactureDto);

        ListFacture listFacture = new ListFacture();
        listFacture.setId(listFactureDto.getId());
        listFacture.setNumFacture(listFactureDto.getNumFacture());
        listFacture.setReleve(listFactureDto.getReleve());

        //set created date;
        String pattern = "yyyy-MM-dd HH:mm";
        LocalDateTime datetime = new LocalDateTime(DateTimeZone.forOffsetHours(1));
        listFacture.setCreatedDate(datetime.toString(pattern));

        ListFacture result = listFactureRepository.save(listFacture);
        if (result != null){
            //create Approvisionnements
            for (ApprovisionnementDto approvisionnementDto : listFactureDto.getApprovisionnements() ){
                approvisionnementDto.setBlId(result.getId());
                approvisionnementService.save(approvisionnementDto);
            }
        }

        if(listFactureDto.getUserId() != null){
            User user = userRepository.findByUserId(listFactureDto.getUserId());
            listFacture.setUser(user);
        }
        if(listFactureDto.getSupplierId() != null){
            Supplier supplier = supplierRepository.findOne(listFactureDto.getSupplierId());
            supplierService.update(listFactureDto.getSupplier());
            listFacture.setSupplier(supplier);
        }
        else{
            //create new supplier
            if(listFactureDto.getSupplier() != null){
                Supplier supplier = supplierService.save(listFactureDto.getSupplier());
                if(supplier != null)
                    listFacture.setSupplier(supplier);
            }
        }
        return new ResponseEntity<>(new ListFactureDto().createDTO(result), HttpStatus.CREATED);
    }


    public ResponseEntity<ListFactureDto> update(ListFactureDto listFactureDto) {
        log.debug("Request to save Bl : {}", listFactureDto);

        ListFacture listFacture = listFactureRepository.findOne(listFactureDto.getId());

        listFacture.setId(listFactureDto.getId());

        if (listFactureDto.getApprovisionnements() != null) {
            for (ApprovisionnementDto approvisionnementDto : listFactureDto.getApprovisionnements()){
                approvisionnementService.update(approvisionnementDto);
            }
        }

        ListFacture result = listFactureRepository.save(listFacture);
        return new ResponseEntity<>(new ListFactureDto().createDTO(result), HttpStatus.CREATED);
    }

    /**
     *  Get all the bls.
     *
     *  @return the list of entities
     */

    @Transactional(readOnly = true)
    public List<ListFactureDto> findAll() {
        log.debug("Request to get all Bls");
        List<ListFacture> bls = listFactureRepository.findAll();
        List<ListFactureDto> blDtos = new ArrayList<>();
        for(ListFacture c: bls){
            blDtos.add(new ListFactureDto().createDTO(c));
        }
        return blDtos;
    }


    @Transactional(readOnly = true)
    public ListFactureDto findOne(Long id) {
        log.debug("Request to get Bl : {}", id);
        ListFacture bl = listFactureRepository.findOne(id);
        return new ListFactureDto().createDTO(bl);
    }

    @Transactional(rollbackFor = InvalidActionException.class)
    public void delete(Long id) throws InvalidActionException {
        log.debug("Request to delete Bl : {}", id);
        ListFacture bl = listFactureRepository.findOne(id);
        if(Optional.ofNullable(bl).isPresent()){
            for(Approvisionnement app: bl.getApprovisionnements()){
                approvisionnementService.delete(app.getId());
            }
            listFactureRepository.deleteById(id);
        }
    }
}