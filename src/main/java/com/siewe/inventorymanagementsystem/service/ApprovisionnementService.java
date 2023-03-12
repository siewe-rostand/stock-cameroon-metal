package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.ApprovisionnementDto;
import com.siewe.inventorymanagementsystem.model.Approvisionnement;
import com.siewe.inventorymanagementsystem.model.Product;
import com.siewe.inventorymanagementsystem.repository.ApprovisionnementRepository;
import com.siewe.inventorymanagementsystem.repository.ProductRepository;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.utils.InvalidActionException;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ApprovisionnementService {
    private final Logger log = LoggerFactory.getLogger(ApprovisionnementService.class);

    @Autowired
    private ApprovisionnementRepository approvisionnementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductStockService productStockService;

    /**
     * Save a approvisionnement.
     *
     * @param approvisionnementDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity<ApprovisionnementDto> save(ApprovisionnementDto approvisionnementDto) {
        log.debug("Request to save Approvisionnement : {}", approvisionnementDto);

        Approvisionnement approvisionnement = new Approvisionnement();

        approvisionnement.setId(approvisionnementDto.getId());
        approvisionnement.setQuantity(approvisionnementDto.getQuantity());
        approvisionnement.setPrixEntree(approvisionnementDto.getPrixEntree());

        //approvisionnement.setPrixEntree(approvisionnementDto.getPrixEntree());
        if(approvisionnementDto.getCoutTotal() != null)
            approvisionnement.setPrixEntree((double) Math.round( (approvisionnementDto.getCoutTotal()/approvisionnement.getQuantity()) * 100.0 ) /100.0);

        //set created date;
        String pattern = "yyyy-MM-dd HH:mm";
        LocalDateTime datetime = new LocalDateTime(DateTimeZone.forOffsetHours(1));
        approvisionnement.setCreatedDate(datetime.toString(pattern));


        if(approvisionnementDto.getId() != null){
            Product product = productRepository.findOne(approvisionnementDto.getId());
            approvisionnement.setProduct(product);
        }

       /* if(approvisionnementDto.getBlId() != null){
            Bl bl = blRepository.findOne(approvisionnementDto.getBlId());
            //supplierService.update(approvisionnementDto.getSupplier());
            approvisionnement.setBl(bl);
        }*/

        Approvisionnement result = approvisionnementRepository.save(approvisionnement);

        if(result != null){
            productStockService.addStock(result);
        }

        return new ResponseEntity<ApprovisionnementDto>(new ApprovisionnementDto().createDTO(result), HttpStatus.CREATED);
    }

    public ResponseEntity<ApprovisionnementDto> update(ApprovisionnementDto approvisionnementDto) {
        log.debug("Request to save Approvisionnement : {}", approvisionnementDto);

        Approvisionnement approvisionnement = approvisionnementRepository.findOne(approvisionnementDto.getId());

        approvisionnement.setId(approvisionnementDto.getId());
        approvisionnement.setQuantity(approvisionnementDto.getQuantity());
        //approvisionnement.setPrixEntree(approvisionnementDto.getPrixEntree());

        if(approvisionnementDto.getCoutTotal() != null)
            approvisionnement.setPrixEntree((double) Math.round( (approvisionnementDto.getCoutTotal()/approvisionnement.getQuantity()) * 100.0 ) /100.0);


        /*if(approvisionnementDto.getUserId() != null){
            User user = userRepository.findOne(approvisionnementDto.getUserId());
            approvisionnement.setUser(user);
        }*/

        if(approvisionnementDto.getId() != null){
            Product product = productRepository.findOne(approvisionnementDto.getId());
            approvisionnement.setProduct(product);
        }

        Approvisionnement result = approvisionnementRepository.save(approvisionnement);
        /*if(result != null){
            productStockService.editStock(result);
        }*/
        return new ResponseEntity<ApprovisionnementDto>(new ApprovisionnementDto().createDTO(result), HttpStatus.CREATED);
    }

    /**
     *  Get all the approvisionnements.
     *
     *  @return the list of entities
     */
    public Page<ApprovisionnementDto> findByStoreId(Integer page,
                                                    Integer size,
                                                    String sortBy,
                                                    String direction,
                                                    Long storeId,
                                                    String createdDateFrom,
                                                    String createdDateTo, Long productId) {
        log.debug("Request to get all Approvisionnements");
        //Pageable pageable = new PageRequest(page, size);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cdf = null;
        if(createdDateFrom!=null)
            cdf = LocalDateTime.parse(createdDateFrom, formatter);

        LocalDateTime cdt = null;
        if(createdDateTo!=null)
            cdt = LocalDateTime.parse(createdDateTo, formatter);

        Pageable pageable =  PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Page<Approvisionnement> approvisionnements;

        if(productId == 0)
            approvisionnements = approvisionnementRepository.findByCreatedDateBetween(cdf, cdt, pageable);
        else
            approvisionnements = approvisionnementRepository.findOne(cdf, cdt, productId, pageable);

        Page<ApprovisionnementDto> approvisionnementDtos = approvisionnements.map(approvisionnement -> new ApprovisionnementDto().createDTO(approvisionnement));
        return approvisionnementDtos;
    }

    public List<ApprovisionnementRepository.ProductApprovisionnements> findProductApprovisionnementsByDateRange(Long storeId, String createdDateFrom, String createdDateTo, Long productId) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cdf = null;
        if(createdDateFrom!=null)
            cdf = LocalDateTime.parse(createdDateFrom, formatter);

        LocalDateTime cdt = null;
        if(createdDateTo!=null)
            cdt = LocalDateTime.parse(createdDateTo, formatter);

        if(productId == 0)
            return approvisionnementRepository.findProductApprovisionnementsByDateRange(cdf, cdt);
        else
            return approvisionnementRepository.findProductApprovisionnementsByDateRange(cdf, cdt, productId);
    }

    /**
     *  Get one approvisionnement by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ApprovisionnementDto findOne(Long id) {
        log.debug("Request to get Approvisionnement : {}", id);
        Approvisionnement approvisionnement = approvisionnementRepository.findOne(id);
        return new ApprovisionnementDto().createDTO(approvisionnement);
    }

    /**
     *  Delete the  approvisionnement by id.
     *
     * @param id the id of the entity
     */
    @Transactional(rollbackFor = InvalidActionException.class)
    public ResponseEntity delete(Long id) throws InvalidActionException {
        log.debug("Request to delete Approvisionnement : {}", id);
        Approvisionnement approvisionnement = approvisionnementRepository.findOne(id);
        if(Optional.ofNullable(approvisionnement).isPresent()){
            System.out.println("delete stock appro");
            productStockService.deleteStockAppro(approvisionnement);
            approvisionnementRepository.deleteById(id);
        }
        return null;
    }
}
