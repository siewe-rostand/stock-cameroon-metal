package com.siewe.inventorymanagementsystem;

import com.siewe.inventorymanagementsystem.model.Category;
import com.siewe.inventorymanagementsystem.model.Role;
import com.siewe.inventorymanagementsystem.repository.CategoryRepository;
import com.siewe.inventorymanagementsystem.repository.RoleRepository;
import com.siewe.inventorymanagementsystem.service.ApprovisionnementService;
import com.siewe.inventorymanagementsystem.service.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * This dataset prepares initial data and persist them into database before application is launched
 */

@Component
public class InitialDataSet {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ApprovisionnementService approvisionnementService;

//    @Autowired
//    private VenteService venteService;

    public InitializingBean load() {
        createRoles();
        createDefaultUsers();
        createManagers();
        //createCategories();
        return null;
    }

    private void createRoles() {
        //List<String> roles = Arrays.asList("ADMIN", "USER", "USER");
        List<String> roles = Arrays.asList("ADMIN", "USER", "CUSTOMER","MANAGER");
        for(String role : roles){
            if(roleRepository.findByName(role) == null){
                roleRepository.save(new Role(role));
            }
        }
    }

    private void createCategories() {
        List<String> categories = Arrays.asList("COMPRIMES", "MAT ET ACCESSOIRES", "ORL", "INJECTIONS",
                "PARAMEDICAL", "USAGE EXT", "SACH-GLE", "SIROP", "GOUTTES BUVABLES", "FARINE-LAIT",
                "AMPOULES BUVABLE", "CREMES", "COLLYRE", "FRIGO", "OVULE", "PRESERVATIF", "VITAMINES",
                "SOLUTE", "SUPPOSITOIRES", "ARKO");
        for(String category : categories){
            if(categoryRepository.findByName(category) == null){
                categoryRepository.save(new Category(category));
            }
        }
    }

    private void createDefaultUsers() {
        createAdministrators();
        //  createManagers();
    }

    private void createAdministrators() {
        if(userService.findByLogin("admin") == null)
            userService.addAdmin();
    }


    private void createManagers() {
        //List<String> managers = Arrays.asList("yaya", "jojo", "baba", "toto");
        List<String> managers = Arrays.asList("rostand");
        for(String manager : managers){
            if(userService.findByLogin(manager) == null){
                userService.addNewUser(manager, "MANAGER");
            }
        }
    }
}
