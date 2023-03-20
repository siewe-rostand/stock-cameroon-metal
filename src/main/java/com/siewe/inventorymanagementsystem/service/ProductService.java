package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.ApprovisionnementDto;
import com.siewe.inventorymanagementsystem.dto.ProductDto;
import com.siewe.inventorymanagementsystem.model.Category;
import com.siewe.inventorymanagementsystem.model.Product;
import com.siewe.inventorymanagementsystem.repository.CategoryRepository;
import com.siewe.inventorymanagementsystem.repository.ProductRepository;
import com.siewe.inventorymanagementsystem.repository.StockRepository;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.utils.CustomErrorType;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository productStockRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApprovisionnementService approvisionnementService;

    @Value("${dir.pharma}")
    private String FOLDER;

    /**
     * Save a product.
     *
     * @param productDto the entity to save
     * @return the persisted entity
     */
    //@Secured(value = {"ROLE_ADMIN"})
    public ProductDto save(ProductDto productDto){

        log.debug("Request to save Product : {}", productDto);

        Product product = new Product();
        if (productDto.getId() != null) {
            product = productRepository.findByProductId(productDto.getId());
        }

        product.setName(productDto.getName());
        product.setCip(productDto.getCip());
        product.setDescription(productDto.getDescription());

        product.setPrice(0);
        if(productDto.getPrice() != null)
            product.setPrice(productDto.getPrice());

        product.setStockAlerte(productDto.getStockAlerte());

        if(productDto.getStock() != null && productDto.getCump() != null){
            product.setStock(productDto.getStock());
            product.setCump(productDto.getCump());
        }

        product.setEnabled(true);

        //set created date;
        String pattern = "yyyy-MM-dd";
        LocalDate date = new LocalDate(DateTimeZone.forOffsetHours(1));
        product.setCreatedDate(date.toString(pattern));

        if(productDto.getCategoryId() != null){
            Category category = categoryRepository.findOne(productDto.getCategoryId());
            product.setCategory(category);
        }

        Product result = productRepository.save(product);

        if(productDto.getStock() != null && productDto.getCump() != null) {
            if(productDto.getCump() != 0 && productDto.getStock() != 0){
                ApprovisionnementDto approvisionnementDto = new ApprovisionnementDto();
                approvisionnementDto.setQuantity(productDto.getStock());
                approvisionnementDto.setPrixEntree(productDto.getCump());
                approvisionnementDto.setProductId(result.getProductId());
                approvisionnementService.save(approvisionnementDto);
            }
        }

        //return new ResponseEntity<ProductDto>(new ProductDto().createDTO(result), HttpStatus.CREATED);
        return new ProductDto().createDTO(result);
    }

    //@Secured(value = {"ROLE_ADMIN"})
    public ResponseEntity<ProductDto> update(ProductDto productDto){

        log.debug("Request to save Product : {}", productDto);

        Product product = productRepository.findByProductId(productDto.getId());

        product.setName(productDto.getName());
//        product.setCip(productDto.getCip());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStockAlerte(productDto.getStockAlerte());

        product.setEnabled(productDto.getEnabled());
        product.setDeleted(false);

        if(productDto.getCategoryId() != null){
            Category category = categoryRepository.findOne(productDto.getCategoryId());
            product.setCategory(category);
        }

        Product result = productRepository.save(product);
        return new ResponseEntity<ProductDto>(new ProductDto().createDTO(result), HttpStatus.CREATED);
    }


    //@Secured(value = {"ROLE_ADMIN"})
    public ResponseEntity<ProductDto> save(ProductDto productDto,
                                           MultipartFile file, MultipartFile thumb) throws IOException {

        log.debug("Request to save Product : {}", productDto);

        Product product = new Product();
        if (productDto.getId() != null) {
            product = productRepository.findByProductId(productDto.getId());
        }

        product.setName(productDto.getName());
//        product.setCip(productDto.getCip());
        product.setDescription(productDto.getDescription());

        product.setPrice(0);
        if(productDto.getPrice() != null)
            product.setPrice(productDto.getPrice());

        product.setStockAlerte(productDto.getStockAlerte());

        if(productDto.getStock() != null && productDto.getCump() != null){
            product.setStock(productDto.getStock());
            product.setCump(productDto.getCump());
        }

        product.setEnabled(true);

        //set created date;
        String pattern = "yyyy-MM-dd";
        LocalDate date = new LocalDate(DateTimeZone.forOffsetHours(1));
        product.setCreatedDate(date.toString(pattern));

        if(productDto.getCategoryId() != null){
            Category category = categoryRepository.findOne(productDto.getCategoryId());
            product.setCategory(category);
        }

        Product result = productRepository.save(product);

        if(productDto.getStock() != null && productDto.getCump() != null) {
            if(productDto.getCump() != 0 && productDto.getStock() != 0){
                ApprovisionnementDto approvisionnementDto = new ApprovisionnementDto();
                approvisionnementDto.setQuantity(productDto.getStock());
                approvisionnementDto.setPrixEntree(productDto.getCump());
                approvisionnementDto.setProductId(result.getProductId());
                approvisionnementService.save(approvisionnementDto);
            }
        }

        if(file != null && thumb != null){
            if(result != null){
                if (!Files.exists(Paths.get(FOLDER + "products/"))) {
                    File products = new File(FOLDER + "products/");
                    if(! products.mkdirs()) {
                        return new ResponseEntity(new CustomErrorType("Unable to create folder ${dir.images}"), HttpStatus.CONFLICT);
                    }
                }

                if(!file.isEmpty()){
                    try {
                        file.transferTo(new File(FOLDER + "products/" + result.getProductId()));
                        thumb.transferTo(new File(FOLDER + "products/" + result.getProductId() + "_small"));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        return new ResponseEntity(new CustomErrorType("Error while saving product image"), HttpStatus.NO_CONTENT);
                    }
                }
            }
        }
        return new ResponseEntity<ProductDto>(new ProductDto().createDTO(result), HttpStatus.CREATED);
    }

    //@Secured(value = {"ROLE_ADMIN"})
    public ResponseEntity<ProductDto> update(ProductDto productDto,
                                             MultipartFile file, MultipartFile thumb) throws IOException {

        log.debug("Request to save Product : {}", productDto);

        Product product = productRepository.findByProductId(productDto.getId());

        product.setName(productDto.getName());
//        product.setCip(productDto.getCip());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStockAlerte(productDto.getStockAlerte());
        product.setEnabled(productDto.getEnabled());

        if(productDto.getCategoryId() != null){
            Category category = categoryRepository.findOne(productDto.getCategoryId());
            product.setCategory(category);
        }

        //set created date;
        String pattern = "yyyy-MM-dd";
        LocalDate date = new LocalDate(DateTimeZone.forOffsetHours(1));
        product.setCreatedDate(date.toString(pattern));

        Product result = productRepository.save(product);

        if(file != null && thumb != null){
            if(result != null){
                if (!Files.exists(Paths.get(FOLDER + "products/"))) {
                    File products = new File(FOLDER + "products/");
                    if(! products.mkdirs()) {
                        return new ResponseEntity(new CustomErrorType("Unable to create folder ${dir.images}"), HttpStatus.CONFLICT);
                    }
                }

                if(!file.isEmpty()){
                    try {
                        file.transferTo(new File(FOLDER + "products/" + result.getProductId()));
                        thumb.transferTo(new File(FOLDER + "products/" + result.getProductId() + "_small"));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        return new ResponseEntity(new CustomErrorType("Error while saving product image"), HttpStatus.NO_CONTENT);
                    }
                }
            }
        }
        return new ResponseEntity<ProductDto>(new ProductDto().createDTO(result), HttpStatus.CREATED);
    }

    /**
     *  Get all the products by category id.
     *
     *  @return the list of entities
     */
    //@Secured(value = {"ROLE_ADMIN"})
    public Page<ProductDto> findAll(Integer page, Integer size, String sortBy,
                                    String direction, String name, boolean stockBas){
        log.debug("Request to get all Products by Store");

        Pageable pageable =  PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Page<Product> products = null;

        /*if(SecurityUtils.isCurrentUserInRole("ROLE_ADMIN") || SecurityUtils.isCurrentUserInRole("ROLE_USER")){
            if(stockBas)
                products = productRepository.findAllWithStockBas("%"+name+"%", pageable);
            else
                products = productRepository.findAll("%"+name+"%", pageable);
        }
        else{
            if(stockBas)
                products = productRepository.findByEnabledTrueWithStockBas("%"+name+"%", pageable);
            else
                products = productRepository.findByEnabledTrue("%"+name+"%", pageable);
        }*/

        Page<ProductDto> productDtos = products.map(product -> {
            return new ProductDto().createDTO(product);
        });
        return productDtos;
    }

    public Page<ProductDto> findAllByCategory(Integer page, Integer size, String sortBy, String direction, String name, Long categoryId, boolean stockBas) {
        log.debug("Request to get all Products by Store");

        Category cat = categoryRepository.findOne(categoryId);
        if(cat != null){
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
            Page<Product> products = null;

            /*if(SecurityUtils.isCurrentUserInRole("ROLE_ADMIN") || SecurityUtils.isCurrentUserInRole("ROLE_USER")){
                if(stockBas)
                    products = productRepository.findAllByCategoryIdWithStockBas("%"+name+"%", cat.getId(), pageable);
                else
                    products = productRepository.findAllByCategoryId("%"+name+"%", cat.getId(), pageable);
            }
            else{
                if(stockBas)
                    products = productRepository.findByEnabledTrueWithStockBas("%"+name+"%", pageable);
                else
                    products = productRepository.findByEnabledTrue("%"+name+"%", pageable);
            }*/

            Page<ProductDto> productDtos = products.map(product -> {
                return new ProductDto().createDTO(product);
            });
            return productDtos;

        }
        return null;
    }


    public Page<ProductDto> findByEnabledTrue(Integer page, Integer size, String sortBy,
                                              String direction) {
        log.debug("Request to get all Products by Store");

        Pageable pageable =  PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Page<Product> products = null;
        products = productRepository.findByEnabledTrue(pageable);

        Page<ProductDto> productDtos = products.map(product -> {
            /*ProductStock productStock = productStockRepository.findFirstByProductIdOrderByDateDesc(product.getId());
            if(productStock != null){
                return new ProductDto().createDTO(product, productStock.getStock(), productStock.getCump());
            }*/
            return new ProductDto().createDTO(product);
        });
        return productDtos;
    }

    public List<ProductDto> findByMc(String mc) {

        List<Product> products = productRepository.findByMc("%"+mc+"%");
        List<ProductDto> productDtos = new ArrayList<>();

        for (Product product : products)
            productDtos.add(new ProductDto().createDTO(product));

        return productDtos;
    }

    public List<ProductDto> saveMany(List<ProductDto> productDtos) {
        ArrayList<ProductDto> cDtos = new ArrayList<>();
        for (ProductDto productDto : productDtos)
            cDtos.add(this.save(productDto));

        return cDtos;
    }

    @Transactional(readOnly = true)
