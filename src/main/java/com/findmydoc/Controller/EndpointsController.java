package com.findmydoc.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EndpointsController {
    public static final Logger logger = LoggerFactory.getLogger(EndpointsController.class);

    @GetMapping("/error")
    public String error() {
        return "error.html";
    }

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/about")
    public String about() {
        return "about.html";
    }

    @GetMapping("/feedback")
    public String feedback() {
        return "feedback.html";
    }

    @GetMapping("/testimonials")
    public String testimonials() {
        return "testimonial.html";
    }

    @GetMapping("/claim-document")
    public String claimDocument() {
        return "claim-document.html";
    }

    @GetMapping("/details-display")
    public String detailsDisplay() { return "details-display.html"; }

    @GetMapping("/custodian-details")
    public String custodianDetails() { return "custodian-details.html"; }

    @GetMapping("/privacy-policy")
    public String privacyPolicy() { return "privacy-policy.html"; }

    @GetMapping("/Ts-Cs")
    public String TsCs() { return "TsandCs.html"; }

    @GetMapping("/FAQ")
    public String FAQ() { return "faq.html"; }
}
