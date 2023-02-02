package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.controller.enumeration.view_model.KeyAndPasswordVM;
import com.siewe.inventorymanagementsystem.model.Role;
import com.siewe.inventorymanagementsystem.repository.RoleRepository;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.service.MailService;
import com.siewe.inventorymanagementsystem.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class AccountController {

    public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int PASSWORD_MAX_LENGTH = 100;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    /**
     * GET  /account : get the current user.
     *
     * @return the ResponseEntity with status 200 (OK) and the current user in body, or status 500 (Internal Server Error) if the user couldn't be returned
     */
   /* @RequestMapping(value="/account")
    public ResponseEntity<UserDto> findByUserIsCurrentUser(){
        UserDto result =  userService.findByUserIsCurrentUser();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }*/

    /**
     * GET  /activate : activate the registered user.
     *
     * @param key the activation key
     * @return the ResponseEntity with status 200 (OK) and the activated user in body, or status 500 (Internal Server Error) if the user couldn't be activated
     */
    @RequestMapping(value = "/activate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> activateAccount(@RequestParam(value = "key") String key) {
        return userService.activateRegistration(key)
                .map(user -> new ResponseEntity<String>(HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }
    /**
     * POST  /account/change_password : changes the current user's password
     *
     * @param password the new password
     * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request) if the new password is not strong enough
     */
    @PostMapping(path = "/account/change-password")
    public ResponseEntity<?> changePassword(@RequestBody String password) {
        if (!checkPasswordLength(password)) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
//        userService.changePassword(password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST   /account/reset_password/init : Send an e-mail to reset the password of the user
     *
     * @param mail the mail of the user
     * @param request the HTTP request
     * @return the ResponseEntity with status 200 (OK) if the e-mail was sent, or status 400 (Bad Request) if the e-mail address is not registered
     */
    @RequestMapping(value = "/account/reset_password/init",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> requestPasswordReset(@RequestBody String mail, HttpServletRequest request) {
        return userService.requestPasswordReset(mail)
                .map(user -> {
                    String baseUrl = request.getScheme() +
                            "://" +
                            request.getServerName() +
                            ":" +
                            request.getServerPort() +
                            request.getContextPath();
                    //mailService.sendPasswordResetMail(new UserDto().createDTO(user), baseUrl);
                    return new ResponseEntity<>("e-mail was sent", HttpStatus.OK);
                }).orElse(new ResponseEntity<>("e-mail address not registered", HttpStatus.BAD_REQUEST));
    }


    /**
     * POST   /account/reset_password/finish : Finish to reset the password of the user
     *
     * @param keyAndPassword the generated key and the new password
     * @return the ResponseEntity with status 200 (OK) if the password has been reset,
     * or status 400 (Bad Request) or 500 (Internal Server Error) if the password could not be reset
     */

    @RequestMapping(value = "/account/reset_password/finish",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        return userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey())
                .map(user -> new ResponseEntity<String>(HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private boolean checkPasswordLength(String password) {
        return (!StringUtils.isEmpty(password) &&
                password.length() >= PASSWORD_MIN_LENGTH &&
                password.length() <= PASSWORD_MAX_LENGTH);
    }


    @GetMapping("/roles")
//    @Secured(value = {"ROLE_ADMIN"})
    public List<String> getRoles() {
        List<Role> roles =  roleRepository.findAll();
        List<String> roleNames = new ArrayList<>();
        for (Role role: roles){
            //if(!role.getName().equals("ADMIN"))
            roleNames.add(role.getName());
        }
        return roleNames;
    }
}
