package com.findmydoc.Model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustodianRegistrationRequest {
    private String custodianName;
    private Long phoneNumber;
    private int registrationOtp;


}
