package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.UserDto;
import com.siewe.inventorymanagementsystem.model.Role;
import com.siewe.inventorymanagementsystem.model.User;
import com.siewe.inventorymanagementsystem.model.error.EntityNotFoundException;
import com.siewe.inventorymanagementsystem.repository.RoleRepository;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.utils.RandomUtil;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;



    private  User local(UserDto userDto,String role){
        User user = new User();
        user.setUserId(userDto.getId());
        if(userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail().toLowerCase());
        }
        user.setLastname(userDto.getLastname());
        user.setFirstname(userDto.getFirstname());
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getFullname());
        user.setTelephone(userDto.getTelephone());
        user.setTelephoneAlt(userDto.getTelephone_alt());
        user.setCity(userDto.getCity());
        user.setQuarter(userDto.getQuarter());

        user.setLangKey(userDto.getLangKey());
        user.setActivated(true);
        user.setDeleted(false);

        //set created date;
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        java.time.LocalDateTime date = java.time.LocalDateTime.now();
        user.setCreatedDate(date.format(formatter));

        //before saving a user we encrypt password, and we can give roles
        user.setPassword(userDto.getPassword());
        //user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //user.setRoles(new HashSet<>(roleRepository.findAll()));

        user.setRoles(new HashSet<>());
        Role role1 = roleRepository.findByName(role);
        if(role != null){
            user.getRoles().add(role1);
        }

        return  userRepository.save(user);
    }

//    public void changePassword(String password) {
//        User user = userRepository.findByUsername(SecurityUtils.getCurrentUserLogin());
//        log.debug("Changed password for UserBase: {}", user);
//        if(user != null){
//            //user.setPassword(bCryptPasswordEncoder.encode(password));
//            user.setPassword(password);
//            userRepository.save(user);
//        }
//    }

