package com.siewe.inventorymanagementsystem.controller;


import com.siewe.inventorymanagementsystem.dto.ProductDto;
import com.siewe.inventorymanagementsystem.service.ProductService;
import com.siewe.inventorymanagementsystem.utils.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing Product.
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
public class ProductController {
    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    /**
     * POST  /products : Create a new product.
     *
     * @param productDto the product to create
     * @return the ResponseEntity with status 201 (Created) and with body the new product, or with status 400 (Bad Request) if the product has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) throws URISyntaxException {
        log.debug("REST request to save Product : {}", productDto);
        if (productDto.getId() != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A product with id " +
                    productDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }
        ProductDto result = productService.save(productDto);
        return new ResponseEntity<ProductDto>(result, HttpStatus.CREATED);

    }

    @PutMapping("/products")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto) throws URISyntaxException {
        log.debug("REST request to update Product : {}", productDto);
        if (productDto.getId() == null) {
            return createProduct(productDto);
        }
        return productService.update(productDto);
    }

    /**
     * post a product with image
     * @param productDto
     * @param file
     * @param thumb
     * @return
     * @throws IOException
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/products-with-image", consumes = {"multipart/form-data"})
    @ResponseBody
    public ResponseEntity<ProductDto> createProductWithImage(@RequestPart("product") ProductDto productDto,
                                                             @RequestPart(name="file", required=false) MultipartFile file,
                                                             @RequestParam(name="thumb", required=false) MultipartFile thumb) throws IOException {

        log.debug("REST request to save Product with image : {}", productDto);
        if (productDto.getId() != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A product with id " +
                    productDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }

        return productService.save(productDto, file, thumb);
    }


    /**
     * PUT  /products : Updates an existing product.
     *
     * @param productDto the product to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated product,
     * or with status 400 (Bad Request) if the product is not valid,
     * or with status 500 (Internal Server Error) if the product couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/products-with-image", method = RequestMethod.PUT, consumes = {"multipart/form-data"})
    @ResponseBody
    public ResponseEntity<ProductDto> updateProduct(@RequestPart("product") ProductDto productDto,
                                                    @RequestParam(name="file", required=false) MultipartFile file,
                                                    @RequestParam(name="thumb", required=false) MultipartFile thumb) throws URISyntaxException, IOException {
        log.debug("REST request to update Product with image : {}", productDto);
        if(productDto.getId() != null)
            return productService.update(productDto, file, thumb);
        else
            return productService.save(productDto, file, thumb);
    }

    /**
     * GET  /products : get all the products.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of products in body
     */

    @GetMapping("/products")
    public Page<ProductDto> getAllProducts(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "50") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "product", defaultValue = "") String product,
            @RequestParam(name = "stockBase") boolean stockBas) {
        log.debug("REST request to get page of Products by store");
        return productService.findAll(page, size, sortBy, direction, product, stockBas);
    }

    @GetMapping("/products-by-category/{categoryId}")
    public Page<ProductDto> getAllProductsByCategory(@PathVariable Long categoryId,
                                                     @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "size", defaultValue = "5") Integer size,
                                                     @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
                                                     @RequestParam(name = "direction", defaultValue = "asc") String direction,
                                                     @RequestParam(name = "product", defaultValue = "") String product,
                                                     @RequestParam(name = "stockBase") boolean stockBas) {
        log.debug("REST request to get page of Products by store");
        return productService.findAllByCategory(page, size, sortBy, direction, product, categoryId, stockBas);
    }

    @GetMapping("/products-enabled")
    public Page<ProductDto> getAllProductsEnabled(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "9999") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        log.debug("REST request to get page of Enabled Products");
        return productService.findByEnabledTrue(page, size, sortBy, direction);
    }

    @GetMapping("/products-search")
    public Map<String, List<ProductDto>> getProductsByMc(@RequestParam(name = "mc") String mc) {
        log.debug("REST request to get Products");
        Map<String, List<ProductDto>> map = new HashMap<>();
        map.put("results", productService.findByKeyword(mc));
        return map;
    }

    @PostMapping("/products-import")
    public ResponseEntity<List<ProductDto>> createManyProduct(@Valid @RequestBody List<ProductDto> productDtos) throws URISyntaxException {
        log.debug("REST request to save Product : {}", productDtos);

        List<ProductDto> result = productService.saveMany(productDtos);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }


    /**
     * GET  /products/:id : get the "id" product.
     *
     * @param id the id of the product to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the product, or with status 404 (Not Found)
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        log.debug("REST request to get Product : {}", id);
        ProductDto productDto = productService.findOne(id);

        return Optional.ofNullable(productDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }



    /**
     * DELETE  /products/:id : delete the "id" product.
     *
     * @param id the id of the product to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        log.debug("REST request to delete Product : {}", id);
        productService.delete(id);
        return new ResponseEntity<ProductDto>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/products-delete-all")
    public ResponseEntity<?> deleteAllProduct() {
        log.debug("REST request to delete Product : {}");
        productService.deleteAll();
        return new ResponseEntity<ProductDto>(HttpStatus.NO_CONTENT);
    }


    /**
     * get a particular product photo
     * @param fileName
     * @param request
     * @return
     */
    @GetMapping(value = "/product-image/{fileName:.+}",produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = productService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

