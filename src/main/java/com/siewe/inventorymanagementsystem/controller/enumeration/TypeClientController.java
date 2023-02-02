package com.siewe.inventorymanagementsystem.controller.enumeration;


import com.siewe.inventorymanagementsystem.model.enumeration.TypeClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TypeClientController {

    @RequestMapping(value = "/typeClients", method = RequestMethod.GET)
    public List<TypeClientDto> getTypeClients( ) {
        List<TypeClientDto> result = new ArrayList<>();

        List<TypeClient> typeClients = Arrays.asList(TypeClient.values());

        for(TypeClient typeClient : typeClients){
            result.add(new TypeClientDto(typeClient.name(), typeClient.toValue()));
        }

        return result;
    }

    private class TypeClientDto {
        private String name;
        private String value;

        public TypeClientDto(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}
