package com.findmydoc.Service;

import com.findmydoc.Model.OwnerDetails;
import com.findmydoc.Model.dto.DocumentSearchDTO;
import com.findmydoc.Model.dto.OwnerLoginRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface OwnerService {

    OwnerDetails registerOwner(@Valid OwnerDetails ownerDetails);

    String loginOwner(OwnerLoginRequest loginRequest);

    boolean validateOtp(Long phoneNumber, int enteredOtp);

    boolean documentExists(DocumentSearchDTO documentSearch) throws Exception;
}
