package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.OrderedProductDto;
import com.siewe.inventorymanagementsystem.dto.ReglementDto;
import com.siewe.inventorymanagementsystem.dto.VenteDto;
import com.siewe.inventorymanagementsystem.model.OrderedProduct;
import com.siewe.inventorymanagementsystem.model.User;
import com.siewe.inventorymanagementsystem.model.Vente;
import com.siewe.inventorymanagementsystem.model.enumeration.TypePaiement;
import com.siewe.inventorymanagementsystem.repository.OrderedProductRepository;
import com.siewe.inventorymanagementsystem.repository.ProductRepository;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.repository.VenteRepository;
import com.siewe.inventorymanagementsystem.utils.CustomErrorType;
import com.siewe.inventorymanagementsystem.utils.InvalidOrderItemException;
import lombok.Data;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.*;

@Service
@Transactional
public class VenteService {

    private final Logger log = LoggerFactory.getLogger(VenteService.class);

    @Autowired
    private VenteRepository venteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepository customerRepository;

    @Autowired
    private UserService customerService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReglementService reglementService;

    @Autowired
    private OrderedProductService orderedProductService;

    @Autowired
    private OrderedProductRepository orderedProductRepository;

    @Autowired
    private StockService productStockService;

    @Value("${dir.pharma}")
    private String FOLDER;

    /**
     * Save a vente.
     *
     * @param venteDto the entity to save
     * @return the persisted entity
     */
    @Transactional(rollbackFor = InvalidOrderItemException.class)
    public Vente save(VenteDto venteDto) throws InvalidOrderItemException {
        log.debug("Request to save Vente : {}", venteDto);
        HashMap<String, String> error = new HashMap<>();

        Vente vente = new Vente();
        vente.setVenteId(venteDto.getId());
        vente.setPrixTotal(venteDto.getPrixTotal());
        vente.setReglement(venteDto.getReglement());
        vente.setTypePaiement(TypePaiement.fromValue(venteDto.getTypePaiement()));

        if(venteDto.getCustomerId() != null){
            User customer = customerRepository.findByUserId(venteDto.getCustomerId());
            customerService.update(venteDto.getCustomer());
            vente.setCustomer(customer);
        }
        else{
            //create new customer
            /* 
            if(venteDto.getCustomer() != null){
                User customer = customerService.saveCustomer(venteDto.getCustomer());
                if(customer != null)
                    vente.setCustomer(customer);
            }*/
        }

        //set created date;
        String pattern = "yyyy-MM-dd HH:mm";
        if(venteDto.getCreatedDate() == null){
            LocalDateTime datetime = new LocalDateTime(DateTimeZone.forOffsetHours(1));
            vente.setCreatedDate(datetime.toString(pattern));
        }
        else{
            vente.setCreatedDate(venteDto.getCreatedDate());
        }

        if(venteDto.getUserId() != null){
            User user = userRepository.findByUserId(venteDto.getUserId());
            vente.setUser(user);
        }

        Vente result = venteRepository.save(vente);
        if(result != null){
            if(venteDto.getOrderedProducts() != null){
                for(OrderedProductDto orderedProductDto: venteDto.getOrderedProducts()){
                    if(orderedProductDto != null){
                        orderedProductDto.setVenteId(result.getVenteId());
                        orderedProductService.save(orderedProductDto);
                    }
                }
            }

            //create reglements
            Double amount = result.getPrixTotal();
            if(result.getTypePaiement().equals(TypePaiement.Credit)){
                //System.out.println(result.getTypePaiement().toValue());
                if(venteDto.getAcompte() != null)
                    amount = venteDto.getAcompte();
            }
            vente.setAcompte(amount);

            ReglementDto reglementDto = new ReglementDto();
            reglementDto.setVenteId(result.getVenteId());
            reglementDto.setAmount(amount);
            if(amount > 0)
                reglementService.save(reglementDto);
        }

        venteRepository.save(result);
        return result;
    }

