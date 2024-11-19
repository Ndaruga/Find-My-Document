package com.findmydoc.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Table(name = "Custodian")
public class CustodianDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="custodian_id", nullable=false, updatable = false)
    private int Id;

    @Size(min=5, max=100, message="Name must be more than  characters")
    @Column(name="custodian_name", nullable = false)
    private String fullName;

    @Size(min=12, max=15, message = "Phone Number must be between 12 - 15 digits")
    @Column(name="phone_number", nullable = false, unique = true)
    private Long phoneNumber;

//    @Column(name="alternate_phone_number", unique = true)
//    private Long alternatePhoneNumber;

    @Column(name="is_verified", nullable = false)
    private boolean isVerified;

    @Column(name="login_time")
    private Timestamp loginDateTime;

    @Column(name="one_time_password")
    private int oneTimePassword;

    @Column(name="otp_expiration_time")
    private LocalDateTime otpExpirationTime;

    @Column(name="is_logged_in", nullable = false)
    private boolean isLoggedIn;

}
