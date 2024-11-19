package com.findmydoc.Repository;

import com.findmydoc.Model.OwnerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<OwnerDetails, Integer> {

    boolean existsByPhoneNumber(Long phoneNumber);

    List<OwnerDetails> findAllByOtpExpirationTimeBefore(LocalDateTime now);

    Optional<OwnerDetails> findByPhoneNumber(Long phoneNumber);
}
