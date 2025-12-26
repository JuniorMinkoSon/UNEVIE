/**
 * UNEVIE Admin Dashboard - Unified UI Logic
 * Handles sidebar, backdrops, dropdowns and active states.
 */

document.addEventListener('DOMContentLoaded', () => {
    // --- Elements ---
    const sidebar = document.getElementById('adminSidebar');
    const toggle = document.getElementById('sidebarToggle');
    const backdrop = document.getElementById('sidebarBackdrop');
    const themeToggle = document.getElementById('themeToggle');
    const html = document.documentElement;

    // --- 1. Sidebar Toggle ---
    const openSidebar = () => {
        if (!sidebar) return;
        sidebar.classList.remove('-translate-x-full');
        if (backdrop) {
            backdrop.classList.remove('hidden');
            // Force reflow for transition
            backdrop.offsetHeight;
            backdrop.classList.remove('opacity-0');
        }
    };

    const closeSidebar = () => {
        if (!sidebar) return;
        sidebar.classList.add('-translate-x-full');
        if (backdrop) {
            backdrop.classList.add('opacity-0');
            // Hide after transition
            setTimeout(() => {
                if (backdrop.classList.contains('opacity-0')) {
                    backdrop.classList.add('hidden');
                }
            }, 300);
        }
    };

    if (toggle && sidebar) {
        toggle.addEventListener('click', (e) => {
            e.stopPropagation();

            // Logic differs between Mobile and Desktop
            if (window.innerWidth < 1024) {
                // Mobile overlay behavior
                if (sidebar.classList.contains('-translate-x-full')) {
                    openSidebar();
                } else {
                    closeSidebar();
                }
            } else {
                // Desktop collapse behavior
                // Toggle 'lg:hidden' to hide/show from the flex layout
                sidebar.classList.toggle('lg:hidden');
            }
        });
    }

    if (backdrop) {
        backdrop.addEventListener('click', closeSidebar);
    }

    // --- 2. Dropdowns Handling ---
    const setupDropdown = (btnId, menuId) => {
        const btn = document.getElementById(btnId);
        const menu = document.getElementById(menuId);

        if (btn && menu) {
            btn.addEventListener('click', (e) => {
                e.stopPropagation();
                // Close other dropdowns
                document.querySelectorAll('[id$="DropdownMenu"]').forEach(m => {
                    if (m.id !== menuId) {
                        m.classList.add('hidden', 'opacity-0', 'scale-95');
                    }
                });

                if (menu.classList.contains('hidden')) {
                    menu.classList.remove('hidden');
                    // Force reflow
                    menu.offsetHeight;
                    menu.classList.remove('opacity-0', 'scale-95');
                } else {
                    menu.classList.add('opacity-0', 'scale-95');
                    setTimeout(() => {
                        if (menu.classList.contains('opacity-0')) {
                            menu.classList.add('hidden');
                        }
                    }, 200);
                }
            });
        }
    };

    setupDropdown('userDropdownBtn', 'userDropdownMenu');
    setupDropdown('langDropdownBtn', 'langDropdownMenu');

    // Global click to close dropdowns
    document.addEventListener('click', () => {
        document.querySelectorAll('[id$="DropdownMenu"]').forEach(menu => {
            if (!menu.classList.contains('hidden')) {
                menu.classList.add('opacity-0', 'scale-95');
                setTimeout(() => menu.classList.add('hidden'), 200);
            }
        });
    });

    // --- 3. Language & Theme ---
    window.changeLanguage = (lang) => {
        const url = new URL(window.location.href);
        url.searchParams.set('lang', lang);
        window.location.href = url.toString();
    };

    // Theme toggle
    const currentTheme = localStorage.getItem('admin-theme') || 'dark';
    if (currentTheme === 'dark') html.classList.add('dark');
    else html.classList.remove('dark');

    if (themeToggle) {
        themeToggle.addEventListener('click', () => {
            html.classList.toggle('dark');
            localStorage.setItem('admin-theme', html.classList.contains('dark') ? 'dark' : 'light');
        });
    }

    // --- 4. Navigation & Animations ---
    const currentPath = window.location.pathname;
    document.querySelectorAll('.nav-item').forEach(item => {
        const href = item.getAttribute('href');
        if (currentPath === href || (href !== '/admin/dashboard' && currentPath.startsWith(href))) {
            item.classList.add('active');
        }
    });

    // Animate table rows
    document.querySelectorAll('tbody tr').forEach((row, index) => {
        row.style.animationDelay = `${index * 0.05}s`;
        row.classList.add('animate-fadeIn');
    });
});
