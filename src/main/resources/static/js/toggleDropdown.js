function toggleDropdown(event) {
    event.stopPropagation();
    const menu = document.getElementById("dropdownMenu");
    menu.classList.toggle("show");
}

window.onclick = function(event) {
    const menu = document.getElementById("dropdownMenu");
    if (menu && !event.target.closest('.profile-dropdown')) {
        menu.classList.remove('show');
    }
}