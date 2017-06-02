package com.askdog.dao.repository;

import com.askdog.model.entity.ExternalUser;
import com.askdog.model.entity.inner.user.UserProvider;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExternalUserRepository extends CrudRepository<ExternalUser, Long> {
    Optional<ExternalUser> findByExternalUserIdAndProvider(String externalUserId, UserProvider provider);

    Optional<ExternalUser> findFirstByUser_IdOrderByRegistrationTimeAsc(Long userId);

    Optional<ExternalUser> findByExternalUserId(String userId);

    Optional<ExternalUser> findByUser_IdAndProvider(Long userId, UserProvider provider);
}