package com.findmydoc.Repository;

import com.findmydoc.Model.FounderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FounderRepository extends JpaRepository<FounderDetails, Integer> {
    FounderDetails findByPhoneNumber(long phoneNumber);
}
