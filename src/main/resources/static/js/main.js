
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
            // Handle register form submission
            closeForm('registerForm');
        });

        $('#searchDocForm').on('submit', function(e) {
            e.preventDefault();
            // Handle search form submission
            closeForm('searchForm');
        });
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
    console.log("Register Form called")
    headerCarousel.trigger('stop.owl.autoplay');
    document.getElementById('registerForm').style.display = 'flex';
};

function openSearchForm() {
    // Stop carousels
    console.log("Search Form called")
    headerCarousel.trigger('stop.owl.autoplay');
    document.getElementById('searchForm').style.display = 'flex';
};

function closeForm(formId) {
    document.getElementById(formId).style.display = 'none';
    headerCarousel.trigger('play.owl.autoplay', [1500]);
};