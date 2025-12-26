// =====================
// script.js — UX globale
// =====================

document.addEventListener('DOMContentLoaded', () => {

    // --- Smooth scroll ---
    document.querySelectorAll('a[href^="#"]').forEach(a => {
        a.addEventListener('click', e => {
            e.preventDefault();
            const t = document.querySelector(a.getAttribute('href'));
            if (t) t.scrollIntoView({ behavior: 'smooth', block: 'start' });
        });
    });

    // --- Menu mobile toggle ---
    const menuBtn = document.querySelector('.js-menu-toggle');
    const nav = document.querySelector('nav');
    if (menuBtn && nav) {
        menuBtn.addEventListener('click', () => {
            nav.classList.toggle('open');
            menuBtn.classList.toggle('active');
        });
    }

    // --- Intersection Observer for Scroll Animations ---
    const revealElements = document.querySelectorAll('.reveal, .reveal-left, .reveal-right, .reveal-zoom');

    const revealCallback = (entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('active');
                // Optionnel: arrêter d'observer une fois animé
                // observer.unobserve(entry.target);
            }
        });
    };

    const revealObserver = new IntersectionObserver(revealCallback, {
        threshold: 0.1, // Déclencher quand 10% de l'élément est visible
        rootMargin: '0px 0px -50px 0px' // Déclencher légèrement avant l'entrée complète
    });

    revealElements.forEach(el => revealObserver.observe(el));
});
