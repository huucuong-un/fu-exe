package com.exe01.backend.repository;

import com.exe01.backend.entity.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findById(UUID id);

    List<Account> findAllByOrderByCreatedDate(Pageable pageable);

    List<Account> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

}
