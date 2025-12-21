// =====================
// cart.js — panier localStorage
// =====================

const CART_KEY = 'ecom_cart_v1';

function getCart() {
    return JSON.parse(localStorage.getItem(CART_KEY) || '[]');
}

function saveCart(c) {
    localStorage.setItem(CART_KEY, JSON.stringify(c));
}

function addToCart(product) {
    const cart = getCart();
    const idx = cart.findIndex(p => p.id === product.id);
    if (idx > -1) cart[idx].qty += product.qty || 1;
    else cart.push({ ...product, qty: product.qty || 1 });
    saveCart(cart);
    showCartNotification(product.nom);
}

function removeFromCart(id) {
    saveCart(getCart().filter(p => p.id !== id));
}

function clearCart() {
    saveCart([]);
}

// --- Animation / Feedback visuel ---
function showCartNotification(productName) {
    let n = document.createElement('div');
    n.className = 'cart-notice';
    n.innerHTML = `<strong>${productName}</strong> ajouté au panier 🛒`;
    document.body.appendChild(n);
    setTimeout(() => n.classList.add('show'), 100);
    setTimeout(() => {
        n.classList.remove('show');
        setTimeout(() => n.remove(), 300);
    }, 3000);
}

// --- Gestion des clics sur “Acheter” ---
document.addEventListener('click', e => {
    const btn = e.target.closest('.btn-primary');
    if (btn && btn.textContent.trim().toLowerCase().includes('acheter')) {
        const card = btn.closest('.card-product');
        if (!card) return;
        const product = {
            id: card.getAttribute('data-id') || Date.now(),
            nom: card.querySelector('h3')?.textContent.trim(),
            prix: card.querySelector('.price')?.textContent.trim(),
            qty: 1
        };
        addToCart(product);
    }
});
