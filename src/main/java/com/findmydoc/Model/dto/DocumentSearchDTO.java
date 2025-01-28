package com.findmydoc.Model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class DocumentSearchDTO {
    private String docType;
    private String docNo;
//    private String docSerialNo;
}
