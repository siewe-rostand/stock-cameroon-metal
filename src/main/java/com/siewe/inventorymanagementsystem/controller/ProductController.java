package com.siewe.inventorymanagementsystem.controller;


import com.siewe.inventorymanagementsystem.dto.ProductDto;
import com.siewe.inventorymanagementsystem.service.ProductService;
import com.siewe.inventorymanagementsystem.utils.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping("/api/products")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) throws URISyntaxException {
        log.debug("REST request to save Product : {}", productDto);
        if (productDto.getId() != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A product with id " +
                    productDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }
        ProductDto result = productService.save(productDto);
        return new ResponseEntity<ProductDto>(result, HttpStatus.CREATED);

    }

    @PutMapping("/api/products")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto) throws URISyntaxException {
        log.debug("REST request to update Product : {}", productDto);
        if (productDto.getId() == null) {
            return createProduct(productDto);
        }
        return productService.update(productDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/api/products-with-image", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseBody
    public ResponseEntity<ProductDto> createProductWithImage(@RequestPart("product") ProductDto productDto,
                                                             @RequestPart(name="file", required=false) MultipartFile file,
                                                             @RequestParam(name="thumb", required=false) MultipartFile thumb) throws IOException {

        log.debug("REST request to save Product : {}", productDto);
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
     * or with status 500 (Internal Server Error) if the product couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/api/products-with-image", method = RequestMethod.PUT, consumes = {"multipart/form-data"})
    @ResponseBody
    public ResponseEntity<ProductDto> updateProduct(@RequestPart("product") ProductDto productDto,
                                                    @RequestParam(name="file", required=false) MultipartFile file,
                                                    @RequestParam(name="thumb", required=false) MultipartFile thumb) throws URISyntaxException, IOException {
        log.debug("REST request to update Product : {}", productDto);
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

    @GetMapping("/api/products")
    public Page<ProductDto> getAllProducts(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "product", defaultValue = "") String product,
            @RequestParam(name = "stockBas") boolean stockBas) {
        log.debug("REST request to get page of Products by store");
        return productService.findAll(page, size, sortBy, direction, product, stockBas);
    }

    @GetMapping("/api/products-by-category/{categoryId}")
    public Page<ProductDto> getAllProductsByCategory(@PathVariable Long categoryId,
                                                     @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "size", defaultValue = "5") Integer size,
                                                     @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
                                                     @RequestParam(name = "direction", defaultValue = "asc") String direction,
                                                     @RequestParam(name = "product", defaultValue = "") String product,
                                                     @RequestParam(name = "stockBas") boolean stockBas) {
        log.debug("REST request to get page of Products by store");
        return productService.findAllByCategory(page, size, sortBy, direction, product, categoryId, stockBas);
    }

    @GetMapping("/api/products-enabled")
    public Page<ProductDto> getAllProductsEnabled(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "9999") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        log.debug("REST request to get page of Enabled Products");
        return productService.findByEnabledTrue(page, size, sortBy, direction);
    }

    @GetMapping("/api/products-search")
    public Map<String, List<ProductDto>> getProductsByMc(@RequestParam(name = "mc") String mc) {
        log.debug("REST request to get Products");
        Map<String, List<ProductDto>> map = new HashMap<>();
        map.put("results", productService.findByMc(mc));
        return map;
    }

    @PostMapping("/api/products-import")
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
    @GetMapping("/api/products/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        log.debug("REST request to get Product : {}", id);
        ProductDto productDto = productService.findOne(id);

        return Optional.ofNullable(productDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @RequestMapping(value="/api/product-image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getProductImage(@PathVariable("id") Long productId) throws IOException {
        return productService.getProductImage(productId);
    }

    @RequestMapping(value = "/api/product-image-thumb/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getProductImageThumb(@PathVariable("id") Long id) throws IOException {
        return productService.findThumbById(id);
    }


    /**
     * DELETE  /products/:id : delete the "id" product.
     *
     * @param id the id of the product to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/api/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        log.debug("REST request to delete Product : {}", id);
        productService.delete(id);
        return new ResponseEntity<ProductDto>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/api/products-delete-all")
    public ResponseEntity<?> deleteAllProduct() {
        log.debug("REST request to delete Product : {}");
        productService.deleteAll();
        return new ResponseEntity<ProductDto>(HttpStatus.NO_CONTENT);
    }


}
