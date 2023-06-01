package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.UserLoginDto;
import com.siewe.inventorymanagementsystem.model.User;
import com.siewe.inventorymanagementsystem.model.UserLogin;
import com.siewe.inventorymanagementsystem.repository.UserLoginRepository;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserLoginService {

    private final Logger log = LoggerFactory.getLogger(UserLoginService.class);

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private UserRepository userRepository;


    /**
     * Save a userLogin.
     *
     * @param userLoginDto the entity to save
     * @return the persisted entity
     */
    public UserLoginDto save(UserLoginDto userLoginDto) {

        UserLogin userLogin = new UserLogin();

        String pattern = "dd-MM-yyyy HH:mm";
        userLogin.setId(userLoginDto.getId());
        LocalDateTime datetime = new LocalDateTime();
        userLogin.setDateTime(datetime.toString(pattern));

        User user = userRepository.findByUserId(userLoginDto.getUserId());
        userLogin.setUser(user);

        UserLogin result = userLoginRepository.save(userLogin);
        return new UserLoginDto().createDTO(result);
    }



    /**
     *  Get all the userLogins.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public ResponseEntity<List<UserLoginDto>> findAll() {
        log.debug("Request to get all UserLogins");
        List<UserLogin> userLogins = userLoginRepository.findAll();

        List<UserLoginDto> userLoginDtos = new ArrayList<>();

        for (UserLogin userLogin : userLogins)
            userLoginDtos.add(new UserLoginDto().createDTO(userLogin));

        return new ResponseEntity<List<UserLoginDto>>(userLoginDtos, HttpStatus.OK);
    }

    public Page<UserLoginDto> findAll(Integer page, Integer size, String sortBy, String direction, String login, String dateFrom, String dateTo) {
        log.debug("Request to get all Plannings");

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

        LocalDateTime cdf = null;
        if(dateFrom!=null)
            cdf = LocalDateTime.parse(dateFrom, formatter);

        LocalDateTime cdt = null;
        if(dateTo!=null)
            cdt = LocalDateTime.parse(dateTo, formatter);

        Pageable pageable =  PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<UserLogin> userLogins = userLoginRepository.findAll( "%"+login+
                "%", cdf, cdt, pageable);

        Page<UserLoginDto> userLoginDtos = userLogins.map(userLogin -> new UserLoginDto().createDTO(userLogin));
        return userLoginDtos;
    }

    /**
     *  Get one userLogin by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ResponseEntity<UserLoginDto> findOne(Long id) {
        log.debug("Request to get UserLogin : {}", id);
        UserLogin userLogin = userLoginRepository.findOne(id);

        UserLoginDto userLoginDto = new UserLoginDto().createDTO(userLogin);
        return Optional.ofNullable(userLoginDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

