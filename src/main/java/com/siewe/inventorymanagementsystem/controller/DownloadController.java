package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.model.Customer;
import com.siewe.inventorymanagementsystem.model.Invoice;
import com.siewe.inventorymanagementsystem.service.DownloadService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DownloadController {

    @Autowired
    private DownloadService downloadService;

    @GetMapping("/downloadReceipt")
    public void downloadReceipt(HttpServletResponse response) throws IOException {
        Map<String, Object> data = createTestData();
        ByteArrayInputStream exportedData = downloadService.exportReceiptPdf("invoice", data);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=receipt.pdf");
        IOUtils.copy(exportedData, response.getOutputStream());
    }

    private Map<String, Object> createTestData() {
        Map<String, Object> data = new HashMap<>();
        Customer customer = new Customer();
        customer.setName("Simple Solution");
        customer.setAddress("123, Simple Street");
        customer.setEmail("contact@simplesolution.dev");
        customer.setPhone("123 456 789");
        data.put("customer", customer);

        List<Invoice> receiptItems = new ArrayList<>();
        Invoice receiptItem1 = new Invoice();
        receiptItem1.setProductName("Test Item 1");
        receiptItem1.setQuantity(1);
        receiptItem1.setTotal(100.0);
        receiptItems.add(receiptItem1);

        Invoice receiptItem2 = new Invoice();
        receiptItem2.setProductName("Test Item 2");
        receiptItem2.setQuantity(4);
        receiptItem2.setTotal(2000.0);
        receiptItems.add(receiptItem2);

        Invoice receiptItem3 = new Invoice();
        receiptItem3.setProductName("Test Item 3");
        receiptItem3.setQuantity(2);
        receiptItem3.setTotal(400.0);
        receiptItems.add(receiptItem3);

        data.put("receiptItems", receiptItems);
        return data;
    }
}

