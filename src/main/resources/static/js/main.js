// =====================
// main.js — interactions BarikaWeb
// =====================

// --- Bouton WhatsApp ---
document.addEventListener('click', e => {
    const btn = e.target.closest('.js-whatsapp');
    if (btn) {
        const phone = btn.dataset.phone || '+22500000000';
        const text = encodeURIComponent(btn.dataset.text || 'Bonjour, je souhaite un devis.');
        window.open(`https://wa.me/${phone}?text=${text}`, '_blank');
    }
});

// --- Wishlist local ---
function toggleWishlist(id) {
    const key = 'wishlist';
    let w = JSON.parse(localStorage.getItem(key) || '[]');
    if (w.includes(id)) w = w.filter(x => x !== id);
    else w.push(id);
    localStorage.setItem(key, JSON.stringify(w));
    showToast('Mis à jour dans vos favoris ❤️');
}

// --- Toast notification ---
function showToast(message) {
    let toast = document.createElement('div');
    toast.className = 'toast-message';
    toast.textContent = message;
    document.body.appendChild(toast);
    setTimeout(() => toast.classList.add('show'), 100);
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 2500);
}
