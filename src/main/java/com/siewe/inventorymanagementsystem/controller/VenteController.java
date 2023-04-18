//package com.siewe.inventorymanagementsystem.controller;
//
//import com.siewe.inventorymanagementsystem.dto.VenteDto;
//import com.siewe.inventorymanagementsystem.model.Vente;
//import com.siewe.inventorymanagementsystem.repository.UserRepository;
//import com.siewe.inventorymanagementsystem.repository.VenteRepository;
//import com.siewe.inventorymanagementsystem.service.ReglementService;
//import com.siewe.inventorymanagementsystem.service.VenteService;
//import com.siewe.inventorymanagementsystem.utils.CustomErrorType;
//import com.siewe.inventorymanagementsystem.utils.InvalidOrderItemException;
//import org.joda.time.DateTimeZone;
//import org.joda.time.LocalDate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.io.IOException;
//import java.net.URISyntaxException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.Optional;
//
///**
// * REST controller for managing Vente.
// */
//@CrossOrigin
//@RestController
//public class VenteController {
//    private final Logger log = LoggerFactory.getLogger(VenteController.class);
//
//    @Autowired
//    private VenteService venteService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//
//    @Autowired
//    private ReglementService reglementService;
//
//    /**
//     * POST  /ventes : Create a new vente.
//     *
//     * @param venteDto the vente to create
//     * @return the ResponseEntity with status 201 (Created) and with body the new vente, or with status 400 (Bad Request) if the vente has already an ID
//     * @throws URISyntaxException if the Location URI syntax is incorrect
//     */
//    @PostMapping("/api/ventes")
//    public ResponseEntity<VenteDto> createVente(@Valid @RequestBody VenteDto venteDto) throws URISyntaxException {
//        log.debug("REST request to save Vente : {}", venteDto);
//        //automatically set user to current user
////        venteDto.setUserId(userRepository.findByUsername(SecurityUtils.getCurrentUserLogin()).getId());
//
//        Vente result = null;
//        try {
//            result = venteService.save(venteDto);
//        } catch (InvalidOrderItemException e) {
//            //return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
//            return new ResponseEntity(new CustomErrorType(e.getMessage()), HttpStatus.CONFLICT);
//        }
//        return new ResponseEntity<VenteDto>(new VenteDto().createDTO(result), HttpStatus.CREATED);
//    }
//
//    /**
//     * PUT  /ventes : Updates an existing vente.
//     *
//     * @param venteDto the vente to update
//     * @return the ResponseEntity with status 200 (OK) and with body the updated vente,
//     * or with status 400 (Bad Request) if the vente is not valid,
//     * or with status 500 (Internal Server Error) if the vente couldnt be updated
//     * @throws URISyntaxException if the Location URI syntax is incorrect
//     */
//    @PutMapping("/api/ventes")
//    public ResponseEntity<VenteDto> updateVente(@Valid @RequestBody VenteDto venteDto) throws URISyntaxException {
//        log.debug("REST request to update Vente : {}", venteDto);
//        if (venteDto.getId() == null) {
//            return createVente(venteDto);
//        }
//        return venteService.update(venteDto);
//    }
//
//
//    /**
//     * GET  /ventes : get all the ventes.
//     */
//
//    @GetMapping("/api/ventes-by-store/{storeId}")
//    public Page<VenteDto> getByStore(@PathVariable Long storeId,
//                                     @RequestParam(name = "page", defaultValue = "0") Integer page,
//                                     @RequestParam(name = "size", defaultValue = "999999") Integer size,
//                                     @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
//                                     @RequestParam(name = "direction", defaultValue = "desc") String direction,
//                                     @RequestParam(name = "createdDateFrom") String createdDateFrom,
//                                     @RequestParam(name = "createdDateTo") String createdDateTo,
//                                     @RequestParam(name = "seller", defaultValue="0") Long sellerId) {
//        log.debug("REST request to get Ventes by store");
//        /*if(!SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")){
//            sellerId = userRepository.findByLogin(SecurityUtils.getCurrentUserLogin()).getId();
//        }*/
//        return venteService.findByStoreId(page, size, sortBy, direction, storeId, createdDateFrom, createdDateTo, sellerId);
//    }
//
//    @GetMapping("/api/recouvrements-by-store/{storeId}")
//    public Page<VenteDto> getRecouvrementsByStore(@PathVariable Long storeId,
//                                                  @RequestParam(name = "page", defaultValue = "0") Integer page,
//                                                  @RequestParam(name = "size", defaultValue = "999999") Integer size,
//                                                  @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
//                                                  @RequestParam(name = "direction", defaultValue = "desc") String direction) {
//        log.debug("REST request to get Ventes by store");
//        return venteService.findRecouvrementsByStoreId(page, size, sortBy, direction, storeId);
//    }
//
//    @GetMapping("/api/ventes-deleted-by-store/{storeId}")
//    public Page<VenteDto> getVentesDeletedByStore(@PathVariable Long storeId,
//                                                  @RequestParam(name = "page", defaultValue = "0") Integer page,
//                                                  @RequestParam(name = "size", defaultValue = "999999") Integer size,
//                                                  @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
//                                                  @RequestParam(name = "direction", defaultValue = "desc") String direction) {
//
//        log.debug("REST request to get Ventes by store");
//        long sellerId = 0;
//       /* if(!SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")){
//            sellerId = userRepository.findByLogin(SecurityUtils.getCurrentUserLogin()).getId();
//        }*/
//        return venteService.findVentesDeletedByStoreId(page, size, sortBy, direction, storeId, sellerId);
//    }
//
////    @GetMapping("/api/ventes-by-product/{storeId}")
////    public List<VenteRepository.ProductSales> findProductSales(@PathVariable Long storeId,
////                                                               @RequestParam(name = "createdDateFrom") String createdDateFrom,
////                                                               @RequestParam(name = "createdDateTo") String createdDateTo,
////                                                               @RequestParam(name = "seller", defaultValue="0") Long sellerId) {
////        log.debug("REST request to get Ventes by product");
////       /* if(!SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")){
////            sellerId = userRepository.findByLogin(SecurityUtils.getCurrentUserLogin()).getId();
////        }*/
////        return venteService.findProductSalesByDateRange(storeId, createdDateFrom, createdDateTo, sellerId);
////    }
//
//    @GetMapping("/api/ventes-by-day/{storeId}")
//    public List<VenteService.DailySale> findDailySales(@PathVariable Long storeId,
//                                                       @RequestParam(name = "createdDateFrom") String createdDateFrom,
//                                                       @RequestParam(name = "createdDateTo") String createdDateTo,
//                                                       @RequestParam(name = "product", defaultValue="0") Long productId,
//                                                       @RequestParam(name = "seller", defaultValue="0") Long sellerId) {
//        log.debug("REST request to get Ventes by day");
//        /*if(!SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")){
//            sellerId = userRepository.findByLogin(SecurityUtils.getCurrentUserLogin()).getId();
//        }*/
//        if(productId == 0)
//            return venteService.findDailySalesByDateRange(storeId, createdDateFrom, createdDateTo, sellerId);
//        else
//            return venteService.findDailySalesByProductAndDateRange(productId, createdDateFrom, createdDateTo, sellerId);
//
//    }
//
//    @GetMapping("/api/ventes-by-hour/{storeId}")
//    public List<VenteService.HourlySale> findSalesByHour(@PathVariable Long storeId,
//                                                         @RequestParam(name = "createdDateFrom") String createdDateFrom,
//                                                         @RequestParam(name = "createdDateTo") String createdDateTo,
//                                                         @RequestParam(name = "seller", defaultValue="0") Long sellerId) {
//        log.debug("REST request to get Ventes by day");
//        return venteService.findHourlySalesByDateRange(storeId, createdDateFrom, createdDateTo, sellerId);
//    }
//
//    @GetMapping("/api/ventes-by-seller/{storeId}")
//    public List<VenteService.SellerSale> findSalesBySeller(@PathVariable Long storeId,
//                                                           @RequestParam(name = "createdDateFrom") String createdDateFrom,
//                                                           @RequestParam(name = "createdDateTo") String createdDateTo) {
//        log.debug("REST request to get Ventes by day");
//        return venteService.findSellerSalesByDateRange(storeId, createdDateFrom, createdDateTo);
//    }
//    /**
//     * GET  /ventes/:id : get the "id" vente.
//     *
//     * @param id the id of the vente to retrieve
//     * @return the ResponseEntity with status 200 (OK) and with body the vente, or with status 404 (Not Found)
//     */
//    @GetMapping("/api/ventes/{id}")
//    public ResponseEntity<VenteDto> getVente(@PathVariable Long id) {
//        log.debug("REST request to get Vente : {}", id);
//        VenteDto venteDto = venteService.findOne(id);
//        return Optional.ofNullable(venteDto)
//                .map(result -> new ResponseEntity<>(
//                        result,
//                        HttpStatus.OK))
//                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
//    }
//
//    @RequestMapping(value="/api/ventes-export", method=RequestMethod.GET)
//    public ResponseEntity<byte[]> export(@RequestParam(name = "store") Long storeId,
//                                         @RequestParam(name = "createdDateFrom") String createdDateFrom,
//                                         @RequestParam(name = "createdDateTo") String createdDateTo,
//                                         @RequestParam(name = "seller", defaultValue="0") Long sellerId) throws IOException {
//
//        String FILE_PATH = "";
//        HttpHeaders headers = new HttpHeaders();
//
//        FILE_PATH = venteService.createXlsFile(storeId, createdDateFrom, createdDateTo, sellerId);
//        headers.setContentType(MediaType.parseMediaType("application/xls"));
//
//        Path path = Paths.get(FILE_PATH);
//        byte[] data = Files.readAllBytes(path);
//        String filename = "ventes_du_" + createdDateFrom + "_au_" + createdDateTo +".xls";
//        filename = filename.replace(" ", "_");
//
//        headers.set(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=" + filename.toLowerCase());
//        //headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
//        return response;
//    }
//
//    @RequestMapping(value="/api/recouvrements-export", method=RequestMethod.GET)
//    public ResponseEntity<byte[]> exportRecouvrements(@RequestParam(name = "store") Long storeId) throws IOException {
//
//        String FILE_PATH = "";
//        HttpHeaders headers = new HttpHeaders();
//
//        FILE_PATH = venteService.createXlsFileRecouvrements(storeId);
//        headers.setContentType(MediaType.parseMediaType("application/xls"));
//
//        Path path = Paths.get(FILE_PATH);
//        byte[] data = Files.readAllBytes(path);
//        String filename = "recouvrements_du_" + new LocalDate(DateTimeZone.forOffsetHours(1)).toString("yyyy-MM-dd") +".xls";
//        filename = filename.replace(" ", "_");
//
//        headers.set(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=" + filename.toLowerCase());
//        //headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
//        return response;
//    }
//
//
//    @RequestMapping(value="/api/ventes-export-best-products", method=RequestMethod.GET)
//    public ResponseEntity<byte[]> exportBestProducts(@RequestParam(name = "store") Long storeId,
//                                                     @RequestParam(name = "createdDateFrom") String createdDateFrom,
//                                                     @RequestParam(name = "createdDateTo") String createdDateTo) throws IOException {
//
//        String FILE_PATH = "";
//        HttpHeaders headers = new HttpHeaders();
//
//        FILE_PATH = venteService.createXlsFileBestProducts(storeId, createdDateFrom, createdDateTo);
//        headers.setContentType(MediaType.parseMediaType("application/xls"));
//
//        Path path = Paths.get(FILE_PATH);
//        byte[] data = Files.readAllBytes(path);
//        String filename = "ventes_par_produit_du_" + createdDateFrom + "_au_" + createdDateTo +".xls";
//        filename = filename.replace(" ", "_");
//
//        headers.set(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=" + filename.toLowerCase());
//        //headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
//        return response;
//    }
//
//    @RequestMapping(value="/api/ventes-export-best-sellers", method=RequestMethod.GET)
//    public ResponseEntity<byte[]> exportBestSellers(@RequestParam(name = "store") Long storeId,
//                                                    @RequestParam(name = "createdDateFrom") String createdDateFrom,
//                                                    @RequestParam(name = "createdDateTo") String createdDateTo) throws IOException {
//
//        String FILE_PATH = "";
//        HttpHeaders headers = new HttpHeaders();
//        FILE_PATH = venteService.createXlsFileBestSellers(storeId, createdDateFrom, createdDateTo);
//        headers.setContentType(MediaType.parseMediaType("application/xls"));
//
//        Path path = Paths.get(FILE_PATH);
//        byte[] data = Files.readAllBytes(path);
//        String filename = "ventes_par_vendeur_du_" + createdDateFrom + "_au_" + createdDateTo +".xls";
//        filename = filename.replace(" ", "_");
//
//        headers.set(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=" + filename.toLowerCase());
//        //headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
//        return response;
//    }
//
//
//    /**
//     * DELETE  /ventes/:id : delete the "id" vente.
//     *
//     * @param id the id of the vente to delete
//     * @return the ResponseEntity with status 200 (OK)
//     */
//    @DeleteMapping("/api/ventes/{id}")
//    public ResponseEntity<?> deleteVente(@PathVariable Long id) throws CustomErrorType {
//        log.debug("REST request to delete Vente : {}", id);
//        venteService.delete(id);
//        return new ResponseEntity<VenteDto>(HttpStatus.NO_CONTENT);
//    }
//
//    @DeleteMapping("/api/ventes-confirm-delete/{id}")
//    public ResponseEntity<?> confirmDeleteVente(@PathVariable Long id) {
//        log.debug("REST request to delete Vente : {}", id);
//        venteService.deleteConfirm(id);
//        return new ResponseEntity<VenteDto>(HttpStatus.NO_CONTENT);
//    }
//
//    @GetMapping("/api/ventes-cancel-delete/{id}")
//    public ResponseEntity<?> cancelDeleteVente(@PathVariable Long id) {
//        log.debug("REST request to delete Vente : {}", id);
//        venteService.deleteCancel(id);
//        return new ResponseEntity<VenteDto>(HttpStatus.NO_CONTENT);
//    }
//
//    @DeleteMapping("/api/payments/{id}")
//    public ResponseEntity<?> deletePayment(@PathVariable Long id) {
//        log.debug("REST request to delete Payment : {}", id);
//        reglementService.delete(id);
//        return new ResponseEntity<VenteDto>(HttpStatus.NO_CONTENT);
//    }
//}
//
