package com.findmydoc.Repository;

import com.findmydoc.Model.CustodianDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustodianRepository extends JpaRepository<CustodianDetails, Integer> {
    boolean findByPhoneNumber(long phoneNumber);

}
