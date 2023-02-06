package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.dto.UserLoginDto;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.service.UserLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URISyntaxException;
import java.util.List;

public class UserLoginController {
    private final Logger log = LoggerFactory.getLogger(UserLoginController.class);

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private UserRepository userRepository;

    /**
     * POST  /userLogins : Create a new userLogin.
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new userLogin, or with status 400 (Bad Request) if the userLogin has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-login")
    public ResponseEntity<?> createUserLogin() throws URISyntaxException {
        log.debug("REST request to save UserLogin : {}");

        UserLoginDto userLoginDto = new UserLoginDto();
        //automatically set user to current user
//        userLoginDto.setUserId(userRepository.findByUsername(SecurityUtils.getCurrentUserLogin()).getId());
        UserLoginDto result = userLoginService.save(userLoginDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * GET  user-logins : get all the userLogins.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userLogins in body
     */
    @GetMapping("user-login-all")
    public ResponseEntity<List<UserLoginDto>> getAllUserLogins() {
        log.debug("REST request to get all UserLogins");
        return userLoginService.findAll();
    }

    @GetMapping("/user-login")
    public Page<UserLoginDto> getAllUserLogins(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                               @RequestParam(name = "size", defaultValue = "10") Integer size,
                                               @RequestParam(name = "sortBy", defaultValue = "dateTime") String sortBy,
                                               @RequestParam(name = "direction", defaultValue = "desc") String direction,
                                               @RequestParam(name = "login", defaultValue = "") String login,
                                               @RequestParam(name = "dateFrom") String dateFrom,
                                               @RequestParam(name = "dateTo") String dateTo){
        log.debug("REST request to get UserLogins");
        return userLoginService.findAll(page, size, sortBy, direction, login, dateFrom, dateTo);
    }

    /**
     * GET  user-logins/:id : get the "id" userLogin.
     *
     * @param id the id of the userLogin to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userLogin, or with status 404 (Not Found)
     */
    @GetMapping("user-login/{id}")
    public ResponseEntity<UserLoginDto> getUserLogin(@PathVariable Long id) {
        log.debug("REST request to get UserLogin : {}", id);
        return userLoginService.findOne(id);
    }
}

