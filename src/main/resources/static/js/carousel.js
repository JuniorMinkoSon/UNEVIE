const images = document.querySelectorAll('.carousel-images img');
const indicators = document.querySelectorAll('.indicator');
let current = 0;

setInterval(() => {
    images[current].classList.remove('active');
    indicators[current].classList.remove('active');

    current = (current + 1) % images.length;

    images[current].classList.add('active');
    indicators[current].classList.add('active');
}, 4000);
