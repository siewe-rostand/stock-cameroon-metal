package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.User;
import org.joda.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {


    User findByUserId(Long id);
    User findByPlayerId(String playerId);
//    @Query("select u from User u where (u.username = ?1 or u.email = ?1)")
//    User findByLoginOrEmail(String loginOrEmail);

    User findByTelephone(String phone);

    User findByEmail(String email);


    @Query("select (count(u) > 0) from User u where u.email = ?1")
    Boolean existsByEmail(String email);

    Boolean existsByTelephone(String phone);

    @Override
    void delete(User user);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = ?1")
    List<User> findAllByRole(String roleName);

    @Query("SELECT u FROM User u WHERE u.lastname like ?1 or u.firstname like ?1")
    List<User> findByMc(String mc);

    Optional<User> findOneByActivationKey(String activationKey);

//    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Timestamp date);

    Optional<User> findOneByResetKey(String resetKey);

//    Optional<User> findByTelephone(String phone);
    Optional<User> findOneByTelephone(String phone);

    Optional<User> findOneByUserId(Long userId);

    User deleteUserByUserId(Long id);
    void deleteByUserId(Long id);

    @Query("select distinct u from User u JOIN u.roles r "
            + "where (u.lastname like ?1 or u.firstname like ?1 or ?1 is null) "
            + "and r.name in ?2 ")
    Page<User> findAll(String login, String[] roles, Pageable pageable);

    @Query("select u from User u "
            + "where u.lastname like ?1 or u.firstname like ?1 or ?1 is null")
    Page<User> findAll(String name, Pageable pageable);

    Page<User> findAll(Pageable pageable);

    User findByLastname(String lastname);

    @Query("SELECT u FROM User u JOIN u.roles r "
            + "WHERE r.name = ?1 and (u.deleted is null or u.deleted = false)")
    Page<User> findByRole(String role, Pageable pageable);
}
