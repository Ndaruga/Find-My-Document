package com.findmydoc.Controller;

import com.findmydoc.Model.FounderDetails;
import com.findmydoc.Service.FounderService;
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
@RequestMapping({"api/v1/founder"})
public class FounderController {
    private static final Logger logger = LoggerFactory.getLogger(FounderController.class);

    @Autowired
    private FounderService founderService;

    public FounderController() {}

    @PostMapping({"/new"})
    public ResponseEntity<Map<String, String>> addNewFounder(@RequestBody FounderDetails founderDetails) {
        founderService.addNewFounder(founderDetails);
        logger.info(founderDetails.toString());
        return new ResponseEntity<>(Map.of("message", "Founder added Successfully"), HttpStatus.CREATED);
    }


}