//    @Transactional(readOnly = true)
//    public UserDto findByUserIsCurrentUser() {
//        User user = userRepository.findByUsername(SecurityUtils.getCurrentUserLogin());
//        return new UserDto().createDTO(user);
//    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime sdf = null;
        return userRepository.findOneByResetKey(key)
                .filter(user -> {
                    LocalDateTime oneDayAgo = new LocalDateTime().minusHours(24);
                    LocalDateTime ldt = null;
                    if(user.getResetDate() != null)
                        ldt = LocalDateTime.parse(user.getResetDate(), formatter);
                    return ldt.isAfter(oneDayAgo);
                })
                .map(user -> {
                    //user.setPassword(passwordEncoder.encode(newPassword));
                    user.setPassword(newPassword);
                    user.setResetKey(null);
                    user.setResetDate(null);
                    userRepository.save(user);
                    return user;
                });
    }

    public Optional<User> requestPasswordReset(String mail) {
        String pattern = "yyyy-MM-dd HH:mm";
        LocalDateTime datetime = new LocalDateTime();

        return userRepository.findOneByTelephone(mail)
                .filter(User::getActivated)
                .map(user -> {
                    user.setResetKey(RandomUtil.generateResetKey());
                    user.setResetDate(datetime.toString(pattern));
                    userRepository.save(user);
                    return user;
                });
    }
    public User findUserByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }
    /**
     * Save a user.
     *
     * @param userDto the entity to save
     * @return the persisted entity
     */
    public UserDto save(UserDto userDto, String role) {
        log.debug("Request to save User Dto class : {}", userDto);
        User user = new User();
        user.setUserId(userDto.getId());
        if(userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail().toLowerCase());
        }
        user.setLastname(userDto.getLastname());
        user.setFirstname(userDto.getFirstname());
        user.setUsername("userDto.getUsername()");
        user.setName(userDto.getFullname());
        user.setTelephone(userDto.getTelephone());
        user.setTelephoneAlt(userDto.getTelephone_alt());
        user.setCity(userDto.getCity());
        user.setQuarter(userDto.getQuarter());

        user.setLangKey(userDto.getLangKey());
        user.setActivated(true);
        user.setDeleted(false);

        //set created date;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        LocalDateTime date = new LocalDateTime();
        user.setCreatedDate(date.toString(pattern));

        //before saving a user we encrypt password, and we can give roles
        user.setPassword(userDto.getPassword());
        //user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //user.setRoles(new HashSet<>(roleRepository.findAll()));

        user.setRoles(new HashSet<>());
        Role role1 = roleRepository.findByName(role);
        if(role != null){
            user.getRoles().add(role1);
        }

        User result =   userRepository.save(user);
        return new UserDto().createDTO(result);
    }


    public UserDto save(UserDto userDto){
        log.debug("Request to save a new user {}",userDto);
        System.err.println(userDto);

        User user = new User();
        user.setUserId(userDto.getId());
        user.setUsername(userDto.getUsername());
        if (userDto.getUsername().isEmpty()){
            throw new EntityNotFoundException(UserDto.class,"username",userDto.getUsername());
        }
        if(userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail().toLowerCase());
        }
        user.setLastname(userDto.getLastname());
        user.setFirstname(userDto.getFirstname());
        user.setUsername(userDto.getFirstname() + "_" + userDto.getLastname());
        user.setName(userDto.getFullname());
        user.setTelephone(userDto.getTelephone());
        user.setTelephoneAlt(userDto.getTelephone_alt());
        user.setCity(userDto.getCity());
        user.setQuarter(userDto.getQuarter());

        user.setLangKey(userDto.getLangKey());
        user.setActivated(true);
        user.setDeleted(false);

        //set created date;
        String pattern = "yyyy-MM-dd";
        LocalDate date = new LocalDate();
        user.setCreatedDate(date.toString(pattern));

        HashSet<Role> roles = new HashSet<>();
        if (user.getRoles() != null){
            for (String role : userDto.getRoles())
                roles.add(roleRepository.findByName(role));
        }
        user.setRoles(roles);
        User result = userRepository.save(user);

        return new UserDto().createDTO(result);

    }

    /**
     * Save a customer.
     *
     * @param customerDto the entity to save
     * @return the persisted entity
     */
    public UserDto saveCustomer(UserDto customerDto,String role) {
        log.debug("Request to save Customer : {}", customerDto);

        User customer = new User();

        customer.setLastname(customerDto.getLastname());
        customer.setFirstname(customerDto.getFirstname());
        customer.setUsername(customerDto.getFirstname() + "_" + customerDto.getLastname());

        if (customerDto.getUsername().isEmpty()){
            throw new EntityNotFoundException(UserDto.class,"Username must not be null!",customerDto.getUsername());
        }
        customer.setUserId(customerDto.getId());
        customer.setName(customerDto.getFirstname() + "  " + customerDto.getLastname());
        customer.setTelephone(customerDto.getTelephone());
        customer.setTelephoneAlt(customerDto.getTelephone_alt());
        customer.setQuarter(customerDto.getQuarter());
        customer.setCity(customerDto.getCity());
        customer.setActivated(true);
        customer.setDeleted(false);

        String pattern = "yyyy-MM-dd HH:mm:ss";
        LocalDateTime datetime = new LocalDateTime(DateTimeZone.forOffsetHours(1));
        customer.setCreatedDate(datetime.toString(pattern));

        customer.setRoles(new HashSet<>());
        Role role1 = roleRepository.findByName(role);
        if(role != null){
            customer.getRoles().add(role1);
        }


        User result =  userRepository.save(customer);
        return new UserDto().createDTO(result);
    }

    public UserDto save(UserDto userDto, String storeName, String role) {
        log.debug("Request to save User with store : {}", userDto);

        User user = new User();
        user.setUserId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setLastname(userDto.getLastname());
        user.setFirstname(userDto.getFirstname());
        user.setTelephone(userDto.getTelephone());
        user.setTelephoneAlt(userDto.getTelephone_alt());

        user.setLangKey(userDto.getLangKey());
        user.setActivated(true);
        user.setDeleted(false);

        String pattern = "yyyy-MM-dd HH:mm:ss";
        LocalDateTime date = new LocalDateTime();
        user.setCreatedDate(date.toString(pattern));

        user.setPassword(userDto.getPassword());

        /*Role role1 = roleRepository.findByName(role);
        if(role1 != null){user.setRole(role1); }*/

        user.setRoles(new HashSet<>());
        Role role1 = roleRepository.findByName(role);
        if(role != null){
            user.getRoles().add(role1);
        }

        User result =  userRepository.save(user);
        return new UserDto().createDTO(result);
    }


    public UserDto update(UserDto userDto) {
        log.debug("Request to save User : {}", userDto);
        String fullname = userDto.getFirstname() + "  " + userDto.getLastname();

        User user = userRepository.findByUserId(userDto.getId());
        if (user==null){
            throw  new EntityNotFoundException(User.class,"user id",userDto.getId().toString());
        }

        user.setUserId(userDto.getId());
        //currentUser.setPassword(userDto.getPassword());
        //user.setUsername(userDto.getLogin());
        //user.setEmail(userDto.getEmail());
        user.setLangKey(userDto.getLangKey());
        user.setActivated(userDto.getActivated());

        user.setLastname(userDto.getLastname());
        user.setFirstname(userDto.getFirstname());
        user.setName(userDto.getFullname());
        user.setTelephoneAlt(userDto.getTelephone_alt());
        user.setTelephone(userDto.getTelephone());
        user.setValidated(userDto.getValidated());
        
        //set created date;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        LocalDateTime date = new LocalDateTime();
        user.setCreatedDate(date.toString(pattern));

        //this is to ensure that only one user will be linked to a player id
        if(userDto.getPlayerId() != null){
            User previouslyLinkedToDevice = userRepository.findByPlayerId(userDto.getPlayerId());
            if(previouslyLinkedToDevice != null)
                previouslyLinkedToDevice.setPlayerId(null);
        }
        user.setPlayerId(userDto.getPlayerId());

        HashSet<Role> roles = new HashSet<>();
        if (userDto.getRoles() != null) {
            for (String role : userDto.getRoles())
                roles.add(roleRepository.findByName(role));
        }
        user.setRoles(roles);

        User result = userRepository.save(user);
        return new UserDto().createDTO(result);
    }
