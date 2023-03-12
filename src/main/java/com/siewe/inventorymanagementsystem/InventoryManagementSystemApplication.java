package com.siewe.inventorymanagementsystem;

import com.siewe.inventorymanagementsystem.service.ProductService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@Configuration
public class InventoryManagementSystemApplication {


//    @Autowired
//    private  initialDataSet;
    public static void main(String[] args) {
        SpringApplication.run(InventoryManagementSystemApplication.class, args);
    }



//    @Bean
//    InitializingBean init() {
//        return initialDataSet.load();
//    }


}
