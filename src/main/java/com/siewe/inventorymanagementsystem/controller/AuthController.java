package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.controller.vm.LoginVM;
import com.siewe.inventorymanagementsystem.dto.UserDto;
import com.siewe.inventorymanagementsystem.security.JwtResponse;
import com.siewe.inventorymanagementsystem.security.SecurityUtils;
import com.siewe.inventorymanagementsystem.security.jwt.TokenProvider;
import com.siewe.inventorymanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private  final TokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    private final AuthenticationManager authenticationManager;

    public AuthController(TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> authorize(@Valid @RequestBody LoginVM loginUser, HttpServletResponse response){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser.getEmail(),loginUser.getPassword());

        try {
            this.authenticationManager.authenticate(authenticationToken);
//            return new ResponseEntity<>(this.tokenProvider.createToken(loginUser.getUsername()), HttpStatus.OK);
            return new ResponseEntity<>(new JwtResponse(this.tokenProvider.createToken(loginUser.getEmail()),loginUser.getEmail()), HttpStatus.OK);
//            return new ResponseEntity.ok(new JwtResponse(this.tokenProvider.createToken(loginUser.getUsername()),loginUser.getUsername()));
        }catch (BadCredentialsException bce){
            return new ResponseEntity<>("Bad Credential {password or email is wrong} \n"+bce ,HttpStatus.BAD_REQUEST);
        }catch (DisabledException e){
            return new ResponseEntity<>("User is disabled", HttpStatus.BAD_REQUEST);
        }catch (AccountExpiredException e){
            return new ResponseEntity<>("User account has expired",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(e,HttpStatus.EXPECTATION_FAILED);
        }
    }


    @PostMapping("/register-app")
    public String register(@RequestBody UserDto userDto){
        if (userService.emailExist(userDto.getEmail())){
            return "User already exist in the system";
        }

        userService.save(userDto);
        return this.tokenProvider.createToken(userDto.getEmail());
    }

    @GetMapping("/refresh-token")
    public String refreshToken(){
        return this.tokenProvider.createToken(SecurityUtils.getCurrentUserLogin());
    }

}
