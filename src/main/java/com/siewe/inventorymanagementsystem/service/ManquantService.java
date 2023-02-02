package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.ManquantDto;
import com.siewe.inventorymanagementsystem.model.Manquant;
import com.siewe.inventorymanagementsystem.model.Product;
import com.siewe.inventorymanagementsystem.repository.ManquantRepository;
import com.siewe.inventorymanagementsystem.repository.ProductRepository;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ManquantService {

    private final Logger log = LoggerFactory.getLogger(ManquantService.class);

    @Autowired
    private ManquantRepository manquantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockService productStockService;

    /**
     * Save a manquant.
     *
     * @param manquantDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity<ManquantDto> save(ManquantDto manquantDto) {
        log.debug("Request to save Manquant : {}", manquantDto);

        Manquant manquant = new Manquant();

        manquant.setId(manquantDto.getId());
        manquant.setQuantity(manquantDto.getQuantity());

        //set created date;
        String pattern = "yyyy-MM-dd HH:mm";
        LocalDateTime datetime = new LocalDateTime(DateTimeZone.forOffsetHours(1));
        manquant.setCreatedDate(datetime.toString(pattern));

        /*if(manquantDto.getUserId() != null){
            User user = userRepository.findOne(manquantDto.getUserId());
            manquant.setUser(user);
        }*/

        if(manquantDto.getProductId() != null){
            Product product = productRepository.findByProductId(manquantDto.getProductId());
            manquant.setProduct(product);
            manquant.setCout(product.getCump());
        }

        Manquant result = manquantRepository.save(manquant);
        if(result != null){
            productStockService.deleteStockManquant(result);
        }
        return new ResponseEntity<ManquantDto>(new ManquantDto().createDTO(result), HttpStatus.CREATED);
    }

    public ResponseEntity<ManquantDto> update(ManquantDto manquantDto) {
        log.debug("Request to save Manquant : {}", manquantDto);

        Manquant manquant = manquantRepository.findOne(manquantDto.getId());

        manquant.setId(manquantDto.getId());
        manquant.setQuantity(manquantDto.getQuantity());

        /*if(manquantDto.getUserId() != null){
            User user = userRepository.findOne(manquantDto.getUserId());
            manquant.setUser(user);
        }*/

        /*if(manquantDto.getProductId() != null){
            Product product = productRepository.findOne(manquantDto.getProductId());
            manquant.setProduct(product);
        }*/

        Manquant result = manquantRepository.save(manquant);
        return new ResponseEntity<ManquantDto>(new ManquantDto().createDTO(result), HttpStatus.CREATED);
    }


    /**
     *  Get all the manquants.
     *
     *  @return the list of entities
     */
    public Page<ManquantDto> findByStoreId(Integer page,
                                           Integer size,
                                           String sortBy,
                                           String direction,
                                           Long storeId, String createdDateFrom,
                                           String createdDateTo) {
        log.debug("Request to get all Manquants");
        //Pageable pageable = new PageRequest(page, size);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cdf = null;
        if(createdDateFrom!=null)
            cdf = LocalDateTime.parse(createdDateFrom, formatter);

        LocalDateTime cdt = null;
        if(createdDateTo!=null)
            cdt = LocalDateTime.parse(createdDateTo, formatter);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<Manquant> manquants = manquantRepository.findByCreatedDateBetween(cdf, cdt, pageable);

        Page<ManquantDto> manquantDtos = manquants.map(manquant -> new ManquantDto().createDTO(manquant));
        return manquantDtos;
    }

    public List<ManquantRepository.ProductManquants> findProductManquantsByDateRange(Long storeId, String createdDateFrom, String createdDateTo) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cdf = null;
        if(createdDateFrom!=null)
            cdf = LocalDateTime.parse(createdDateFrom, formatter);

        LocalDateTime cdt = null;
        if(createdDateTo!=null)
            cdt = LocalDateTime.parse(createdDateTo, formatter);

        return manquantRepository.findProductManquantsByDateRange(cdf, cdt);
    }

    /**
     *  Get one manquant by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ManquantDto findOne(Long id) {
        log.debug("Request to get Manquant : {}", id);
        Manquant manquant = manquantRepository.findOne(id);
        return new ManquantDto().createDTO(manquant);
    }

    /**
     *  Delete the  manquant by id.
     *
     * @param id the id of the entity
     */
    //@Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity delete(Long id) {
        log.debug("Request to delete Manquant : {}", id);
        Manquant manquant = manquantRepository.findOne(id);
        if(Optional.ofNullable(manquant).isPresent()){
            /*if(!manquant.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin())){
                return new ResponseEntity(new CustomErrorType("You should not do this !"), HttpStatus.CONFLICT);
            }*/
            productStockService.restoreStockManquant(manquant);
            manquantRepository.deleteById(id);
        }
        return null;
    }
}
