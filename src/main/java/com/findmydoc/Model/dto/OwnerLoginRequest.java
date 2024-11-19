package com.findmydoc.Model.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class OwnerLoginRequest {
    private Long phoneNumber;
}
