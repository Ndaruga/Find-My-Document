package com.findmydoc.Service.Impl;

import com.findmydoc.Model.CustodianDetails;
import com.findmydoc.Model.dto.CustodianRegistrationRequest;
import com.findmydoc.Repository.CustodianRepository;
import com.findmydoc.Service.CustodianService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

import static com.findmydoc.Service.Impl.GenerateOTP.generateOTP;

@Service
public class CustodianServiceImpl implements CustodianService {
    private static final Logger logger = LoggerFactory.getLogger(CustodianServiceImpl.class);

    @Autowired
    private CustodianRepository custodianRepository;

    public CustodianServiceImpl() {}

    @Override
    public CustodianDetails addNewCustodian(@Valid CustodianDetails custodianDetails) {
        String custodianName = custodianDetails.getFullName().strip();
        Long phoneNumber = custodianDetails.getPhoneNumber();

        if (!custodianName.matches("[A-Za-z'\\s]{2,50}")){
            throw new InvalidParameterException("Invalid name provided");
        } else if (!String.valueOf(phoneNumber).matches("[0-9]{12,15}")) {
            throw new InvalidParameterException("Invalid Phone Number. Only 12 to 15 numbers are allowed.");
        }else if (custodianRepository.findByPhoneNumber(phoneNumber)){
            throw new InvalidParameterException("Phone Number already exists");
        }else {
//            Send Verification OTP
            int registrationOtp = generateOTP(5);
            logger.info("Generated registration OTP: " + registrationOtp);
            
            custodianDetails.setOneTimePassword(registrationOtp);
            custodianDetails.setVerified(false);
            custodianDetails.setLoggedIn(false);
            return custodianRepository.save(custodianDetails);
        }
    }

//    Send a Registration request to the server
    public CustodianRegistrationRequest registrationRequest(CustodianDetails custodianDetails) {
        CustodianRegistrationRequest custodianRegistrationRequest = new CustodianRegistrationRequest();

    }
    

//    @Override
//    public CustodianLoginRequest loginFounder(CustodianLoginRequest founderLoginRequest) {
//        Long phoneNumber = founderLoginRequest.getPhoneNumber();
//        int oneTimePassword = founderLoginRequest.getOneTimePassword();
//
//        if (!String.valueOf(phoneNumber).matches("[0-9]{12,15}")) {
//            throw new InvalidParameterException("Invalid Phone Number. Only 12 to 15 numbers are allowed.");
//        } else if (!String.valueOf(oneTimePassword).matches("[0-9]{5}")) {
//            throw new InvalidParameterException("Invalid One Time Password provided");
//        } else if (!founderRepository.findByPhoneNumber(phoneNumber)) {
//            throw new InvalidParameterException("Provided Phone Number does not exist");
//        } else if (!founderRepository.findByPhoneNumberAndOtp(phoneNumber, oneTimePassword)) {
//            throw new InvalidParameterException("Provided OTP or Phone Number does not exist");
//        } else {
//            founderLoginRequest.setVerified(true);
//            return founderLoginRequest;
//        }
//    }

}
