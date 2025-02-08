
// Carousel Control
let headerCarousel;
let testimonialCarousel;

(function ($) {
    "use strict";

    // Spinner
    var spinner = function () {
        setTimeout(function () {
            if ($('#spinner').length > 0) {
                $('#spinner').removeClass('show');
            }
        }, 1);
    };
    spinner();
    
    // Initiate the wowjs
    new WOW().init();

    // Sticky Navbar
    $(window).scroll(function () {
        if ($(this).scrollTop() > 300) {
            $('.sticky-top').css('top', '0px');
        } else {
            $('.sticky-top').css('top', '-100px');
        }
    });
    
    // Dropdown on mouse hover
    const $dropdown = $(".dropdown");
    const $dropdownToggle = $(".dropdown-toggle");
    const $dropdownMenu = $(".dropdown-menu");
    const showClass = "show";
    
    $(window).on("load resize", function() {
        if (this.matchMedia("(min-width: 992px)").matches) {
            $dropdown.hover(
                function() {
                    const $this = $(this);
                    $this.addClass(showClass);
                    $this.find($dropdownToggle).attr("aria-expanded", "true");
                    $this.find($dropdownMenu).addClass(showClass);
                },
                function() {
                    const $this = $(this);
                    $this.removeClass(showClass);
                    $this.find($dropdownToggle).attr("aria-expanded", "false");
                    $this.find($dropdownMenu).removeClass(showClass);
                }
            );
        } else {
            $dropdown.off("mouseenter mouseleave");
        }
    });


    // Initialize carousels
    $(document).ready(function() {
        headerCarousel = $(".header-carousel");
        testimonialCarousel = $(".testimonial-carousel");

        headerCarousel.owlCarousel({
            autoplay: true,
            smartSpeed: 1500,
            items: 1,
            dots: false,
            loop: true,
            nav: true,
            navText: [
                '<i class="bi bi-chevron-left"></i>',
                '<i class="bi bi-chevron-right"></i>'
            ]
        });

        testimonialCarousel.owlCarousel({
            autoplay: true,
            smartSpeed: 1000,
            center: true,
            margin: 24,
            dots: true,
            loop: true,
            nav: false,
            responsive: {
                0: { items: 1 },
                768: { items: 2 },
                992: { items: 3 }
            }
        });
    });


    // Form submission handlers
    $(document).ready(function() {
        $('#registerDocForm').on('submit', function(e) {
            e.preventDefault();
            const documentType = $('#documentType').val().trim();
            const nationality = $('#nationality').val().trim();
            const firstName = $('#ownerFirstName').val().trim();
            const lastName = $('#ownerLastName').val().trim();
            const documentNumber = $('#documentNumber').val().trim();
            const serialNumber = $('#serialNumber').val().trim();
            const dateFound = $('#dateFound').val();
            const moreNotes = $('#moreNotes').val().trim()
            const custodianId = sessionStorage.getItem('returnedCustodianID');

            if (!custodianId > 1){
                alert("An error occured while registering your details.");
                return;
            }

            // Validation regex patterns
            const namePattern = /^[A-Za-z]+$/;
            const docSerialPattern = /^[A-Z0-9-]+$/;

            // Current date
            const today = new Date();
            const fifteenDaysAgo = new Date();
            fifteenDaysAgo.setDate(today.getDate() - 15);

            // Validate names
            if (!namePattern.test(firstName)) {
                alert("First name must contain alphabetic characters only.");
                return;
            }

            if (lastName && !namePattern.test(lastName)) {
                alert("Last name must contain alphabetic characters only.");
                return;
            }

            // Validate document and serial numbers
            if (documentNumber && !docSerialPattern.test(documentNumber)) {
                alert("Document number must contain only uppercase letters, numbers, or hyphens.");
                return;
            }

            if (serialNumber && !docSerialPattern.test(serialNumber)) {
                alert("Serial number must contain only uppercase letters, numbers, or hyphens.");
                return;
            }

            // Validate date found
            const dateFoundObj = new Date(dateFound);
            if (isNaN(dateFoundObj)) {
                alert("Please select a valid date.");
                return;
            }

            if (dateFoundObj > today) {
                alert("The date found cannot be in the future.");
                return;
            }

            if (dateFoundObj < fifteenDaysAgo) {
                alert("The date found cannot be earlier than 15 days ago.");
                return;
            }

            // If all validations pass, proceed
            const payload = {
                documentType: String(documentType),
                serialNumber: String(serialNumber),
                documentNumber: String(documentNumber),
                ownerFirstName: String(firstName),
                dateFound: String(dateFound),
                moreNotes: String(moreNotes),
                custodianId: String(custodianId)
            };

    
            // Send POST request
            $.ajax({
                url: "api/v1/document/new",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(payload),
                success: function() {
                    alert("Document registered successfully!");
                    closeForm('CustodianDetails');
                    closeForm('registerDocForm');
                },
                error: function(error) {
                    alert("Failed to register document. Please Try again later");
                }
            });
        });

        $('#searchDocForm').on('submit', function(e) {
            e.preventDefault();
        
            const docType = $('#docType').val().trim();
            const docNo = $('#docNumber').val().trim();
        
            // Encode query parameters
            const queryParams = `?docType=${encodeURIComponent(docType)}&docNo=${encodeURIComponent(docNo)}`;
        
            // Send GET request with query parameters
            $.ajax({
                url: `/api/v1/document/search${queryParams}`,
                type: "GET",
                success: function(response) {
                    if (response.documentNumber === docNo || response.serialNumber === docNo) {
                        sessionStorage.setItem("custodianId", response.custodianId);
                        sessionStorage.setItem("foundDocumentType", docType);
                        sessionStorage.setItem("foundDocumentNo", docNo);
                        $('#successModal').fadeIn();
                        closeForm('searchForm');
                        $('#searchDocForm')[0].reset();
                    }
                },
                error: function(error) {
                    if (error.status === 404) {
                        $('#errorModal').fadeIn();
                    } else {
                        alert("An error occurred while processing your request.");
                    }
                    $('#searchDocForm')[0].reset();
                }
            });
        });

        // Initially disable the owner verification input, check boxes and submit button
        $(".c-verification-box input, .c-form-check input, .c-submit-btn").prop("disabled", true);

        // Custodian Form: Handle Verify Button Click
        $(".c-verify-btn").on("click", function () {
            const firstName = $("#c-firstName").val().trim();
            const lastName = $("#c-lastName").val().trim();
            let phoneNumber = $("#c-phoneNumber").val().trim();
            const countryCode = $("#c-countryCode").val();
            const otpLength = 4;
        
            // Name Validation (Only letters & apostrophe, 2-12 characters)
            const namePattern = /^[A-Za-z']{2,12}$/;
        
            if (!namePattern.test(firstName)) {
                alert("First name must be 2-12 characters long and contain only letters or an apostrophe (').");
                return;
            }
        
            if (!namePattern.test(lastName)) {
                alert("Last name must be 2-12 characters long and contain only letters or an apostrophe (').");
                return;
            }
        
            // Phone Number Validation (Only numbers, 9-10 digits)
            const phonePattern = /^[0-9]{9,10}$/;
        
            // Remove leading zero if present
            if (phoneNumber.startsWith("0")) {
                phoneNumber = phoneNumber.substring(1);
            }
        
            if (!phonePattern.test(phoneNumber)) {
                alert("Phone number must be 9-10 digits and contain only numbers.");
                return;
            }
        
            // Final formatted phone number (with country code)
            const fullPhoneNumber = countryCode + phoneNumber;
        
        
            // Send verification request
            $.ajax({
                url: "/api/v1/custodian/register",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({ fullName: firstName + " " + lastName, phoneNumber: fullPhoneNumber }),
                success: function (response) {
                    sessionStorage.setItem("returnedCustodianID", response.message.split(".")[0].trim());
                    alert("Verification code sent. Please check your phone.");
                    $(".c-verification-box input, .c-form-check input, .c-submit-btn").prop("disabled", false);
                    $(".c-verification-box input").data("otpLength", otpLength);
                },
                error: function () {
                    alert("Error sending verification code. Please try again later.");
                }
            });
        });
        


        // Custodian Form: Handle OTP Input & Enable Submit Button
        $(".c-verification-box input").on("keyup", function (event) {            
            const currentInput = $(this);        
            // Handle number input
            if (event.key >= 0 && event.key <= 9) {
                if (currentInput.val().length === 1) {
                    currentInput.next("input").focus();
                }
            }
    
            // Handle backspace key
            if (event.key === "Backspace") {
                if (currentInput.val().length === 0) {
                    currentInput.prev("input").focus();
                }
            }
            
            let code = $(".c-verification-box input").map(function () {
                return $(this).val();
            }).get().join("");
        });
        
        // Custodian Form: Handle Form Submission
        $("#CustodianDetailsForm").on("submit", function (e) {
            e.preventDefault();

            const phoneNumber = $("#c-countryCode").val() + $("#c-phoneNumber").val().trim();
            const otp = $(".c-verification-box input").map(function () {
                return $(this).val();
            }).get().join("");
            const apiUrl = "/api/v1/custodian/verify-otp";
            const otpLength = 4;

            if (otp.length !== otpLength) {
                alert("Please enter the full 4-digit verification code.");
                return;
            }

            if (!$("#c-agreeTerms").prop("checked") || !$("#c-agreeThirdParty").prop("checked")) {
                alert("Please agree to both Terms & Conditions and Third-Party Data Sharing.");
                return;
            }

            $.ajax({
                url: apiUrl,
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({ phoneNumber, otp }),
                success: function () {
                    alert("Custodian verified successfully!");
                    openRegisterForm('registerDocForm');
                    $("#CustodianDetailsForm")[0].reset();
                    $(".c-verification-box input, .c-form-check input, .c-submit-btn").prop("disabled", true);
                },
                error: function () {
                    alert("Error verifying OTP. Please try again.");
                }
            });
        });

        // Initially disable the owner verification input, check boxes and submit button
        $(".o-verification-box input, .o-form-check input, .o-submit-btn").prop("disabled", true);

        // Owner Form: Handle Verify Button Click
        $("#ownerVerifyBtn").on("click", function () {    
            const firstName = $("#o-firstName").val().trim();
            const lastName = $("#o-lastName").val().trim();
            const phoneNumber = $("#o-phoneNumber").val().trim();
            const countryCode = $("#o-countryCode").val();
            const otpLength = 5;
    

            // Name Validation (Only letters & apostrophe, 2-12 characters)
            const namePattern = /^[A-Za-z']{2,12}$/;
        
            if (!namePattern.test(firstName)) {
                alert("First name must be 2-12 characters long and contain only letters or an apostrophe (').");
                return;
            }
        
            if (!namePattern.test(lastName)) {
                alert("Last name must be 2-12 characters long and contain only letters or an apostrophe (').");
                return;
            }
        
            // Phone Number Validation (Only numbers, 9-10 digits)
            const phonePattern = /^[0-9]{9,10}$/;
        
            // Remove leading zero if present
            if (phoneNumber.startsWith("0")) {
                phoneNumber = phoneNumber.substring(1);
            }
        
            if (!phonePattern.test(phoneNumber)) {
                alert("Phone number must be 9-10 digits and contain only numbers.");
                return;
            }
        
            // Final formatted phone number (with country code)
            const fullPhoneNumber = countryCode + phoneNumber;
        
        
            // Send verification request
            $.ajax({
                url: "/api/v1/owner/register",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({ fullName: firstName + " " + lastName, phoneNumber:fullPhoneNumber }),
                success: function () {
                    alert("Verification code sent. Please check your phone.");
                    $(".o-verification-box input, .o-form-check input, .o-submit-btn").prop("disabled", false);
                    $(".o-verification-box input").data("otpLength", otpLength);
                },
                error: function () {
                    alert("Error sending verification code. Please try again.");
                }
            });
        });
    
        // Owner Form: Handle OTP Input & Enable Submit Button
        $(".o-verification-box input").on("keyup", function (event) {
            const currentInput = $(this);
            let otpLength = 5;
        
            // Handle number input
            if (event.key >= 0 && event.key <= 9) {
                if (currentInput.val().length === 1) {
                    currentInput.next("input").focus();
                }
            }
    
            // Handle backspace key
            if (event.key === "Backspace") {
                if (currentInput.val().length === 0) {
                    currentInput.prev("input").focus();
                }
            }
    
            // Collect the verification code
            let code = $(".o-verification-box input").map(function () {
                return $(this).val();
            }).get().join("");
        
            if (code.length === otpLength && $("#OwnerAgreeTerms").prop("checked") && $("#OwnerAgreeThirdParty").prop("checked")) {
                $(".owner-submit-btn").prop("disabled", false);
            } else {
                $(".owner-submit-btn").prop("disabled", true);
            }
        });
    
        // Owner Form: Handle Form Submission
        $("#OwnerDetailsForm").on("submit", function (e) {
            e.preventDefault();
    
            const phoneNumber = $("#o-countryCode").val() + $("#o-phoneNumber").val().trim();
            let otp = $(".o-verification-box input").map(function () {
                return $(this).val();
            }).get().join("");
    
            let otpLength = $(".o-verification-box input").data("otpLength") || 5;
    
            if (otp.length !== otpLength) {
                alert(`Please enter the full ${otpLength}-digit verification code.`);
                return;
            }
    
            if (!$("#OwnerAgreeTerms").prop("checked") || !$("#OwnerAgreeThirdParty").prop("checked")) {
                alert("Please agree to both Terms & Conditions and Third-Party Data Sharing.");
                return;
            }
    
            $.ajax({
                url: "/api/v1/owner/verify-otp",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({ phoneNumber, otp }),
                success: function () {
                    alert("Owner verified successfully!");
                    window.location.href = "/custodian-details";
                    $("#OwnerDetailsForm")[0].reset();
                    $(".o-verification-box input, #OwnerDetails .form-check input, .o-submit-btn").prop("disabled", true);
                },
                error: function () {
                    alert("Error verifying OTP. Please try again.");
                }
            });
        });  

    });

    // Modal button handlers
    $('#closeButton').on('click', function() {
        $('#searchDocForm')[0].reset();
        closeForm('searchForm');
        $('#successModal').fadeOut(); 
    });

    $('#claimButton').on('click', function() {
        const foundDocumentNo = sessionStorage.getItem("foundDocumentNo");
        const foundDocumentType = sessionStorage.getItem("foundDocumentType");


        if (!foundDocumentNo && !foundDocumentType){
            alert("No document found. Please search a document first.");
            return;
        }
        openRegisterForm('OwnerDetails');
        $('#successModal').fadeOut();
    });

    // In error Modal 
    $('#noButton').on('click', function() {
        $('#searchDocForm')[0].reset();
        closeForm('searchForm');
        $('#errorModal').fadeOut();
    });

    $('#yesButton').on('click', function() {
        // Handle claim action (e.g., redirect to claim page)
        window.location.href = "/notify";
    });

    // Back to top button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 300) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });

    $('.back-to-top').click(function () {
        $('html, body').animate({scrollTop: 0}, 1500, 'easeInOutExpo');
        return false;
    });

})(jQuery);

// Form handling functions
function openRegisterForm(formId) {
    headerCarousel.trigger('stop.owl.autoplay');
    document.getElementById(formId).style.display = 'flex';
};

function openSearchForm() {
    // Stop carousels
    headerCarousel.trigger('stop.owl.autoplay');
    document.getElementById('searchForm').style.display = 'flex';
};

function closeForm(formId) {
    document.getElementById(formId).style.display = 'none';
    headerCarousel.trigger('play.owl.autoplay', [1500]);
};