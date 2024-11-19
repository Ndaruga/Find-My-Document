package com.findmydoc.Model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VerifyOtpRequest {
    private int otp;
    private Long phoneNumber;
}
