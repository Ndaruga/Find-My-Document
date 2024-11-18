package com.findmydoc.Controller;

import com.findmydoc.Model.CustodianDetails;
import com.findmydoc.Service.CustodianService;
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

    @PostMapping({"/new"})
    public ResponseEntity<Map<String, String>> addNewCustodian(@RequestBody CustodianDetails custodianDetails) {
        custodianService.addNewCustodian(custodianDetails);
        logger.info(custodianDetails.toString());
        return new ResponseEntity<>(Map.of("message", "Custodian added Successfully"), HttpStatus.CREATED);
    }

//    @PostMapping({"login"})
//    public ResponseEntity<Map<String, String>> loginFounder(@RequestBody CustodianLoginRequest founderLoginRequest) {
//        founderService.loginFounder(founderLoginRequest);
//        logger.info(founderLoginRequest.toString());
//        return new ResponseEntity<>(Map.of("message", "Founder login Successfully"), HttpStatus.OK);
//    }
}
