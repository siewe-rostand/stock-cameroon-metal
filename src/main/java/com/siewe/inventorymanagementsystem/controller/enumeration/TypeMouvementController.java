package com.siewe.inventorymanagementsystem.controller.enumeration;


import com.siewe.inventorymanagementsystem.model.enumeration.TypeMouvement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TypeMouvementController {

    @RequestMapping(value = "/typeMouvements", method = RequestMethod.GET)
    public List<TypeMouvementDto> getTypeMouvements( ) {
        List<TypeMouvementDto> result = new ArrayList<>();

        List<TypeMouvement> typeMouvements = Arrays.asList(TypeMouvement.values());

        for(TypeMouvement typeMouvement : typeMouvements){
            result.add(new TypeMouvementDto(typeMouvement.name(), typeMouvement.toValue()));
        }

        return result;
    }

    private class TypeMouvementDto {
        private String name;
        private String value;

        public TypeMouvementDto(String name, String value) {
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
