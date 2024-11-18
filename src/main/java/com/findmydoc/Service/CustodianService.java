package com.findmydoc.Service;


import com.findmydoc.Model.CustodianDetails;
import org.springframework.stereotype.Service;

@Service
public interface CustodianService {
    CustodianDetails addNewCustodian(CustodianDetails custodianDetails);

//    CustodianLoginRequest loginFounder(CustodianLoginRequest founderLoginRequest);
}
