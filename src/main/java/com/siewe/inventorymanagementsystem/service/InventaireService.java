package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.InventaireDto;
import com.siewe.inventorymanagementsystem.dto.LigneProductDto;
import com.siewe.inventorymanagementsystem.dto.ManquantDto;
import com.siewe.inventorymanagementsystem.model.Inventaire;
import com.siewe.inventorymanagementsystem.model.LigneProduct;
import com.siewe.inventorymanagementsystem.repository.InventaireRepository;
import com.siewe.inventorymanagementsystem.repository.LigneProductRepository;
import com.siewe.inventorymanagementsystem.repository.ProductRepository;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventaireService {

    private final Logger log = LoggerFactory.getLogger(InventaireService.class);

    @Autowired
    private InventaireRepository inventaireRepository;

    @Autowired
    private LigneProductService ligneProductService;

    @Autowired
    private ManquantService manquantService;

    @Autowired
    private LigneProductRepository ligneProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${dir.pharma}")
    private String FOLDER;

     /*@Autowired
    private ProductInventaireService productInventaireService;*/

    /**
     * Save a inventaire.
     *
     * @param inventaireDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity<InventaireDto> save(InventaireDto inventaireDto) {
        log.debug("Request to save Inventaire : {}", inventaireDto);

        Inventaire inventaire = new Inventaire();
        inventaire.setId(inventaireDto.getId());

        //set created date;
        String pattern = "yyyy-MM-dd HH:mm";
        LocalDateTime datetime = new LocalDateTime(DateTimeZone.forOffsetHours(1));
        inventaire.setCreatedDate(datetime.toString(pattern));

        Inventaire result = inventaireRepository.save(inventaire);
        if (result != null){
            //create LigneProducts
            for (LigneProductDto ligneProductDto : inventaireDto.getLignesProduct() ){
                ligneProductDto.setInventaireId(result.getId());
                ligneProductService.save(ligneProductDto);
            }
        }
        return new ResponseEntity<>(new InventaireDto().createDTO(result), HttpStatus.CREATED);
    }


    public ResponseEntity<InventaireDto> update(InventaireDto inventaireDto) {
        log.debug("Request to save Inventaire : {}", inventaireDto);

        Inventaire inventaire = inventaireRepository.findOne(inventaireDto.getId());

        inventaire.setId(inventaireDto.getId());

        if (inventaireDto.getLignesProduct() != null) {
            for (LigneProductDto ligneProductDto : inventaireDto.getLignesProduct()){
                ligneProductService.update(ligneProductDto);
            }
        }

        Inventaire result = inventaireRepository.save(inventaire);
        return new ResponseEntity<>(new InventaireDto().createDTO(result), HttpStatus.CREATED);
    }

    public ResponseEntity<InventaireDto> validate(Long id) {
        Inventaire inventaire = inventaireRepository.findOne(id);
        if(inventaire == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (inventaire.getLignesProduct() != null) {
            for (LigneProduct ligneProduct : inventaire.getLignesProduct()){
                if(ligneProduct.getStockTheorique() - ligneProduct.getStockPhysique() > 0){
                    ManquantDto manquantDto = new ManquantDto();
                    manquantDto.setQuantity(ligneProduct.getStockTheorique() - ligneProduct.getStockPhysique());
                    manquantDto.setProductId(ligneProduct.getProduct().getId());
                    manquantDto.setCout(ligneProduct.getStockTheorique() - ligneProduct.getStockPhysique() * ligneProduct.getProduct().getCump());
                    manquantService.save(manquantDto);
                }

            }
        }
        String pattern = "yyyy-MM-dd HH:mm";
        LocalDateTime datetime = new LocalDateTime(DateTimeZone.forOffsetHours(1));

        inventaire.setValidated(true);
        inventaire.setValidatedDate(datetime.toString(pattern));
//        inventaire.setValidatedBy(userRepository.findByUsername(SecurityUtils.getCurrentUserLogin()));

        Inventaire result = inventaireRepository.save(inventaire);
        return new ResponseEntity<>(new InventaireDto().createDTO(result), HttpStatus.OK);
    }


    /**
     *  Get all the inventaires.
     *
     *  @return the list of entities
     */

    @Transactional(readOnly = true)
    public List<InventaireDto> findAll() {
        log.debug("Request to get all Inventaires");
        List<Inventaire> inventaires = inventaireRepository.findAll();
        List<InventaireDto> inventaireDtos = new ArrayList<>();
        for(Inventaire c: inventaires){
            inventaireDtos.add(new InventaireDto().createDTO(c));
        }
        return inventaireDtos;
    }


    @Transactional(readOnly = true)
    public InventaireDto findOne(Long id) {
        log.debug("Request to get Inventaire : {}", id);
        Inventaire inventaire = inventaireRepository.findOne(id);
        return new InventaireDto().createDTO(inventaire);
    }

    public void delete(Long id) {
        log.debug("Request to delete Inventaire : {}", id);
        Inventaire inventaire = inventaireRepository.findOne(id);
        if(Optional.ofNullable(inventaire).isPresent()){
            inventaireRepository.deleteById(id);
        }
    }

    public String createXlsFile(InventaireDto inventaireDto) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheetCesep = workbook.createSheet("Inventaire");

        this.fillSheetInformationsCesep(sheetCesep, inventaireDto);

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

    private void fillSheetInformationsCesep(XSSFSheet sheet, InventaireDto inventaireDto) {
        int rowNum = 0;
        Row row = sheet.createRow(rowNum++);
        int colNum = 0;
        row.createCell(colNum++).setCellValue("Produit");
        row.createCell(colNum++).setCellValue("Stock Théorique");
        row.createCell(colNum++).setCellValue("Stock Physique");
        row.createCell(colNum++).setCellValue("Ecart");
        row.createCell(colNum++).setCellValue("Manquant");

        double totalManquant = 0.0;
        for (LigneProductDto ligneProductDto : inventaireDto.getLignesProduct()){
            LigneProduct ligneProduct = ligneProductRepository.findOne(ligneProductDto.getId());
            row = sheet.createRow(rowNum++);
            colNum = 0;
            row.createCell(colNum++).setCellValue(ligneProduct.getProduct().getName());
            row.createCell(colNum++).setCellValue(ligneProduct.getStockTheorique());
            row.createCell(colNum++).setCellValue(ligneProduct.getStockPhysique());
            row.createCell(colNum++).setCellValue(ligneProduct.getStockTheorique()-ligneProduct.getStockPhysique());
            row.createCell(colNum++).setCellValue(Math.round( (ligneProduct.getStockTheorique()-ligneProduct.getStockPhysique()) * ligneProduct.getProduct().getCump()) );
            totalManquant += (ligneProduct.getStockTheorique()-ligneProduct.getStockPhysique()) * ligneProduct.getProduct().getCump();

        }

        row = sheet.createRow(rowNum++);
        colNum = 0;
        row.createCell(colNum++).setCellValue("Total");
        row.createCell(colNum++).setCellValue("");
        row.createCell(colNum++).setCellValue("");
        row.createCell(colNum++).setCellValue("");
        row.createCell(colNum++).setCellValue(Math.round(totalManquant));
    }
}
