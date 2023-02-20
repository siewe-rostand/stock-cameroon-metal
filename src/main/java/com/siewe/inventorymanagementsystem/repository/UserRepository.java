package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.User;
import org.joda.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {


    User findByUserId(Long id);
    User findByUsername(String username);
    User findByPlayerId(String playerId);

    @Override
    void delete(User user);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = ?1")
    List<User> findAllByRole(String roleName);

    @Query("SELECT u FROM User u WHERE u.lastname like ?1 or u.firstname like ?1")
    List<User> findByMc(String mc);

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(LocalDate date);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findByTelephone(String phone);
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

    User findByLastname(String lastname);

    @Query("SELECT u FROM User u JOIN u.roles r "
            + "WHERE r.name = ?1 and (u.deleted is null or u.deleted = false)")
    Page<User> findByRole(String role, Pageable pageable);
}