    public ResponseEntity<VenteDto> save2(VenteDto venteDto) {
        log.debug("Request to save Vente : {}", venteDto);

        Vente vente = new Vente();

        vente.setVenteId(venteDto.getId());
        vente.setCreatedDate(venteDto.getCreatedDate());

        if(venteDto.getUserId() != null){
            User user = userRepository.findByUserId(venteDto.getUserId());
            vente.setUser(user);
        }

        Vente result = venteRepository.save(vente);
        /*
        if(result != null){
            productStockService.deleteStockVente(result);
        }
        */
        return new ResponseEntity<VenteDto>(new VenteDto().createDTO(result), HttpStatus.CREATED);
    }


    public ResponseEntity<VenteDto> update(VenteDto venteDto) {
        log.debug("Request to save Vente : {}", venteDto);

        Vente vente = venteRepository.findByVenteId(venteDto.getId());

        if(vente != null){
            if(venteDto.getNewReglement() != null){
                ReglementDto reglementDto = new ReglementDto();
                reglementDto.setVenteId(vente.getVenteId());
                reglementDto.setAmount(venteDto.getNewReglement());
                vente.setAcompte(vente.getAcompte() + venteDto.getNewReglement());
                reglementService.save(reglementDto);
            }
        }
        Vente result = venteRepository.save(vente);
        return new ResponseEntity<VenteDto>(new VenteDto().createDTO(result), HttpStatus.CREATED);
    }

    /**
     *  Get all the ventes.
     *
     *  @return the list of entities
     */
    public Page<VenteDto> findByStoreId(Integer page,
                                        Integer size,
                                        String sortBy,
                                        String direction,
                                        Long storeId, String createdDateFrom,
                                        String createdDateTo, Long sellerId) {
        log.debug("Request to get all Ventes");
        //Pageable pageable = new PageRequest(page, size);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cdf = null;
        if(createdDateFrom!=null)
            cdf = LocalDateTime.parse(createdDateFrom, formatter);

        LocalDateTime cdt = null;
        if(createdDateTo!=null)
            cdt = LocalDateTime.parse(createdDateTo, formatter);

        Pageable pageable =  PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<Vente> ventes;
        if(sellerId == 0){
            ventes = venteRepository.findByCreatedDateBetween(cdf, cdt, pageable);
        }
        else{
            ventes = venteRepository.findBySellerId(sellerId, cdf, cdt, pageable);
        }

        Page<VenteDto> venteDtos = ventes.map(vente -> new VenteDto().createDTO(vente));
        return venteDtos;
    }

    public Page<VenteDto> findRecouvrementsByStoreId(Integer page, Integer size, String sortBy, String direction, Long storeId) {

        Pageable pageable =  PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<Vente> ventes = venteRepository.findRecouvrements(pageable);

        Page<VenteDto> venteDtos = ventes.map(vente -> new VenteDto().createDTO(vente));
        return venteDtos;    }

