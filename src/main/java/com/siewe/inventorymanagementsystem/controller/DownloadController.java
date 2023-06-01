package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.service.DownloadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class DownloadController {

    @Autowired
    private DownloadService downloadService;

    @Value("${dir.pharma}")
    private String PHARMA_FOLDER;

    @GetMapping("/api/download/invoice")
    public ResponseEntity<byte[]> getInvoice(@RequestParam(name = "vente") Long venteId) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        String FILE_PATH = downloadService.createInvoice(venteId);
        headers.setContentType(MediaType.parseMediaType("application/pdf"));

        Path path = Paths.get(FILE_PATH);
        byte[] data = Files.readAllBytes(path);
        String filename = StringUtils.stripAccents(path.getFileName().toString());
        filename = filename.replace(" ", "_");
        //String filename = path.getFileName().toString();

        headers.set(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=" + filename.toLowerCase());
        //headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
        return response;
    }
}

