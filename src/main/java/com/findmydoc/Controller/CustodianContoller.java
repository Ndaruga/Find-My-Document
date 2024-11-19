package com.findmydoc.Controller;

import com.findmydoc.Model.CustodianDetails;
import com.findmydoc.Model.dto.CustodianLoginRequest;
import com.findmydoc.Model.dto.VerifyOtpRequest;
import com.findmydoc.Service.CustodianService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping({"api/v1/custodian"})
public class CustodianContoller {
    private static final Logger logger = LoggerFactory.getLogger(CustodianContoller.class);

    @Autowired
    private CustodianService custodianService;

    public CustodianContoller() {}

    @PostMapping({"/register"})
    public ResponseEntity<Map<String, String>> addNewCustodian(@RequestBody CustodianDetails custodianDetails) {
        custodianService.addNewCustodian(custodianDetails);
        logger.info(custodianDetails.toString());
        return new ResponseEntity<>(Map.of("message", "Custodian added Successfully"), HttpStatus.CREATED);
    }

    @PostMapping({"/login"})
    public ResponseEntity<Map<String, String>> loginCustodian(@RequestBody CustodianLoginRequest loginRequest) {
        Long phoneNumber = loginRequest.getPhoneNumber();
        String response = custodianService.loginCustodian(phoneNumber);
        logger.info(custodianService.toString());
        return new ResponseEntity<>(Map.of("message", response), HttpStatus.OK);
    }

    @PostMapping({"verify-otp"})
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody @Valid VerifyOtpRequest verifyOtpRequest){
        try{
            boolean isVerified = custodianService.validateOtp(verifyOtpRequest.getPhoneNumber(), verifyOtpRequest.getOtp());
            if(isVerified){
                return new ResponseEntity<>(Map.of("message", "OTP verified successfully"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Map.of("message", "Failed to verify OTP"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


}
