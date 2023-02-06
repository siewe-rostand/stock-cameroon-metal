package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.UserLogin;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@SuppressWarnings("unused")
public interface UserLoginRepository extends JpaRepository<UserLogin,Long> {
        default UserLogin findOne(Long id) {
        return (UserLogin) findById(id).orElse(null);
    }

    @Query("select userLogin from UserLogin userLogin where userLogin.user.username = ?1")
    List<UserLogin> findByUserIsCurrentUser(String currentUserLogin);

    @Query("select userLogin from UserLogin userLogin "
            + "where (userLogin.user.username like ?1 or ?1 = '%%') "
            + "AND userLogin.dateTime between ?2 and ?3 ")
    Page<UserLogin> findAll(String login, LocalDateTime cdf, LocalDateTime cdt, Pageable pageable);
}
