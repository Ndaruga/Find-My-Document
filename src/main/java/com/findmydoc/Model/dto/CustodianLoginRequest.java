package com.findmydoc.Model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustodianLoginRequest {
    private Long phoneNumber;
    private int oneTimePassword;
    private boolean isVerified;
}
