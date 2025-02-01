package com.findmydoc.Service;


import com.findmydoc.Model.CustodianDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CustodianService {
    CustodianDetails addNewCustodian(CustodianDetails custodianDetails);

    String loginCustodian(Long phoneNumber);

    boolean validateOtp(Long phoneNumber, int enteredOtp);

    Optional<CustodianDetails> getCustodianDetails(Integer custodianId);

}
