package com.findmydoc.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Table(name = "Owner")
public class OwnerDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="owner_id", nullable=false, updatable = false)
    private int Id;

    @Size(min=5, max=100, message="Name must be more than  characters")
    @Column(name="owner_name", nullable = false)
    private String fullName;

    @Size(min=12, max=15, message = "Phone Number must be between 12 - 15 digits")
    @Column(name="phone_number", nullable = false, unique = true)
    private Long phoneNumber;

    @Column(name="is_verified", nullable = false)
    private boolean isVerified = false;

    @Column(name="login_time")
    private LocalDateTime loginDateTime;

    @Column(name="one_time_password", nullable = false)
    private int oneTimePassword=0;

    @Column(name="otp_expiration_time")
    private LocalDateTime otpExpirationTime;

    @Column(name="is_logged_in", nullable = false)
    private boolean isLoggedIn;

}