/*
    @Transactional(readOnly = true)
    public UserDto findByUserIsCurrentUser() {
   User user = userRepository.findByLogin(SecurityUtils.getCurrentUserLogin());
        return new UserDto().createDTO(user);
    }


    public void changePassword(String password) {
        User user = userRepository.findByLogin(SecurityUtils.getCurrentUserLogin());
        log.debug("Changed password for UserBase: {}", user);
        if(user != null){
            //user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setPassword(password);
            userRepository.save(user);
        }
    }*/

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
                .map(user -> {
                    // activate given user for the registration key.
                    user.setActivated(true);
                    user.setActivationKey(null);
                    userRepository.save(user);
                    //userSearchRepository.save(user);
                    log.debug("Activated user: {}", user);
                    return user;
                });
    }

    /**
     *  Get all the users.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public ResponseEntity<List<UserDto>> findAll() {
        log.debug("Request to get all Users");
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        // if (users.isEmpty()){
        //     throw new EntityNotFoundException(UserDto.class,"Username must not be null!",customerDto.getUsername());
        // }
        // if (users.isEmpty()) {
        //     return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        //     // You many decide to return HttpStatus.NOT_FOUND
        // }
        for (User user : users)
            userDtos.add(new UserDto().createDTO(user));
        return new ResponseEntity<List<UserDto>>(userDtos, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAllByRole(String roleName){
        log.debug("Request to get all Users by Role");
        List<User> users = userRepository.findAllByRole(roleName);
        List<UserDto> userDtos = new ArrayList<>();

        for (User user : users)
            userDtos.add(new UserDto().createDTO(user));

        //return new ResponseEntity<List<UserDto>>(userDtos, HttpStatus.OK);
        return userDtos;
    }

    public Page<UserDto> findAll(Integer page, Integer size, String sortBy, String direction, String name) {
        log.debug("Request to get all Users");

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<User> users = userRepository.findAll("%"+name+"%", pageable);

        Page<UserDto> userDtos = users.map(user -> new UserDto().createDTO(user));
        return userDtos;
    }

    public Page<UserDto> findAll(Integer page, Integer size, String sortBy, String direction, String login, String[] roles) {
        log.debug("Request to get all Users");

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<User> users = userRepository.findAll("%"+login+"%", roles, pageable);

        return users.map(user -> new UserDto().createDTO(user));
    }

    public Page<UserDto> findSellers(Integer page, Integer size, String sortBy, String direction) {
        log.debug("Request to get all Users");

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);

        //Page<User> users = userRepository.findByRole("USER", pageable);
        Page<User> users = userRepository.findByRole("USER", pageable);

        return users.map(user -> new UserDto().createDTO(user));
    }

    public Page<UserDto> findAll(Integer page, Integer size, String sortBy, String direction) {
        log.debug("Request to get all Users");

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);

        //Page<User> users = userRepository.findByRole("USER", pageable);
        Page<User> users = userRepository.findAll( pageable);

        return users.map(user -> new UserDto().createDTO(user));
    }
       /*
    public List<UserDto> findSellersByStore(Long storeId) {
        log.debug("Request to get all Users");

        List<UserDto> userDtos = new ArrayList<>();
        List<User> users = userRepository.findByStoreIdAndDeletedFalse(storeId);

        for(User user: users){
            userDtos.add(new UserDto().createDTO(user));
        }
        return userDtos;
    }
    */

    public List<UserDto> findByMc(String mc) {
        List<User> users = userRepository.findByMc("%"+mc+"%");
        List<UserDto> userDtos = new ArrayList<>();

        for (User user : users)
            userDtos.add(new UserDto().createDTO(user));

        return userDtos;
    }


    /**
     *  Get one user by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ResponseEntity<UserDto> findOne(Long id) {
        log.debug("Request to get User : {}", id);
        User user = userRepository.findByUserId(id);

        UserDto userDto = new UserDto().createDTO(user);
        return Optional.ofNullable(userDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElseThrow(()->new EntityNotFoundException(UserDto.class,"User not found with id",String.valueOf(userDto.getId())));
    }


    /**
     *  Delete the  user by id.
     *
     *  @param id the id of the entity
     */
