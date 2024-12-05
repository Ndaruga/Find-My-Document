package com.findmydoc.RepositoryTest;


import com.findmydoc.Model.CustodianDetails;
import com.findmydoc.Repository.CustodianRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CustodianRepositoryTest {

    @Autowired
    private CustodianRepository custodianRepository;

    CustodianDetails custodianDetails;

    @BeforeEach
    void setUp() {
        custodianDetails = new CustodianDetails(1, "Martin Luther", 26549983032L, false, null, 45623, LocalDateTime.now().plusMinutes(5), false);
        custodianRepository.save(custodianDetails);
    }

    @AfterEach
    void tearDown() {
        custodianDetails = null;
        custodianRepository.deleteAll();

    }


//    Test case SUCCESS
    @Test
    void testExistsByPhoneNumber_found(){
        boolean phoneNumberExists = custodianRepository.existsByPhoneNumber(26549983032L);
        assertThat(phoneNumberExists).isTrue();
    }

    @Test
    void testFindByPhoneNumber_found(){
        Optional<CustodianDetails> checkDetails = custodianRepository.findByPhoneNumber(26549983032L);
        assertThat(checkDetails).isPresent();
        assertThat(checkDetails.get().getPhoneNumber()).isEqualTo(26549983032L);
        assertThat(checkDetails.get().getFullName()).isEqualTo("Martin Luther");
        assertThat(checkDetails.get().getOtpExpirationTime()).isBefore(LocalDateTime.now().plusMinutes(5));
    }

//    Test case FAILURE

    @Test
    void testExistsByPhoneNumber_notFound(){
        boolean phoneNumberExists = custodianRepository.existsByPhoneNumber(26549943032L);
        assertThat(phoneNumberExists).isFalse();
    }

    @Test
    void testFindByPhoneNumber_notFound(){
        Optional<CustodianDetails> noDetails = custodianRepository.findByPhoneNumber(26549943032L);
        assertThat(noDetails).isNotPresent();
    }
}