    public Page<VenteDto> findVentesDeletedByStoreId(Integer page, Integer size, String sortBy, String direction, Long storeId, long sellerId) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<Vente> ventes;
        if(sellerId == 0){
            ventes = venteRepository.findVentesDeleted(pageable);
        }
        else{
            ventes = venteRepository.findVentesDeletedBySellerId(sellerId, pageable);
        }
        Page<VenteDto> venteDtos = ventes.map(vente -> new VenteDto().createDTO(vente));
        return venteDtos;
    }

    public List<VenteRepository.ProductSales> findProductSalesByDateRange(Long storeId, String createdDateFrom, String createdDateTo, Long sellerId) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cdf = null;
        if(createdDateFrom!=null)
            cdf = LocalDateTime.parse(createdDateFrom, formatter);

        LocalDateTime cdt = null;
        if(createdDateTo!=null)
            cdt = LocalDateTime.parse(createdDateTo, formatter);

        List<VenteRepository.ProductSales> list;
        int n = 20;
        if(sellerId == 0){
            list = venteRepository.findProductSalesByDateRange(cdf, cdt);
        }
        else{
            list = venteRepository.findProductSalesBySellerIdAndDateRange(sellerId, cdf, cdt);
        }
        if(list.size() < 20)
            n = list.size()-1 ;
        return list.subList(0, n);
    }

    public String createXlsFile(Long storeId, String createdDateFrom, String createdDateTo, Long sellerId) throws IOException {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cdf = null;
        if(createdDateFrom!=null) cdf = LocalDateTime.parse(createdDateFrom, formatter);

        LocalDateTime cdt = null;
        if(createdDateTo!=null) cdt = LocalDateTime.parse(createdDateTo, formatter);

        List<Vente> ventes;
        if(sellerId == 0){
            ventes = venteRepository.findByCreatedDateBetween(cdf, cdt);
            Map<String, List<Vente>> groupedBySeller = groupBySeller(ventes);
            //return createXlsFile(groupedBySeller);
            return createXlsFile(ventes, null);
        }
        else {
            ventes = venteRepository.findBySellerIdAndCreatedDateBetween(sellerId, cdf, cdt);
            User seller = userRepository.findByUserId(sellerId);
            return createXlsFile(ventes, seller);
        }
    }

    private String createXlsFile(List<Vente> ventes, User seller)  throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        String sellerName = "All";
        if(seller != null)
            sellerName = seller.getUsername();

        /*if(seller.getName() != null)
            sellerName = StringUtils.stripAccents(seller.getName().replace(" ", "_").toLowerCase());*/
        XSSFSheet sheet = workbook.createSheet();

        sheet.setColumnWidth(0, 15*256);
        sheet.setColumnWidth(1, 15*256);
        sheet.setColumnWidth(3, 50*256);

        this.fillSheetInformations(sheet, ventes);

        try {
            FileOutputStream outputStream = new FileOutputStream(FOLDER + "test" + ".xlsx");
            workbook.write(outputStream);
            workbook.close();
            return FOLDER + "test" + ".xlsx";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void fillSheetInformations(XSSFSheet sheet, List<Vente> ventes) {
        int rowNum = 0;
        Row row = sheet.createRow(rowNum++);
        int colNum = 0;
        row.createCell(colNum++).setCellValue("Date");
        row.createCell(colNum++).setCellValue("Client");
        row.createCell(colNum++).setCellValue("Téléphone");
        row.createCell(colNum++).setCellValue("Produits");
        row.createCell(colNum++).setCellValue("Montant");
        //row.createCell(colNum++).setCellValue("Type paiement");
        //row.createCell(colNum++).setCellValue("Acompte");
        //row.createCell(colNum++).setCellValue("Reste à payer");

        double totalVentes = 0;
        double totalAcompte = 0;
        double totalReste = 0;
        for (Vente vente : ventes){
            row = sheet.createRow(rowNum++);
            colNum = 0;
            row.createCell(colNum++).setCellValue(vente.getCreatedDate());
            row.createCell(colNum++).setCellValue(vente.getCustomer() != null ? vente.getCustomer().getName() : "");
            row.createCell(colNum++).setCellValue(vente.getCustomer() != null ? vente.getCustomer().getTelephone() : "");
            String items = "";
            for(OrderedProduct op: vente.getOrderedProducts()) {
                items += op.getQuantity() + "x " + op.getProduct().getName() + ", ";
            }
            row.createCell(colNum++).setCellValue(items.replaceAll(", $", ""));

            row.createCell(colNum++).setCellValue(Math.round(vente.getPrixTotal()));
            //row.createCell(colNum++).setCellValue(vente.getTypePaiement().toValue());
            //row.createCell(colNum++).setCellValue(Math.round(vente.getAcompte()));
            //row.createCell(colNum++).setCellValue(Math.round(vente.getPrixTotal() - vente.getAcompte()));
            totalVentes += vente.getPrixTotal();
            totalAcompte += vente.getAcompte();
        }

        row = sheet.createRow(rowNum++);
        colNum = 0;
        row.createCell(colNum++).setCellValue("");
        row.createCell(colNum++).setCellValue("");
        row.createCell(colNum++).setCellValue("");
        row.createCell(colNum++).setCellValue("Total");
        row.createCell(colNum++).setCellValue(Math.round(totalVentes));
        row.createCell(colNum++).setCellValue("");
        //row.createCell(colNum++).setCellValue(Math.round(totalAcompte));
        //row.createCell(colNum++).setCellValue(Math.round(totalVentes - totalAcompte));

    }


    public String createXlsFileRecouvrements(Long storeId) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        sheet.setColumnWidth(0, 15*256);
        sheet.setColumnWidth(1, 15*256);
        sheet.setColumnWidth(3, 50*256);

        List<Vente> ventes = venteRepository.findRecouvrements();

        this.fillSheetInformations(sheet, ventes);

        try {
            FileOutputStream outputStream = new FileOutputStream(FOLDER + "test" + ".xlsx");
            workbook.write(outputStream);
            workbook.close();
            return FOLDER + "test" + ".xlsx";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    private String createXlsFile(Map<String, List<Vente>> groupedBySeller) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        for (Map.Entry<String, List<Vente>> entry : groupedBySeller.entrySet()) {
            String sellerName = entry.getKey();
            User seller = userRepository.findByUsername(entry.getKey());
            if(seller != null){
                if(seller.getName() != null){
                    sellerName = seller.getName();
                }
            }
            XSSFSheet sheet = workbook.createSheet(StringUtils.stripAccents(sellerName));

            sheet.setColumnWidth(0, 15*256);
            sheet.setColumnWidth(1, 50*256);

            this.fillSheetInformations(sheet, entry.getValue());
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(FOLDER + "test" + ".xlsx");
            workbook.write(outputStream);
            workbook.close();
            return FOLDER + "test" + ".xlsx";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String createXlsFileBestProducts(Long storeId, String createdDateFrom, String createdDateTo)  throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        sheet.setColumnWidth(0, 20*256);

        List<VenteRepository.ProductSales> productSales = findProductSalesByDateRange(storeId, createdDateFrom, createdDateTo, (long) 0);
        this.fillSheetInformationsForBestProducts(sheet, productSales);

        try {
            FileOutputStream outputStream = new FileOutputStream(FOLDER + "test" + ".xlsx");
            workbook.write(outputStream);
            workbook.close();
            return FOLDER + "test" + ".xlsx";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void fillSheetInformationsForBestProducts(XSSFSheet sheet, List<VenteRepository.ProductSales> productSales) {
        int rowNum = 0;
        Row row = sheet.createRow(rowNum++);
        int colNum = 0;
        row.createCell(colNum++).setCellValue("Produit");
        row.createCell(colNum++).setCellValue("Quantité");
        row.createCell(colNum++).setCellValue("Total");
        row.createCell(colNum++).setCellValue("Bénéfice");

        for (VenteRepository.ProductSales ps : productSales){
            row = sheet.createRow(rowNum++);
            colNum = 0;
            row.createCell(colNum++).setCellValue(ps.getProductName());
            row.createCell(colNum++).setCellValue(ps.getQuantity());
            row.createCell(colNum++).setCellValue(Math.round(ps.getTotalVentes()));
            row.createCell(colNum++).setCellValue(Math.round(ps.getBenefice()));
        }
    }

    public String createXlsFileBestSellers(Long storeId, String createdDateFrom, String createdDateTo) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        sheet.setColumnWidth(0, 20*256);

        List<SellerSale> sellerSales =  findSellerSalesByDateRange(storeId, createdDateFrom, createdDateTo);
        this.fillSheetInformationsForBestSellers(sheet, sellerSales);

        try {
            FileOutputStream outputStream = new FileOutputStream(FOLDER + "test" + ".xlsx");
            workbook.write(outputStream);
            workbook.close();
            return FOLDER + "test" + ".xlsx";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void fillSheetInformationsForBestSellers(XSSFSheet sheet, List<SellerSale> sellerSales) {
        int rowNum = 0;
        Row row = sheet.createRow(rowNum++);
        int colNum = 0;
        row.createCell(colNum++).setCellValue("Vendeur");
        row.createCell(colNum++).setCellValue("Nb ventes");
        row.createCell(colNum++).setCellValue("Total");
        row.createCell(colNum++).setCellValue("Bénéfice");

        for (SellerSale ss : sellerSales){
            row = sheet.createRow(rowNum++);
            colNum = 0;
            row.createCell(colNum++).setCellValue(ss.getSeller());
            row.createCell(colNum++).setCellValue(ss.getNbVentes());
            row.createCell(colNum++).setCellValue(Math.round(ss.getTotalVentes()));
            row.createCell(colNum++).setCellValue(Math.round(ss.getBenefice()));
        }
    }

    @Data
    public class DailySale {
        double benefice;
        double totalVentes;
        String date;

        public DailySale(String date, double totalVentes, double benefice) {
            this.date = date;
            this.totalVentes = totalVentes;
            this.benefice = benefice;
        }
    }

    @Data
    public class HourlySale {
        double benefice;
        double totalVentes;
        int hour;

        public HourlySale(int hour, double totalVentes, double benefice) {
            this.hour = hour;
            this.totalVentes = totalVentes;
            this.benefice = benefice;
        }
    }

    @Data
    public class SellerSale implements Comparable< SellerSale > {
        Integer nbVentes;
        Double benefice;
        Double totalVentes;
        String seller;

        public SellerSale(String seller, int nbVentes, double totalVentes, double benefice) {
            this.seller = seller;
            this.nbVentes = nbVentes;
            this.totalVentes = totalVentes;
            this.benefice = benefice;
        }

        @Override
        public int compareTo(SellerSale o) {
            return this.getTotalVentes().compareTo(o.getTotalVentes());
        }
    }

    public List<DailySale> findDailySalesByDateRange(Long storeId, String createdDateFrom, String createdDateTo, Long sellerId) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cdf = null;
        if(createdDateFrom!=null)
            cdf = LocalDateTime.parse(createdDateFrom, formatter);

        LocalDateTime cdt = null;
        if(createdDateTo!=null)
            cdt = LocalDateTime.parse(createdDateTo, formatter);

        int days = Days.daysBetween(cdf, cdt).getDays()+1;
        List<Vente> ventes = new ArrayList<Vente>(days);
        List<DailySale> dailySales = new ArrayList<DailySale>(days);

        for (int i=0; i < days; i++) {
            LocalDateTime d = cdf.withFieldAdded(DurationFieldType.days(), i);

            if(sellerId == 0)
                ventes = venteRepository.findByCreatedDateBetween(d, d.plusHours(23).plusMinutes(59));
            else
                ventes = venteRepository.findBySellerIdAndCreatedDateBetween(sellerId, d, d.plusHours(23).plusMinutes(59));


            double totalVentes = 0;
            double benefice = 0;
            for(Vente v: ventes){
                //totalVentes += (v.getPrixVente() * v.getQuantity());
                //benefice += (v.getQuantity() * (v.getPrixVente() - v.getProduct().getCump()));
                totalVentes += (v.getPrixTotal());
                for(OrderedProduct op: v.getOrderedProducts()){
                    benefice += (op.getQuantity() * (op.getPrixVente() - op.getProduct().getCump()));
                }
            }
            dailySales.add(new DailySale(d.toString("dd-MM-yyyy"), totalVentes, benefice));
        }
        return dailySales;
    }

    public List<DailySale> findDailySalesByProductAndDateRange(Long productId, String createdDateFrom, String createdDateTo, Long sellerId) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cdf = null;
        if(createdDateFrom!=null)
            cdf = LocalDateTime.parse(createdDateFrom, formatter);

        LocalDateTime cdt = null;
        if(createdDateTo!=null)
            cdt = LocalDateTime.parse(createdDateTo, formatter);

        int days = Days.daysBetween(cdf, cdt).getDays()+1;
        List<OrderedProduct> orderedProducts = new ArrayList<OrderedProduct>(days);
        List<DailySale> dailySales = new ArrayList<DailySale>(days);

        for (int i=0; i < days; i++) {
            LocalDateTime d = cdf.withFieldAdded(DurationFieldType.days(), i);
            if(sellerId == 0)
                orderedProducts = orderedProductRepository.findByProductIdAndCreatedDateBetween(productId, d, d.plusHours(23).plusMinutes(59));
            else
                orderedProducts = orderedProductRepository.findByProductIdAndSellerIdCreatedDateBetween(productId, sellerId, d, d.plusHours(23).plusMinutes(59));

            double totalVentes = 0;
            double benefice = 0;
            for(OrderedProduct op: orderedProducts){
                totalVentes += (op.getPrixVente() * op.getQuantity());
                benefice += (op.getQuantity() * (op.getPrixVente() - op.getProduct().getCump()));
            }
            dailySales.add(new DailySale(d.toString("dd-MM-yyyy"), totalVentes, benefice));
        }
        return dailySales;
    }


    public List<HourlySale> findHourlySalesByDateRange(Long storeId, String createdDateFrom, String createdDateTo, Long sellerId) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cdf = null;
        if(createdDateFrom!=null)
            cdf = LocalDateTime.parse(createdDateFrom, formatter);

        LocalDateTime cdt = null;
        if(createdDateTo!=null)
            cdt = LocalDateTime.parse(createdDateTo, formatter);

        List<Vente> ventes;
        if(sellerId == 0)
            ventes = venteRepository.findByCreatedDateBetween(cdf, cdt);
        else
            ventes = venteRepository.findBySellerIdAndCreatedDateBetween(sellerId, cdf, cdt);

        Map<Integer, List<Vente>> groupedByHour = groupByHour(ventes);

        List<HourlySale> hourlySales = new ArrayList<HourlySale>(24);
        for (Map.Entry<Integer, List<Vente>> entry : groupedByHour.entrySet()) {
            double totalVentes = 0;
            double benefice = 0;
            for(Vente v: entry.getValue()){
                totalVentes += (v.getPrixTotal());
                for(OrderedProduct op: v.getOrderedProducts()){
                    benefice += (op.getQuantity() * (op.getPrixVente() - op.getProduct().getCump()));
                }
            }
            hourlySales.add(new HourlySale(entry.getKey(), totalVentes, benefice));
        }

        return hourlySales;
    }

    public List<SellerSale> findSellerSalesByDateRange(Long storeId, String createdDateFrom, String createdDateTo) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cdf = null;
        if(createdDateFrom!=null)
            cdf = LocalDateTime.parse(createdDateFrom, formatter);

        LocalDateTime cdt = null;
        if(createdDateTo!=null)
            cdt = LocalDateTime.parse(createdDateTo, formatter);

        List<Vente> ventes = venteRepository.findByCreatedDateBetween(cdf, cdt);
        Map<String, List<Vente>> groupedBySeller = groupBySeller(ventes);

        List<SellerSale> sellerSales = new ArrayList<SellerSale>();
        for (Map.Entry<String, List<Vente>> entry : groupedBySeller.entrySet()) {
            double totalVentes = 0;
            double benefice = 0;
            for(Vente v: entry.getValue()){
                totalVentes += (v.getPrixTotal());
                for(OrderedProduct op: v.getOrderedProducts()){
                    benefice += (op.getQuantity() * (op.getPrixVente() - op.getProduct().getCump()));
                }
            }
            String sellerName = entry.getKey();
            User seller = userRepository.findByUsername(entry.getKey());
            if(seller != null){
                if(seller.getName() != null){
                    sellerName = seller.getName();
                }
            }
            sellerSales.add(new SellerSale(sellerName, entry.getValue().size(), totalVentes, benefice));
        }

        //return sellerSales;
        Collections.sort(sellerSales, Collections.reverseOrder());
        return sellerSales;
    }


    public Map<Integer, List<Vente>> groupByHour(List<Vente> list) {
        Map<Integer, List<Vente>> map = new TreeMap<Integer, List<Vente>>();
        for(int i=1; i<=24; i++){
            map.put(i, new ArrayList<Vente>());
        }
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        for (Vente vente : list) {
            Integer hour = LocalDateTime.parse(vente.getCreatedDate(), formatter).getHourOfDay();
            List<Vente> group = map.get(hour);
            if (group == null) {
                group = new ArrayList();
                map.put(hour, group);
            }
            group.add(vente);
        }
        return map;
    }

    public Map<String, List<Vente>> groupBySeller(List<Vente> list) {
        Map<String, List<Vente>> map = new TreeMap<String, List<Vente>>();
        for (Vente vente : list) {
            String seller = vente.getUser().getUsername();
            List<Vente> group = map.get(seller);
            if (group == null) {
                group = new ArrayList();
                map.put(seller, group);
            }
            group.add(vente);
        }
        return map;
    }
    /**
     *  Get one vente by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public VenteDto findOne(Long id) {
        log.debug("Request to get Vente : {}", id);
        Vente vente = venteRepository.findByVenteId(id);
        return new VenteDto().createDTO(vente);
    }

    /**
     *  Delete the  vente by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) throws CustomErrorType {
        log.debug("Request to delete Vente : {}", id);
        Vente vente = venteRepository.findByVenteId(id);
        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime oneDayAgo = new LocalDateTime().minusHours(24);
        LocalDateTime dateVente = LocalDateTime.parse(vente.getCreatedDate(), formatter);

//        if(dateVente.isBefore(oneDayAgo) && !SecurityUtils.isCurrentUserInRole("ROLE_ADMIN"))
//            throw new CustomErrorType("Vous ne pouvez plus supprimer cette vente");

//        if(Optional.ofNullable(vente).isPresent()){
//            if(!vente.getUser().getUsername().equals(SecurityUtils.getCurrentUserLogin())
//                    && !SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")
//            ){
//                throw new AccessDeniedException("You should not do this !");
//            }
//            vente.setDeleted(true);
//            venteRepository.save(vente);
//        }
    }

    public void deleteCancel(Long id) {
        log.debug("Request to delete Vente : {}", id);
        Vente vente = venteRepository.findByVenteId(id);
        if(Optional.ofNullable(vente).isPresent()){
//            if(!vente.getUser().getUsername().equals(SecurityUtils.getCurrentUserLogin())
//                    && !SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")
//                    && !SecurityUtils.isCurrentUserInRole("ROLE_USER")
//            ){
//                throw new AccessDeniedException("You should not do this !");
//            }VenteController
            vente.setDeleted(false);
            venteRepository.save(vente);
        }
    }

    public void deleteConfirm(Long id) {
        log.debug("Request to delete Vente : {}", id);
        Vente vente = venteRepository.findByVenteId(id);
        if(Optional.ofNullable(vente).isPresent()){
//            if(!vente.getUser().getUsername().equals(SecurityUtils.getCurrentUserLogin())
//                    && !SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")
//                    && !SecurityUtils.isCurrentUserInRole("ROLE_USER")
//            ){
//                throw new AccessDeniedException("You should not do this !");
//            }
            for(OrderedProduct op: vente.getOrderedProducts()){
                productStockService.restoreStockVente(op);
            }
            venteRepository.deleteById(id);
        }
    }
}