//    @Secured(value = {"ROLE_ADMIN"})
    public List<ProductDto> findByCategoryId(Long id) {
        log.debug("Request to get Products by category");
        List<Product> products = productRepository.findByCategory(id);

        List<ProductDto> productDtos = new ArrayList<>();

        for (Product product : products)
            productDtos.add(new ProductDto().createDTO(product));

        return productDtos;
    }

    /**
     *  Get one product by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ProductDto findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        Product product = productRepository.findByProductId(id);

        /*ProductStock productStock = productStockRepository.findFirstByProductIdOrderByDateDesc(product.getId());
        if(productStock != null){
            return new ProductDto().createDTO(product, productStock.getStock(), productStock.getCump());
        }*/
        return new ProductDto().createDTO(product);
    }

    /**
     *  Delete the  product by id.
     *
     *  @param id the id of the entity
     */
    //@Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        Product product = productRepository.findByProductId(id);

        if(Optional.ofNullable(product).isPresent()){
            if(product.getApprovisionnements() != null){
                product.setDeleted(true);
                productRepository.save(product);
            }else {
                productRepository.deleteByProductId(id);
            }
        }
    }

    public void deleteAll() {
        List<Product> products = productRepository.findAll();
        for (Product product : products){
            if(product.getApprovisionnements() != null){
                product.setDeleted(true);
                productRepository.save(product);
            }else {
                productRepository.deleteByProductId(product.getProductId());
            }
        }
    }

    public byte[] getProductImage(Long productId) throws IOException {
        File f = new File(FOLDER + "products/" + productId);
        if(f.exists() && !f.isDirectory()) {
            return IOUtils.toByteArray(new FileInputStream(f));
        }
        f = new File(FOLDER + "products/" + "no_image.png");
        if(f.exists() && !f.isDirectory()) {
            return IOUtils.toByteArray(new FileInputStream(f));
        }
        return null;
    }

    public byte[] findThumbById(Long productId) throws IOException {
        File f = new File(FOLDER + "products/" + productId + "_small");
        if(f.exists() && !f.isDirectory()) {
            return IOUtils.toByteArray(new FileInputStream(f));
        }
        f = new File(FOLDER + "products/" + "no_image.png");
        if(f.exists() && !f.isDirectory()) {
            return IOUtils.toByteArray(new FileInputStream(f));
        }
        return null;
    }


    public Product findByName(String name) {
        return productRepository.findByName(name);
    }
}