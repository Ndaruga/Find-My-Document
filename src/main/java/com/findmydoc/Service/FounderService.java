package com.findmydoc.Service;


import com.findmydoc.Model.FounderDetails;
import org.springframework.stereotype.Service;

@Service
public interface FounderService {
    FounderDetails addNewFounder(FounderDetails founderDetails);
}
