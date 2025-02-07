package com.findmydoc.Service.Impl;

import com.findmydoc.Model.CustodianDetails;
import com.findmydoc.Repository.CustodianRepository;
import com.findmydoc.Service.CustodianService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        }else if (custodianRepository.existsByPhoneNumber(phoneNumber)){
            throw new InvalidParameterException("Phone Number already exists");
        }else {
//            Send Verification OTP
            int registrationOtp = generateOTP(4);
            LocalDateTime otpExpirationTime = LocalDateTime.now().plusMinutes(5);

            String message = String.format("Hi %s.\nWe appreciate your help.\nYour OTP for registration is: %d. Valid for 5 minutes.", custodianDetails.getFullName().split(" ")[0], registrationOtp);
//            smsService.sendSMS(phoneNumber, message);
            logger.info(message);

            custodianDetails.setOneTimePassword(registrationOtp);
            custodianDetails.setOtpExpirationTime(otpExpirationTime);
            custodianDetails.setVerified(false);
            custodianDetails.setLoggedIn(false);
            return custodianRepository.save(custodianDetails);
        }
    }

    @Override
    public String loginCustodian(Long phoneNumber){
        LocalDateTime currentTime = LocalDateTime.now();
        CustodianDetails custodianDetails = custodianRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new InvalidParameterException("Phone Number not Registered"));
        if (!String.valueOf(phoneNumber).matches("[0-9]{12,15}")) {
            throw new InvalidParameterException("Invalid Phone Number. Only 12 to 15 numbers are allowed.");
        } else if (!custodianDetails.isVerified()) {
            throw new InvalidParameterException("Phone Number not verified");
        } else if (currentTime.isBefore(custodianDetails.getLoginDateTime().plusHours(24)) && custodianDetails.isLoggedIn()){
            return "Session Active";
        } else {
            logger.info("Session Expired");

            //            Send Verification OTP
            int loginOtp = generateOTP(4);
            LocalDateTime otpExpirationTime = LocalDateTime.now().plusMinutes(5);

            String message = String.format("Hi %s.\nYour login OTP is: %d. Valid for 5 minutes.", custodianDetails.getFullName().split(" ")[0], loginOtp);
//            smsService.sendSMS(phoneNumber, message);
            logger.info(message);

            custodianDetails.setOneTimePassword(loginOtp);
            custodianDetails.setOtpExpirationTime(otpExpirationTime);
            custodianDetails.setLoggedIn(false);
            custodianRepository.save(custodianDetails);
            return "Your Session expired. Please Login with the OTP sent to your phone number.";

        }
    }

    @Override
    public boolean validateOtp(Long phoneNumber, int enteredOtp) {
        LocalDateTime currentTime = LocalDateTime.now();
        CustodianDetails custodianDetails = custodianRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new InvalidParameterException("Phone Number not Registered"));

        if (custodianDetails.getOneTimePassword() != enteredOtp) {
            throw new InvalidParameterException("Invalid OTP");
        } else if (custodianDetails.getOtpExpirationTime() == null ||
                custodianDetails.getOtpExpirationTime().isBefore(currentTime)) {
            throw new InvalidParameterException("OTP has expired. Please request a new OTP");
        } else {
            custodianDetails.setVerified(true);
            custodianDetails.setLoggedIn(true);
            custodianDetails.setOtpExpirationTime(null);
            custodianDetails.setLoginDateTime(currentTime);
            custodianDetails.setOneTimePassword(0);
            custodianRepository.save(custodianDetails);
            return true;
        }
    }

    public Optional<CustodianDetails> getCustodianDetails(Integer custodianId){
        return custodianRepository.findById(custodianId);
    }

    @Transactional
    @Scheduled(fixedRate = 60000) // Runs every minute
    public void clearExpiredOtps() {
        logger.info("Scheduled task running to clear expired OTPs.");
        List<CustodianDetails> expiredCustodians = custodianRepository.findAllByOtpExpirationTimeBefore(LocalDateTime.now());
        for (CustodianDetails custodian : expiredCustodians) {
            custodian.setOneTimePassword(0); // Reset OTP or delete it
            custodian.setOtpExpirationTime(null);
            custodianRepository.save(custodian);
        }
    }

}
