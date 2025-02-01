package com.findmydoc.Repository;

import com.findmydoc.Model.CustodianDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CustodianRepository extends JpaRepository<CustodianDetails, Integer> {
    boolean existsByPhoneNumber(long phoneNumber);

    List<CustodianDetails> findAllByOtpExpirationTimeBefore(LocalDateTime now);

    Optional<CustodianDetails> findByPhoneNumber(Long phoneNumber);

    Optional<CustodianDetails> findById(Integer custodianId);
}
