
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
            const firstName = $('#ownerFirstName').val().trim();
            const lastName = $('#ownerLastName').val().trim();
            const documentNumber = $('#documentNumber').val().trim();
            const serialNumber = $('#serialNumber').val().trim();
            const dateFound = $('#dateFound').val();
            const moreNotes = $('#moreNotes').val().trim()
            const founderId = 12;

            // Validation regex patterns
            const namePattern = /^[A-Za-z]+$/;
            const docSerialPattern = /^[A-Z0-9-]+$/;

            // Current date
            const today = new Date();
            const fifteenDaysAgo = new Date();
            fifteenDaysAgo.setDate(today.getDate() - 15);

            // Validate names
            if (!namePattern.test(firstName)) {
                alert("Owner's first name must contain alphabetic characters only.");
                return;
            }

            if (lastName && !namePattern.test(lastName)) {
                alert("Owner's last name must contain alphabetic characters only.");
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
                documentType,
                serialNumber,
                documentNumber,
                ownerFirstName,
                dateFound,
                moreNotes,
                founderId
            };
    
            // Send POST request
            $.ajax({
                url: "api/v1/document/new",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(payload),
                success: function(response) {
                    alert("Document registered successfully!");
                    closeForm('registerForm');
                },
                error: function(error) {
                    alert("Failed to register document: " + error.responseText);
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
                    if (response.message === "Document found") {
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

        // Claim form 
        // Initially disable the submit button, verification fields, and checkboxes
        $(".verification-box input, .form-check input, .submit-btn").prop("disabled", true);

        // Handle Verify Button Click
        $("#verifyBtn").on("click", function () {
            const firstName = $("#firstName").val().trim();
            const lastName = $("#lastName").val().trim();
            const phoneNumber = $("#countryCode").val() + $("#phoneNumber").val().trim();

            if (!firstName || !lastName) {
                alert("Please enter your First Name and Last Name.");
                return;
            }

            if (!$("#phoneNumber").val().trim()) {
                alert("Please enter a valid phone number.");
                return;
            }

            // Send verification request
            $.ajax({
                url: "/api/v1/owner/register",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({ fullName: firstName + " " + lastName, phoneNumber }),
                success: function () {
                    alert("Verification code sent. Please check your phone.");
                    // Enable verification code input, checkboxes, and submit button
                    $(".verification-box input, .form-check input, .submit-btn").prop("disabled", false);
                },
                error: function () {
                    alert("Error sending verification code. Please try again.");
                }
            });
        });

        // Enable Submit Button only when verification is complete & checkboxes are checked
        $(".verification-box input").on("keyup", function () {
            let code = $(".verification-box input").map(function () {
                return $(this).val();
            }).get().join("");

            if (code.length === 5 && $("#agreeTerms").prop("checked") && $("#agreeThirdParty").prop("checked")) {
                $(".submit-btn").prop("disabled", false);
            } else {
                $(".submit-btn").prop("disabled", true);
            }
        });

        $(".form-check input").on("change", function () {
            let code = $(".verification-box input").map(function () {
                return $(this).val();
            }).get().join("");

            if (code.length === 5 && $("#agreeTerms").prop("checked") && $("#agreeThirdParty").prop("checked")) {
                $(".submit-btn").prop("disabled", false);
            } else {
                $(".submit-btn").prop("disabled", true);
            }
        });

        // Handle Form Submission
        $("#claimDocumentForm").on("submit", function (e) {
            e.preventDefault();

            const phoneNumber = $("#countryCode").val() + $("#phoneNumber").val().trim();
            const otp = $(".verification-box input").map(function () {
                return $(this).val();
            }).get().join("");

            if (otp.length !== 5) {
                alert("Please enter the full 5-digit verification code.");
                return;
            }

            if (!$("#agreeTerms").prop("checked") || !$("#agreeThirdParty").prop("checked")) {
                alert("Please agree to both Terms & Conditions and Third-Party Data Sharing.");
                return;
            }

            // Send claim request
            $.ajax({
                url: "/api/v1/owner/verify-otp",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({
                    phoneNumber,
                    otp
                }),
                success: function () {
                    alert("Details validation successful!");
                    $("#claimDocumentForm")[0].reset(); // Reset form
                    $(".verification-box input, .form-check input, .submit-btn").prop("disabled", true);
                },
                error: function () {
                    alert("Error claiming document. Please try again.");
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
        // Handle claim action (e.g., redirect to claim page)
        window.location.href = "/claim-document";
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
function openRegisterForm() {
    headerCarousel.trigger('stop.owl.autoplay');
    document.getElementById('registerForm').style.display = 'flex';
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