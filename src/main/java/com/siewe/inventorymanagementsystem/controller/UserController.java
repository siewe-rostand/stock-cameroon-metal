package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.dto.CategoryDto;
import com.siewe.inventorymanagementsystem.dto.UserDto;
import com.siewe.inventorymanagementsystem.model.User;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.service.MailService;
import com.siewe.inventorymanagementsystem.service.UserService;
import com.siewe.inventorymanagementsystem.utils.CustomErrorType;
import com.siewe.inventorymanagementsystem.utils.InvalidActionException;
import com.siewe.inventorymanagementsystem.utils.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    /**
     * POST  /users : Create a new user.
     *
     * @param userDto the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the user has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDto);
        HashMap<String, String> error = new HashMap<>();
        if (userService.emailExist(userDto.getEmail())) {
            error.put("error", "Cet email est déjà utilisé par un autre d'utilisateur !");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        if (userRepository.findOneByTelephone(userDto.getTelephone()).isPresent()){
            error.put("error","ce numéro de téléphone a déjà été utilisé");
            return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
        }
        UserDto result = userService.save(userDto, "USER");
        return ResponseHandler.generateResponse("user save Successfully",HttpStatus.CREATED,result);
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getUserInfo() {
        UserDto userDto = userService.getUserInfo();
        return  ResponseHandler.generateResponse("user data successfully gotten",HttpStatus.OK,userDto);
    }
//
//    @PostMapping("/customers")
//    public ResponseEntity<Object> createCustomer(@Valid @RequestBody UserDto userDto, HttpServletRequest request) throws URISyntaxException {
//        log.debug("REST request to save User : {}", userDto);
//        HashMap<String, String> error = new HashMap<>();
//
//        if (userRepository.findByEmail(userDto.getEmail().toLowerCase()) != null) {
//            error.put("error", "Ce nom d'utilisateur est déjà utilisé !");
//            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//        }
//        UserDto result = userService.saveCustomer(userDto, "CUSTOMER");
//        return ResponseHandler.generateResponse("user created successfully",HttpStatus.CREATED,result);
//    }

    @PostMapping("/user-seller")
    public ResponseEntity<UserDto> createSeller(@Valid @RequestBody UserDto userDto, HttpServletRequest request) throws URISyntaxException, InvalidActionException {
        log.debug("REST request to save seller : {}", userDto);
        HashMap<String, String> error = new HashMap<>();

        if (userRepository.findByEmail(userDto.getEmail().toLowerCase()) != null) {
            error.put("error", "Ce nom d'utilisateur est déjà utilisé !");
            return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        }
        UserDto result = userService.save(userDto, "USER");
        return new ResponseEntity<UserDto>(result, HttpStatus.CREATED);
    }

    /**
     * PUT  /users : Updates an existing user.
     *
     * @param userDto the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the user is not valid,
     * or with status 500 (Internal Server Error) if the user couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/users")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto) throws URISyntaxException {
        log.debug("REST request to update User : {}", userDto);
        if (userDto.getId() == null) {
            return new ResponseEntity(new CustomErrorType("Unable to update. A user id can not be null."), HttpStatus.BAD_REQUEST);
        }
        UserDto result = userService.update(userDto);
        return new ResponseEntity<UserDto>(result, HttpStatus.OK);
    }

    @GetMapping("/all-users")
    public Page<UserDto> getAllUsers(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                     @RequestParam(name = "size", defaultValue = "5") Integer size,
                                     @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                     @RequestParam(name = "direction", defaultValue = "desc") String direction
    ) {
        log.debug("REST request to get Users");
        return userService.findAll(page, size, sortBy, direction);
    }


    /**
     * GET  /users : get all the users by role.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of users with a given role in body
     */
    @GetMapping("/users")
    public Page<UserDto> getAllUsers(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                     @RequestParam(name = "size", defaultValue = "5") Integer size,
                                     @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                     @RequestParam(name = "direction", defaultValue = "desc") String direction,
                                     @RequestParam(name = "login", defaultValue = "") String login,
                                     @RequestParam(name = "roles") String[] roles
    ) {
        log.debug("REST request to get Users");
        return userService.findAll(page, size, sortBy, direction, login, roles);
    }

    @GetMapping("/sellers")
    public Page<UserDto> getSellers(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "lastName") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        log.debug("REST request to get page of Sellers by store");
        return userService.findSellers(page, size, sortBy, direction);
    }

    /*
    @GetMapping("/api/sellers-by-store-all/{storeId}")
    public List<UserDto> getSellersByStore(@PathVariable Long storeId) {
        log.debug("REST request to get page of Sellers by store");
        return userService.findSellersByStore(storeId);
    }
    */

    @GetMapping("/users-search")
    public Map<String, List<UserDto>> getAllUsers(@RequestParam(name = "mc") String mc) {
        log.debug("REST request to get Users");
        Map<String, List<UserDto>> map = new HashMap<>();
        map.put("results", userService.findByMc(mc));
        return map;
    }

    /**
     * GET  /users/:id : get the "id" user.
     *
     * @param id the id of the user to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the user, or with status 404 (Not Found)
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        log.debug("REST request to get User : {}", id);
        return userService.findOne(id);
    }

    /**
     * DELETE  /users/:id : delete the "id" user.
     *
     * @param id the id of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        log.debug("REST request to delete User : {}", id);
        if (id == null) {
            return new ResponseEntity<>(new CustomErrorType("Unable to delete. A user id can not be null."), HttpStatus.BAD_REQUEST);
        }
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