//    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    public void delete(Long id) {
        log.debug("Request to delete User : {}", id);
        User user = userRepository.findByUserId(id);
        if(user == null){
            throw new EntityNotFoundException(User.class,"id",id.toString());
        }
        if (!user.getVentes().isEmpty()) {
            user.setDeleted(true);
            userRepository.save(user);
        } else {
            userRepository.deleteByUserId(id);
        }
    }

    public User findByLogin(String login) {
        return userRepository.findByUsername(login);
    }

    public boolean loginExists(String login) {
        return (userRepository.findByUsername(login) != null);
    }

    public void addNewUser(String login, String role) {
        User user = new User();
        user.setUsername(login);
        user.setLastname(login);
        user.setTelephone("6" + ThreadLocalRandom.current().nextInt(11111111, 99999999));
        user.setPassword("1234");
        //user.setEmail(user.getLogin()+ "@pharma.com");
        user.setActivated(true);

        /*Role role1 = roleRepository.findByName(role);
        if(role1 != null){user.setRole(role1); }*/

        user.setRoles(new HashSet<>());
        Role role1 = roleRepository.findByName(role);
        if(role != null){
            user.getRoles().add(role1);
        }

        //set created date;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        LocalDateTime date = new LocalDateTime();
        user.setCreatedDate(date.toString(pattern));

        userRepository.save(user);
    }

    public void addAdmin() {
        User user = new User();
        user.setLastname("Admin");
        user.setUsername("admin");
        user.setTelephone("");
        user.setPassword("1234");
        //user.setEmail(user.getLogin()+ "@pharma.com");
        user.setActivated(true);

        user.setRoles(new HashSet<>());
        user.getRoles().addAll(roleRepository.findAll());

        //set created date;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        LocalDateTime date = new LocalDateTime();
        user.setCreatedDate(date.toString(pattern));

        userRepository.save(user);
    }

}
