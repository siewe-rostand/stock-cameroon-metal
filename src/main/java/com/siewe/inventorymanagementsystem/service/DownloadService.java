package com.siewe.inventorymanagementsystem.service;

import com.lowagie.text.DocumentException;
import com.siewe.inventorymanagementsystem.dto.OrderDto;
import com.siewe.inventorymanagementsystem.model.Customer;
import com.siewe.inventorymanagementsystem.model.Orders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.persistence.EntityManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@Service
public class DownloadService {

    private final Logger log = LoggerFactory.getLogger(DownloadService.class);

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private OrderService orderService;

    public ByteArrayInputStream exportReceiptPdf(String templateName,Long orderId) {
        Context context = new Context();
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        OrderDto orderDto = orderService.findOne(orderId);
        context.setVariable("customer",orderDto.getCustomer());
        String date = simpleDateFormat.format(new Date());
        context.setVariable("date",date);
        context.setVariable("userName",orderDto.getUsername());
        context.setVariable("products",orderDto.getProducts());
        String htmlContent = templateEngine.process(templateName, context);

        ByteArrayInputStream byteArrayInputStream = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(byteArrayOutputStream, false);
            renderer.finishPDF();
            byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (DocumentException e) {
            log.error(e.getMessage(), e);
        }

        return byteArrayInputStream;
    }
}
