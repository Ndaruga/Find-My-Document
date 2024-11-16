package com.findmydoc.Service.Impl;

import com.findmydoc.Model.FounderDetails;
import com.findmydoc.Repository.FounderRepository;
import com.findmydoc.Service.FounderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

@Service
public class FounderServiceImpl implements FounderService {

    @Autowired
    private FounderRepository founderRepository;

    public FounderServiceImpl() {}

    @Override
    public FounderDetails addNewFounder(@Valid FounderDetails founderDetails) {
        String founderName = founderDetails.getFullName().strip();
        String email = founderDetails.getEmail().toLowerCase().strip();
        Long phoneNumber = founderDetails.getPhoneNumber();

        if (!founderName.matches("[A-Za-z'\\s]{2,50}")){
            throw new InvalidParameterException("Invalid name provided");
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new InvalidParameterException("Invalid email address provided");
        } else if (!String.valueOf(phoneNumber).matches("[0-9]{12,15}")) {
            throw new InvalidParameterException("Invalid Phone Number. Only 12 to 15 numbers are allowed.");
        } else {
            founderDetails.setVerified(false);
            return founderRepository.save(founderDetails);
        }
    }
}
