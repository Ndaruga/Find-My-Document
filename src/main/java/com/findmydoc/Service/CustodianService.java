package com.findmydoc.Service;


import com.findmydoc.Model.CustodianDetails;
import org.springframework.stereotype.Service;

@Service
public interface CustodianService {
    CustodianDetails addNewCustodian(CustodianDetails custodianDetails);

    String loginCustodian(Long phoneNumber);

    boolean validateOtp(Long phoneNumber, int enteredOtp);

}
