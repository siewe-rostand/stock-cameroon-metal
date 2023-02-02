package com.siewe.inventorymanagementsystem.controller.enumeration;

import com.siewe.inventorymanagementsystem.model.enumeration.Civilite;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CiviliteController {

    @RequestMapping(value = "/civilites", method = RequestMethod.GET)
    public List<CiviliteDto> getCivilites( ) {
        List<CiviliteDto> result = new ArrayList<>();

        List<Civilite> civilites = Arrays.asList(Civilite.values());

        for(Civilite civilite : civilites){
            result.add(new CiviliteDto(civilite.name(), civilite.toValue()));
        }

        return result;
    }

    private static class CiviliteDto {
        private String name;
        private String value;

        public CiviliteDto(String name, String value) {
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
