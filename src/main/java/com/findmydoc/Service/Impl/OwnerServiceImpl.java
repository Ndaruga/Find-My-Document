package com.findmydoc.Service.Impl;

import com.findmydoc.Model.OwnerDetails;
import com.findmydoc.Model.dto.DocumentSearchDTO;
import com.findmydoc.Model.dto.OwnerLoginRequest;
import com.findmydoc.Repository.DocumentsRepository;
import com.findmydoc.Repository.OwnerRepository;
import com.findmydoc.Service.OwnerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.findmydoc.Service.Impl.GenerateOTP.generateOTP;

@Service
public class OwnerServiceImpl implements OwnerService {
    private static final Logger logger = LoggerFactory.getLogger(OwnerServiceImpl.class);
    private final OwnerRepository ownerRepository;
    private final DocumentsRepository documentsRepository;

    public OwnerServiceImpl(OwnerRepository ownerRepository, DocumentsRepository documentsRepository) {
        this.ownerRepository = ownerRepository;
        this.documentsRepository = documentsRepository;
    }

    @Override
    public OwnerDetails registerOwner(@Valid OwnerDetails ownerDetails) {
        String ownerName = ownerDetails.getFullName().strip();
        Long phoneNumber = ownerDetails.getPhoneNumber();

        if (!ownerName.matches("[A-Za-z'\\s]{2,50}")){
            throw new InvalidParameterException("Invalid name provided");
        } else if (!String.valueOf(phoneNumber).matches("[0-9]{12,15}")) {
            throw new InvalidParameterException("Invalid Phone Number. Only 12 to 15 numbers are allowed.");
        }else if (ownerRepository.existsByPhoneNumber(phoneNumber)){
            throw new InvalidParameterException("Phone Number already exists");
        }else {
//            Send Verification OTP
            int registrationOtp = generateOTP(5);
            LocalDateTime otpExpirationTime = LocalDateTime.now().plusMinutes(5);

            String message = String.format("Hi %s.\nWelcome to Document Finder.\nYour OTP for registration is: %d. Valid for 5 minutes.", ownerDetails.getFullName().split(" ")[0], registrationOtp);
//            smsService.sendSMS(phoneNumber, message);
            logger.info(message);

            ownerDetails.setOneTimePassword(registrationOtp);
            ownerDetails.setOtpExpirationTime(otpExpirationTime);
            ownerDetails.setVerified(false);
            ownerDetails.setLoggedIn(false);
            return ownerRepository.save(ownerDetails);
        }
    }

    @Override
    public String loginOwner(OwnerLoginRequest loginRequest){
        LocalDateTime currentTime = LocalDateTime.now();

        Long phoneNumber = loginRequest.getPhoneNumber();
        OwnerDetails ownerDetails = ownerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new InvalidParameterException("Phone Number not Registered"));
        if (!String.valueOf(phoneNumber).matches("[0-9]{12,15}")) {
            throw new InvalidParameterException("Invalid Phone Number. Only 12 to 15 numbers are allowed.");
        } else if (!ownerDetails.isVerified()) {
            throw new InvalidParameterException("Phone Number not verified");
        } else if (currentTime.isBefore(ownerDetails.getLoginDateTime().plusHours(24)) && ownerDetails.isLoggedIn()){
            return "Session Active";
        } else {
            logger.info("Session Expired");

            //            Send Verification OTP
            int loginOtp = generateOTP(4);
            LocalDateTime otpExpirationTime = LocalDateTime.now().plusMinutes(5);

            String message = String.format("Hi %s.\nYour login OTP is: %d. Valid for 5 minutes.", ownerDetails.getFullName().split(" ")[0], loginOtp);
//            smsService.sendSMS(phoneNumber, message);
            logger.info(message);

            ownerDetails.setOneTimePassword(loginOtp);
            ownerDetails.setOtpExpirationTime(otpExpirationTime);
            ownerDetails.setLoggedIn(false);
            ownerRepository.save(ownerDetails);
            return "Your Session expired. Please Login with the OTP sent to your phone number.";

        }
    }

    @Override
    public boolean validateOtp(Long phoneNumber, int enteredOtp) {
        LocalDateTime currentTime = LocalDateTime.now();
        OwnerDetails ownerDetails = ownerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new InvalidParameterException("Phone Number not Registered"));

        if (ownerDetails.getOneTimePassword() != enteredOtp) {
            throw new InvalidParameterException("Invalid OTP");
        } else if (ownerDetails.getOtpExpirationTime() == null ||
                ownerDetails.getOtpExpirationTime().isBefore(currentTime)) {
            throw new InvalidParameterException("OTP has expired. Please request a new one");
        } else {
            ownerDetails.setVerified(true);
            ownerDetails.setLoggedIn(true);
            ownerDetails.setOtpExpirationTime(null);
            ownerDetails.setLoginDateTime(currentTime);
            ownerDetails.setOneTimePassword(0);
            ownerRepository.save(ownerDetails);
            return true;
        }
    }

    @Override
    public boolean documentExists(DocumentSearchDTO documentSearch) throws Exception {
        String documentType = documentSearch.getDocType().strip();
        String documentNumber = documentSearch.getDocNo().toUpperCase().strip();
        String docSerialNumber = documentSearch.getDocSerialNo().toUpperCase().strip();

        if (!documentType.matches("[A-Za-z\\s]{5,50}")){
            throw new InvalidParameterException("Invalid document type provided");
        } else if (documentsRepository.existsByDocumentNumberAndDocumentType(documentNumber, documentType)) {
            return true;
        } else if (documentsRepository.existsBySerialNumberAndDocumentType(docSerialNumber, documentType)) {
            return true;
        } else {
            return false;
        }
    }
}
