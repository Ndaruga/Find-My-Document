package com.findmydoc.Controller;

import com.findmydoc.Model.CustodianDetails;
import com.findmydoc.Model.OwnerDetails;
import com.findmydoc.Model.dto.CustodianLoginRequest;
import com.findmydoc.Model.dto.OwnerLoginRequest;
import com.findmydoc.Model.dto.VerifyOtpRequest;
import com.findmydoc.Service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.findmydoc.Controller.DocumentsController.logger;

@RestController
@RequestMapping({"api/v1/owner"})
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    public OwnerController() {}

    @PostMapping({"/register"})
    public ResponseEntity<Map<String, String>> addNewCustodian(@RequestBody OwnerDetails ownerDetails) {
        ownerService.registerOwner(ownerDetails);
        logger.info(ownerDetails.toString());
        return new ResponseEntity<>(Map.of("message", "Owner registered Successfully"), HttpStatus.CREATED);
    }

    @PostMapping({"/login"})
    public ResponseEntity<Map<String, String>> loginCustodian(@RequestBody OwnerLoginRequest loginRequest) {
        String response = ownerService.loginOwner(loginRequest);
        logger.info(ownerService.toString());
        return new ResponseEntity<>(Map.of("message", response), HttpStatus.OK);
    }

    @PostMapping({"verify-otp"})
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody @Valid VerifyOtpRequest verifyOtpRequest){
        try{
            boolean isVerified = ownerService.validateOtp(verifyOtpRequest.getPhoneNumber(), verifyOtpRequest.getOtp());
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
