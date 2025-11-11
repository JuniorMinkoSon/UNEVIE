// Carousel functionality for homepage
document.addEventListener('DOMContentLoaded', () => {
    const carousel = document.getElementById('mainCarousel');
    if (!carousel) return;

    const track = carousel.querySelector('.carousel-track');
    const slides = carousel.querySelectorAll('.carousel-slide');
    const prevBtn = document.getElementById('carouselPrev');
    const nextBtn = document.getElementById('carouselNext');
    const indicators = carousel.querySelectorAll('.indicator');
    
    let currentSlide = 0;
    const totalSlides = slides.length;

    // Function to show a specific slide
    function showSlide(index) {
        // Remove active class from all slides and indicators
        slides.forEach(slide => slide.classList.remove('active'));
        indicators.forEach(indicator => indicator.classList.remove('active'));

        // Add active class to current slide and indicator
        slides[index].classList.add('active');
        if (indicators[index]) {
            indicators[index].classList.add('active');
        }

        // Update track position
        track.style.transform = `translateX(-${index * 100}%)`;
        currentSlide = index;
    }

    // Next slide
    function nextSlide() {
        const next = (currentSlide + 1) % totalSlides;
        showSlide(next);
    }

    // Previous slide
    function prevSlide() {
        const prev = (currentSlide - 1 + totalSlides) % totalSlides;
        showSlide(prev);
    }

    // Event listeners
    if (nextBtn) {
        nextBtn.addEventListener('click', nextSlide);
    }

    if (prevBtn) {
        prevBtn.addEventListener('click', prevSlide);
    }

    // Indicator click handlers
    indicators.forEach((indicator, index) => {
        indicator.addEventListener('click', () => {
            showSlide(index);
        });
    });

    // Auto-play carousel
    let autoPlayInterval = setInterval(nextSlide, 5000);

    // Pause on hover
    carousel.addEventListener('mouseenter', () => {
        clearInterval(autoPlayInterval);
    });

    carousel.addEventListener('mouseleave', () => {
        autoPlayInterval = setInterval(nextSlide, 5000);
    });

    // Keyboard navigation
    document.addEventListener('keydown', (e) => {
        if (e.key === 'ArrowLeft') prevSlide();
        if (e.key === 'ArrowRight') nextSlide();
    });

    // Initialize first slide
    showSlide(0);
});


