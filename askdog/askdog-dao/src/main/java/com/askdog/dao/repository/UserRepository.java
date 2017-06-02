package com.askdog.dao.repository;

import com.askdog.dao.repository.extend.UserStatisticsRepository;
import com.askdog.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, UserStatisticsRepository, JpaSpecificationExecutor<User> {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByName(String name);

    Page<User> findByLastAccessTimeAfter(Date date, Pageable pageable);

    @Query(value = "SELECT count(*) FROM mc_user u LEFT JOIN mc_external_user eu ON u.id = eu.bind_user", nativeQuery = true)
    Long countRegisteredUser();

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.email = ?1")
    boolean existsByEmail(String mail);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.phoneNumber = ?1")
    boolean existsByPhone(String phone);

    @Query(value = "select * from mc_user u where u.name = ?1 or u.email = ?1 or u.phone_number = ?1", nativeQuery = true)
    List<User> search(String key);
}