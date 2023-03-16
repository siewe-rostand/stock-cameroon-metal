package com.siewe.inventorymanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
